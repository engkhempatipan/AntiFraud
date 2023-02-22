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

    private lateinit var btnRefresh: AppCompatButton
    private lateinit var textViewTitleAccessibility: TextView
    private lateinit var textViewAccessibility: TextView
    private lateinit var textViewTitleDisplay: TextView
    private lateinit var textViewDisplayManager: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //bindingView
        btnRefresh = findViewById(R.id.btn_refresh)
        textViewTitleAccessibility = findViewById(R.id.text_view_title_accessibility)
        textViewAccessibility = findViewById(R.id.text_view_accessibility)
        textViewTitleDisplay = findViewById(R.id.text_view_title_display)
        textViewDisplayManager = findViewById(R.id.text_view_display)
        //start
        findFraud()
        btnRefresh.setOnClickListener {
            findFraud()
        }
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
                "DeviceProduct info: $deviceProductInfo , \nDisplayName: $name"
            } else {
                "DisplayName: $name"
            }
        } else {
            return ""
        }

    }

    private fun verifyAccessibilityServiceChecker(): String {
        var insecureList = ""
        val accessibilityCheckerModel: AccessibilityCheckerModel =
            verifyInstallerAccessibilityService()
        if (!accessibilityCheckerModel.isVerifyPass) {
            accessibilityCheckerModel.listOfVerifyError.entries

            accessibilityCheckerModel.listOfVerifyError.entries.forEachIndexed { index, item ->
                insecureList += item.value + "\n"
                Log.d("accessibility", " values[$index]: " + item.value)
            }
        }
        return insecureList
    }

    private fun findFraud() {
        getDisplayManagerList()
        getAccessibilityEnabledList()
    }

    private fun getDisplayManagerList() {
        val displayDetectedList = verifyScreenCasting()
        if (displayDetectedList.isEmpty()) {
            hideDisplay()
        } else {
            textViewDisplayManager.text = displayDetectedList
        }
    }

    private fun getAccessibilityEnabledList() {
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

    private fun hideAccessibility() {
        textViewTitleAccessibility.visibility = View.GONE
        textViewAccessibility.visibility = View.GONE
    }

}