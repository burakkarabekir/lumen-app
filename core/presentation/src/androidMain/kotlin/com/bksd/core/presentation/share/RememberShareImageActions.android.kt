package com.bksd.core.presentation.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
actual fun rememberShareImageActions(): ShareImageActions {
    val context = LocalContext.current
    return remember(context) {
        ShareImageActions(
            onShare = { png ->
                val uri = cacheImageUri(context, png)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "image/png"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(
                    Intent.createChooser(intent, null)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            },
            onSave = { png -> saveImageToGallery(context, png) },
            onCopy = { png ->
                try {
                    val uri = cacheImageUri(context, png)
                    val clip = ClipData.newUri(
                        context.contentResolver,
                        "Lumen moment",
                        uri
                    )
                    val clipboard =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(clip)
                    true
                } catch (e: Exception) {
                    false
                }
            },
        )
    }
}

private fun saveImageToGallery(context: Context, png: ByteArray): Boolean {
    return try {
        val values = ContentValues().apply {
            put(
                MediaStore.Images.Media.DISPLAY_NAME,
                "moment_${System.currentTimeMillis()}.png"
            )
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Lumen")
            }
        }
        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        ) ?: return false
        context.contentResolver.openOutputStream(uri)?.use { it.write(png) } ?: return false
        true
    } catch (e: Exception) {
        false
    }
}

private fun cacheImageUri(context: Context, png: ByteArray): Uri {
    val file = File(context.cacheDir, "shared/moment_${System.currentTimeMillis()}.png")
    file.parentFile?.mkdirs()
    file.writeBytes(png)
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
