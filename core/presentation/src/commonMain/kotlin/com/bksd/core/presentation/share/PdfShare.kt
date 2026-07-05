package com.bksd.core.presentation.share

import androidx.compose.runtime.Composable

class PdfShareAction(
    private val onShare: (pdf: ByteArray, fileName: String) -> Unit,
) {
    fun share(pdf: ByteArray, fileName: String) = onShare(pdf, fileName)
}

@Composable
expect fun rememberPdfShareAction(): PdfShareAction
