package com.bksd.core.presentation.media

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.bksd.core.domain.model.MediaType
import java.io.File
import java.io.FileOutputStream

actual class MediaPickerLauncher(
    private val onPickPhoto: () -> Unit,
    private val onPickVideo: () -> Unit,
    private val onPickFile: (List<String>) -> Unit,
    private val onLaunchCamera: () -> Unit
) {
    actual fun launchCamera() = onLaunchCamera()
    actual fun launchPhotoPicker() = onPickPhoto()
    actual fun launchVideoPicker() = onPickVideo()
    actual fun launchFilePicker(mimeTypes: List<String>) = onPickFile(mimeTypes)
}

@Composable
actual fun rememberMediaPickerLauncher(onResult: (MediaPickResult) -> Unit): MediaPickerLauncher {
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val file = copyUriToTempFile(context, it)
            if (file != null) {
                onResult(MediaPickResult.Success(file.absolutePath, MediaType.PHOTO, file.length()))
            } else {
                onResult(MediaPickResult.Error("Failed to process photo"))
            }
        } ?: onResult(MediaPickResult.Cancelled)
    }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val file = copyUriToTempFile(context, it)
            if (file != null) {
                onResult(MediaPickResult.Success(file.absolutePath, MediaType.VIDEO, file.length()))
            } else {
                onResult(MediaPickResult.Error("Failed to process video"))
            }
        } ?: onResult(MediaPickResult.Cancelled)
    }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val file = copyUriToTempFile(context, it)
            if (file != null) {
                val type = if (context.contentResolver.getType(it)?.startsWith("video") == true) {
                    MediaType.VIDEO
                } else if (context.contentResolver.getType(it)?.startsWith("audio") == true) {
                    MediaType.AUDIO
                } else {
                    MediaType.PHOTO // fallback
                }
                onResult(MediaPickResult.Success(file.absolutePath, type, file.length()))
            } else {
                onResult(MediaPickResult.Error("Failed to process file"))
            }
        } ?: onResult(MediaPickResult.Cancelled)
    }

    return remember {
        MediaPickerLauncher(
            onPickPhoto = {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onPickVideo = {
                videoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly))
            },
            onPickFile = { mimetypes ->
                val type = mimetypes.firstOrNull() ?: "*/*"
                filePicker.launch(type)
            },
            onLaunchCamera = {
                // To implement camera capture properly, we need a FileProvider URI
                // For this architecture demo, we'll return an error indicating it needs setup
                onResult(MediaPickResult.Error("Camera capture requires FileProvider setup in Manifest"))
            }
        )
    }
}

private fun copyUriToTempFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        var fileName = "temp_media_${System.currentTimeMillis()}"

        // Try getting original name
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                fileName = cursor.getString(nameIndex)
            }
        }

        val tempFile = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(tempFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
