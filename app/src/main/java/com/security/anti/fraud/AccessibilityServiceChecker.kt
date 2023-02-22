@file:JvmName("AccessibilityServiceChecker")

package com.security.anti.fraud

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityManager
import android.util.Log

private val installerID = InstallerID.values()
    .filter { it != InstallerID.AMAZON_APP_STORE }
    .toList()

private val packageWhitelistId = WhitelistPackageId.values().toList()

fun Context.verifyInstallerAccessibilityService(): AccessibilityCheckerModel {
    val accessibilityManager =
        getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    )
    val listOfVerifyError = verifyEnableServicesInstaller(enabledServices)
    return AccessibilityCheckerModel(
        listOfVerifyError.isEmpty(),
        listOfVerifyError
    ).also {
        Log.e(
            "",
            """
            AccessibilityModelChecker
            IsVerifyPass: ${it.isVerifyPass}
            ListOfError: ${it.listOfVerifyError.entries}
            """.trimIndent()
        )
    }
}

fun Context.findAllEnabled(): Map<String, String> {
    val accessibilityManager =
        getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    )
    return enabledServices
        .mapNotNull { service ->
            val packageName = service.resolveInfo.serviceInfo.packageName
            val appLabel = getApplicationLabelName(packageName)
            Log.e("", "Accessibility enable Name =$appLabel: Package =$packageName")
            Pair(packageName, appLabel)
        }
        .toMap()
}

fun Context.verifyEnableServicesInstaller(
    enabledServices: List<AccessibilityServiceInfo>
): Map<String, String> {
    enabledServices
    return enabledServices
        .mapNotNull { service ->
            val serviceInfo = service.resolveInfo.serviceInfo
            val packageName = service.resolveInfo.serviceInfo.packageName
            val appLabel = getApplicationLabelName(packageName)
            Log.e("", "Accessibility enable Name =$appLabel: Package =$packageName")

            if (!verifyInstallerId(serviceInfo)) {
                Pair(packageName, appLabel)
            } else {
                null
            }
        }
        .toMap()
}

// Ref : https://github.com/javiersantos/PiracyChecker
fun Context.verifyInstallerId(
    serviceInfo: ServiceInfo
): Boolean {
    val packageName = serviceInfo.packageName
    val applicationInfo = serviceInfo.applicationInfo
    val installer = getInstallerId(packageName)
    return (isValidInstaller(packageName) // || isWhitelistPackage(packageName)
            || isSystemApp(applicationInfo)
            )
        .also {
            Log.e("", "Accessibility INSTALLER NAME : $installer IS PASS :$it")
            Log.e("", "Accessibility __________________________________________________")
        }
}

fun Context.isValidInstaller(
    packageName: String
): Boolean {
    val validInstallers = installerID.flatMap { it.toIDs() }
    val installer = getInstallerId(packageName)
    return (installer != null && validInstallers.contains(installer))
}

private fun isSystemApp(appInfo: ApplicationInfo): Boolean {
    val isSystemApp = appInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM
    Log.e("", "Accessibility isSystemApp : $isSystemApp")
    return isSystemApp
}


fun Context.isWhitelistPackage(
    packageName: String
): Boolean {
    val validPackageId = packageWhitelistId.flatMap { it.toIDs() }
    return validPackageId.contains(packageName)
}

fun Context.getInstallerId(
    packageName: String
): String? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            packageManager.getInstallSourceInfo(packageName).installingPackageName
        } else {
            packageManager.getInstallerPackageName(packageName)
        }
    } catch (e: Exception) {
        null
    }
}

fun Context.getApplicationLabelName(packageName: String): String {
    return try {
        val appInfo = packageManager.getApplicationInfoCompat(packageName, 0)
        packageManager.getApplicationLabel(appInfo).toString()
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
