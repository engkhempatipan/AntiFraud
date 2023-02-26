@file:JvmName("AccessibilityServiceChecker")

package com.security.anti.fraud.checker

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.view.accessibility.AccessibilityManager
import android.util.Log
import com.security.anti.fraud.model.Model

private val installerID = InstallerID.values()
    .filter { it != InstallerID.AMAZON_APP_STORE }
    .toList()

private val packageWhitelistId = WhitelistPackageId.values().toList()

fun Context.findAllAccessibilityEnabledInfo(): List<Model> {
    val listModel = mutableListOf<Model>()
    val accessibilityManager =
        getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
    val enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(
        AccessibilityServiceInfo.FEEDBACK_ALL_MASK
    )
    enabledServices.mapNotNull { service ->
        val serviceInfo = service.resolveInfo.serviceInfo
        val packageName = service.resolveInfo.serviceInfo.packageName
        val appLabel = getApplicationLabelName(packageName)
        val installerId = getInstallerId(packageName)
        val isTrustedApp = verifyInstallerId(serviceInfo)
        listModel.add(
            Model(
                packageName = packageName,
                appName = appLabel,
                installerID = installerId,
                serviceInfo = serviceInfo,
                isTrustedApp = isTrustedApp
            )
        )
    }
    return listModel
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
    Log.e("isSystemApp", "Accessibility isSystemApp : $isSystemApp")
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
