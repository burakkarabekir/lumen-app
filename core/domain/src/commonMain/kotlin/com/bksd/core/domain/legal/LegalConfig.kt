package com.bksd.core.domain.legal

object LegalConfig {
    const val POLICY_VERSION = "2026-07-19"
    const val TERMS_URL = "https://lumenjournalapp.com/terms"
    const val PRIVACY_URL = "https://lumenjournalapp.com/privacy"

    val POLICY_VERSION_DISPLAY: String = POLICY_VERSION.split("-").let { parts ->
        if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else POLICY_VERSION
    }
}
