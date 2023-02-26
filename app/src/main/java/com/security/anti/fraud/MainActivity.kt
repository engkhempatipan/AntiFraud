package com.security.anti.fraud

import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.security.anti.fraud.model.Model

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

    private fun scanUnSecureApps() {
        //show Ui
        showAccessibility()
        showDisplay()
        showAllAccessibilityEnabled()
        //scan
        getDisplayManagerList()
        getListOfUntrustedSource()
        getAllAccessibilityEnabledListModel()
    }
    private fun getAllAccessibilityEnabledListModel() {
        val listEnabled = findAllAccessibilityEnabledInfo()
        if (listEnabled.isEmpty()) {
            hideAllAccessibilityEnabled()
        }
        var textAllAccessibilityEnabled = ""
        listEnabled.forEachIndexed { _, item ->
            textAllAccessibilityEnabled += "Package name = [" + item.packageName + "]\n" +
                    "App name =[${item.appName} ]\n" +
                    "Installer id =[${item.installerID}]\n\n"
        }
        textViewAllAccessibilityEnabled.text = textAllAccessibilityEnabled
    }

    private fun getListOfUntrustedSource() {
        val listEnabled = findAllAccessibilityEnabledInfo()
        if (listEnabled.isEmpty()) {
            hideAccessibility()
        }

        val untrustedList = listEnabled.filter { !it.isTrustedApp }
        var untrustedListString = ""
        if(untrustedList.isNotEmpty()){
            untrustedList.forEachIndexed { _: Int, item: Model ->
                untrustedListString += "Package name = [" + item.packageName + "]\n" +
                        "App name =[${item.appName} ]\n" +
                        "Installer id =[${item.installerID}]\n\n"
            }
        }

        textViewAccessibility.text = untrustedListString
    }


    private fun getDisplayManagerList() {
        val displayDetectedList = verifyScreenCasting()
        if (displayDetectedList.isEmpty()) {
            hideDisplay()
        } else {
            textViewDisplayManager.text = displayDetectedList
        }
    }

    private fun hideDisplay() {
        textViewTitleDisplay.visibility = View.GONE
        textViewDisplayManager.visibility = View.GONE
    }

    private fun showDisplay() {
        textViewTitleDisplay.visibility = View.VISIBLE
        textViewDisplayManager.visibility = View.VISIBLE
        textViewDisplayManager.setOnClickListener {
            startActivity(Intent(Settings.ACTION_CAST_SETTINGS))
        }
    }

    private fun hideAccessibility() {
        textViewTitleAccessibility.visibility = View.GONE
        textViewAccessibility.visibility = View.GONE
    }

    private fun showAccessibility() {
        textViewTitleAccessibility.visibility = View.VISIBLE
        textViewAccessibility.visibility = View.VISIBLE
        textViewAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
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