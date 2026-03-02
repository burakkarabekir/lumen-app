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
import androidx.core.content.FileProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    var tempCameraFilePath by rememberSaveable { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            coroutineScope.launch(Dispatchers.IO) {
                val file = copyUriToTempFile(context, it)
                withContext(Dispatchers.Main) {
                    if (file != null) {
                        onResult(MediaPickResult.Success(file.absolutePath, MediaType.PHOTO, file.length()))
                    } else {
                        onResult(MediaPickResult.Error("Failed to process photo"))
                    }
                }
            }
        } ?: onResult(MediaPickResult.Cancelled)
    }

    val videoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            coroutineScope.launch(Dispatchers.IO) {
                val file = copyUriToTempFile(context, it)
                withContext(Dispatchers.Main) {
                    if (file != null) {
                        onResult(MediaPickResult.Success(file.absolutePath, MediaType.VIDEO, file.length()))
                    } else {
                        onResult(MediaPickResult.Error("Failed to process video"))
                    }
                }
            }
        } ?: onResult(MediaPickResult.Cancelled)
    }

    val cameraPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempCameraFilePath != null) {
            val file = File(tempCameraFilePath!!)
            if (file.exists()) {
                onResult(MediaPickResult.Success(file.absolutePath, MediaType.PHOTO, file.length()))
            } else {
                onResult(MediaPickResult.Error("Camera capture failed or file not found"))
            }
        } else {
            // Clean up empty file if user cancelled
            tempCameraFilePath?.let { File(it).delete() }
            onResult(MediaPickResult.Cancelled)
        }
        tempCameraFilePath = null
    }

    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            coroutineScope.launch(Dispatchers.IO) {
                val file = copyUriToTempFile(context, it)
                withContext(Dispatchers.Main) {
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
                }
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
                try {
                    val fileName = "camera_capture_${System.currentTimeMillis()}.jpg"
                    val file = File(context.cacheDir, fileName)
                    tempCameraFilePath = file.absolutePath
                    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                    cameraPicker.launch(uri)
                } catch (e: Exception) {
                    onResult(MediaPickResult.Error("Failed to launch camera: ${e.message}"))
                }
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
