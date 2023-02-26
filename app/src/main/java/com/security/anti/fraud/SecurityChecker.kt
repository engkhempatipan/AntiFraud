package com.security.anti.fraud

import android.app.Activity
import android.hardware.display.DisplayManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.security.anti.fraud.model.DisplayManagerModel
import com.security.anti.fraud.model.Model

class SecurityChecker {
    /**
     * Detect -> Screen casting, Remote application [TeamViewer,AnyDesk]
     */
    fun checkDisplayPresentation(activity: Activity): DisplayManagerModel? {
        var deviceProductInfo = ""
        var displayName = ""
        val displayManager =
            activity.getSystemService(AppCompatActivity.DISPLAY_SERVICE) as DisplayManager
        val presentationDisplays =
            displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)
        if (presentationDisplays.isNotEmpty()) {
            val display = presentationDisplays[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                display.deviceProductInfo?.let {
                    deviceProductInfo = it.name.toString()
                }
            }
            displayName = display.name
            return if (deviceProductInfo.isNotEmpty()) {
                DisplayManagerModel(
                    displayName = displayName,
                    deviceProductInfo = deviceProductInfo,
                    customWording = "DeviceProduct info = [$deviceProductInfo ], \nDisplayName = [$displayName]"
                )
            } else {
                "DisplayName: = [$displayName]"
                DisplayManagerModel(
                    displayName = displayName,
                    deviceProductInfo = null,
                    customWording = "DisplayName: = [$displayName]"
                )
            }
        } else {
            return null
        }
    }

    fun getAccessibilityEnabledList(activity: Activity): String? {
        val listEnabled: List<Model> = activity.findAllAccessibilityEnabledInfo()
        if (listEnabled.isEmpty())
            return null

        var accessibilityEnabledString = ""
        listEnabled.forEachIndexed { _, item ->
            accessibilityEnabledString += "Package name = [" + item.packageName + "]\n" +
                    "App name =[${item.appName} ]\n" +
                    "Installer id =[${item.installerID}]\n\n"
        }
        return accessibilityEnabledString
    }

    fun getUntrustedApp(activity: Activity): String? {
        val untrustedList: List<Model> =
            activity.findAllAccessibilityEnabledInfo().filter { !it.isTrustedApp }
        if (untrustedList.isEmpty())
            return null

        var untrustedListString = ""
        untrustedList.forEachIndexed { _: Int, item: Model ->
            untrustedListString += "Package name = [" + item.packageName + "]\n" +
                    "App name =[${item.appName} ]\n" +
                    "Installer id =[${item.installerID}]\n\n"
        }
        return untrustedListString
    }
}