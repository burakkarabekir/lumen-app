package com.bksd.core.data.appinfo

import com.bksd.core.domain.appinfo.AppInfoProvider
import platform.Foundation.NSBundle

class IosAppInfoProvider : AppInfoProvider {

    override val versionName: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String
            ?: ""

    override val buildNumber: String
        get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleVersion") as? String ?: ""

    override val storeUrl: String
        get() = "https://apps.apple.com/app/id000000000"
}
