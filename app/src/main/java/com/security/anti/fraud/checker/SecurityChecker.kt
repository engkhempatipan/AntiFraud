package com.security.anti.fraud.checker

import android.app.Activity
import android.hardware.display.DisplayManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.security.anti.fraud.checker.callback.AccessibilityEnabledListCallback
import com.security.anti.fraud.checker.callback.AccessibilityUntrustedEnabledListCallback
import com.security.anti.fraud.checker.callback.DisplayCheckerCallback
import com.security.anti.fraud.model.DisplayManagerModel
import com.security.anti.fraud.model.Model

class SecurityChecker : SecurityCheckerInterface {
    override fun checkDisplayPresentation(
        activity: Activity,
        callback: DisplayCheckerCallback
    ) {
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
            if (deviceProductInfo.isNotEmpty()) {
                val model = DisplayManagerModel(
                    displayName = displayName,
                    deviceProductInfo = deviceProductInfo,
                    customWording = "DeviceProduct info = [$deviceProductInfo ], \nDisplayName = [$displayName]"
                )
                callback.onDetected(model)

            } else {
                val model = DisplayManagerModel(
                    displayName = displayName,
                    deviceProductInfo = null,
                    customWording = "DisplayName: = [$displayName]"
                )
                callback.onDetected(model)
            }
        }
    }

    override fun getAccessibilityEnabledList(
        activity: Activity,
        callback: AccessibilityEnabledListCallback
    ) {
        val listEnabled: List<Model> = activity.findAllAccessibilityEnabledInfo()
        if (listEnabled.isEmpty())
            return
        var accessibilityEnabledString = ""
        listEnabled.forEach { item ->
            accessibilityEnabledString += "Package name = [" + item.packageName + "]\n" +
                    "App name =[${item.appName} ]\n" +
                    "Installer id =[${item.installerID}]\n\n"
        }
        callback.onDetected(listEnabled, accessibilityEnabledString)
    }

    override fun getUntrustedApp(
        activity: Activity,
        callback: AccessibilityUntrustedEnabledListCallback
    ) {
        val untrustedList: List<Model> =
            activity.findAllAccessibilityEnabledInfo().filter { !it.isTrustedApp }
        if (untrustedList.isEmpty())
            return

        var untrustedListString = ""
        untrustedList.forEach { item: Model ->
            untrustedListString += "Package name = [" + item.packageName + "]\n" +
                    "App name =[${item.appName} ]\n" +
                    "Installer id =[${item.installerID}]\n\n"
        }
        callback.onDetected(untrustedList, untrustedListString)
    }
}