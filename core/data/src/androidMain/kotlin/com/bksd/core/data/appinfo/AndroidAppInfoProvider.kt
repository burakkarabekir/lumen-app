package com.bksd.core.data.appinfo

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import com.bksd.core.domain.appinfo.AppInfoProvider

class AndroidAppInfoProvider(private val context: Context) : AppInfoProvider {

    private val packageInfo
        get() = context.packageManager.getPackageInfo(context.packageName, 0)

    override val versionName: String
        get() = runCatching { packageInfo.versionName }.getOrNull() ?: ""

    override val buildNumber: String
        get() = runCatching {
            PackageInfoCompat.getLongVersionCode(packageInfo).toString()
        }.getOrNull() ?: ""

    override val storeUrl: String
        get() = "https://play.google.com/store/apps/details?id=${context.packageName}"
}
