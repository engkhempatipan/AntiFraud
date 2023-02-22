package com.security.anti.fraud

import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {

    private lateinit var textViewTitleAccessibility: TextView
    private lateinit var textViewAccessibility: TextView
    private lateinit var textViewTitleDisplay: TextView
    private lateinit var textViewDisplayManager: TextView
    private lateinit var textViewTitleAllAccessibilityEnabled: TextView
    private lateinit var textViewAllAccessibilityEnabled: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bindingView
        textViewTitleAccessibility = findViewById(R.id.text_view_title_accessibility)
        textViewAccessibility = findViewById(R.id.text_view_accessibility)
        textViewTitleDisplay = findViewById(R.id.text_view_title_display)
        textViewDisplayManager = findViewById(R.id.text_view_display)
        textViewTitleAllAccessibilityEnabled = findViewById(R.id.text_view_title_all_accessibility)
        textViewAllAccessibilityEnabled = findViewById(R.id.text_view_all_accessibility)
    }

    override fun onResume() {
        super.onResume()
        scanUnSecureApps()
    }

    private fun verifyScreenCasting(): String {
        var deviceProductInfo = ""
        var name = ""
        val displayManager = getSystemService(DISPLAY_SERVICE) as DisplayManager
        val presentationDisplays =
            displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION)
        if (presentationDisplays.isNotEmpty()) {
            val display = presentationDisplays[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                display.deviceProductInfo?.let {
                    deviceProductInfo = it.name.toString()
                }
            }
            name = display.name
            return if (deviceProductInfo.isNotEmpty()) {
                "DeviceProduct info = [$deviceProductInfo ], \nDisplayName = [$name]"
            } else {
                "DisplayName: = [$name]"
            }
        } else {
            return ""
        }

    }

    private fun verifyAccessibilityServiceChecker(): String {
        var insecureList = ""
        val accessibilityCheckerModel: AccessibilityCheckerModel =
            this.verifyInstallerAccessibilityService()
        if (!accessibilityCheckerModel.isVerifyPass) {
            Log.d(
                "accessibility",
                "accessibilityx:>" + accessibilityCheckerModel.listOfVerifyError.entries.toString()
            )

            accessibilityCheckerModel.listOfVerifyError.entries.forEachIndexed { index, item ->
                insecureList += "Package name = [" + item.key + "]\nApp name =[${item.value} ]\n\n"
                Log.d("accessibility", " values[$index]: " + item.key)
            }
        }
        return insecureList
    }

    private fun scanUnSecureApps() {
        showAccessibility()
        showDisplay()
        showAllAccessibilityEnabled()

        getDisplayManagerList()
        getUnTrustAccessibilityEnabledList()
        getAllAccessibilityEnabledList()
    }

    private fun getAllAccessibilityEnabledList() {
        val listEnabled = findAllEnabled()
        if (listEnabled.entries.isEmpty()) {
            hideAllAccessibilityEnabled()
        }
        var textAllAccessibilityEnalbed = ""
        listEnabled.entries.forEachIndexed { index, item ->
            textAllAccessibilityEnalbed += "Package name = [" + item.key + "]\nApp name =[${item.value} ]\n\n"
        }
        textViewAllAccessibilityEnabled.text = textAllAccessibilityEnalbed
        Log.d("listEanbled", "listEnabled: = $listEnabled")
    }

    private fun getDisplayManagerList() {
        val displayDetectedList = verifyScreenCasting()
        if (displayDetectedList.isEmpty()) {
            hideDisplay()
        } else {
            textViewDisplayManager.text = displayDetectedList
        }
    }

    private fun getUnTrustAccessibilityEnabledList() {
        val accessibilityDetectedList = verifyAccessibilityServiceChecker()
        if (accessibilityDetectedList.isEmpty()) {
            hideAccessibility()
        } else {
            textViewAccessibility.text = accessibilityDetectedList
        }

    }

    private fun hideDisplay() {
        textViewTitleDisplay.visibility = View.GONE
        textViewDisplayManager.visibility = View.GONE
    }

    private fun showDisplay() {
        textViewTitleDisplay.visibility = View.VISIBLE
        textViewDisplayManager.visibility = View.VISIBLE
    }

    private fun hideAccessibility() {
        textViewTitleAccessibility.visibility = View.GONE
        textViewAccessibility.visibility = View.GONE
    }

    private fun showAccessibility() {
        textViewTitleAccessibility.visibility = View.VISIBLE
        textViewAccessibility.visibility = View.VISIBLE
    }

    private fun hideAllAccessibilityEnabled() {
        textViewTitleAllAccessibilityEnabled.visibility = View.GONE
        textViewAllAccessibilityEnabled.visibility = View.GONE
    }

    private fun showAllAccessibilityEnabled() {
        textViewTitleAllAccessibilityEnabled.visibility = View.VISIBLE
        textViewAllAccessibilityEnabled.visibility = View.VISIBLE
    }

}