package com.bksd.core.domain.appinfo

interface AppInfoProvider {
    val versionName: String
    val buildNumber: String
    val storeUrl: String
}
