@file:JvmName("AccessibilityServiceChecker")

package com.security.anti.fraud

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.view.accessibility.AccessibilityManager


val installerID =
    listOf(
        InstallerID.GOOGLE_PLAY, InstallerID.GALAXY_APPS, InstallerID.HUAWEI_APP_GALLERY,
        InstallerID.VIVO_APP_STORE, InstallerID.OPPO_APP_STORE, InstallerID.XIAOMI_APP_STORE
    )

fun Context.verifyInstallerAccessibilityService(): AccessibilityCheckerModel {
    val accessibilityManager =
        getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    )
    val listOfVerifyError = mutableMapOf<String, String>()
    enabledServices.forEach { enable ->
        val packageName = enable.resolveInfo.serviceInfo.packageName
        val appLabel = getApplicationLabelName(packageName)
        if (!verifyInstallerId(packageName)) {
            listOfVerifyError[appLabel] = packageName
        }
    }
    return AccessibilityCheckerModel(
        listOfVerifyError.isEmpty(),
        listOfVerifyError
    )
}

// Ref : https://github.com/javiersantos/PiracyChecker
fun Context.verifyInstallerId(
    packageName: String
): Boolean {
    val validInstallers = ArrayList<String>()
    val installer = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            packageManager.getInstallSourceInfo(packageName).installingPackageName
        } else {
            packageManager.getInstallerPackageName(packageName)
        }
    } catch (e: Exception) {
        null
    }
    for (id in installerID) {
        validInstallers.addAll(id.toIDs())
    }
    val isVerifyPass = installer != null && validInstallers.contains(installer)
    return isVerifyPass
}

fun Context.getApplicationLabelName(packageName: String): String {
    return try {
        packageManager.getApplicationLabel(
            packageManager.getApplicationInfoCompat(
                packageName,
                0
            )
        ).toString()
    } catch (e: Exception) {
        "null"
    }
}

fun PackageManager.getApplicationInfoCompat(packageName: String, flags: Int = 0): ApplicationInfo =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(flags.toLong()))
    } else {
        @Suppress("DEPRECATION") getApplicationInfo(packageName, flags)
    }
