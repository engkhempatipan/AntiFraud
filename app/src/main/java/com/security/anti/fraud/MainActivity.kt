package com.security.anti.fraud

import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.security.anti.fraud.databinding.ActivityMainBinding
import com.security.anti.fraud.model.Model

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        showAccessibilitySection()
        showDisplaySection()
        showAllAccessibilityEnabledSection()
        //scan
        getDisplayManagerList()
        getUntrustedSourceList()
        getAllAccessibilityEnabledList()
    }

    private fun getAllAccessibilityEnabledList() {
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
        binding.textViewAllAccessibility.text = textAllAccessibilityEnabled
    }

    private fun getUntrustedSourceList() {
        val untrustedList = findAllAccessibilityEnabledInfo().filter { !it.isTrustedApp }
        if (untrustedList.isEmpty()) {
            hideAccessibility()
        }

        var untrustedListString = ""
        if (untrustedList.isNotEmpty()) {
            untrustedList.forEachIndexed { _: Int, item: Model ->
                untrustedListString += "Package name = [" + item.packageName + "]\n" +
                        "App name =[${item.appName} ]\n" +
                        "Installer id =[${item.installerID}]\n\n"
            }
        }

        binding.textViewAccessibility.text = untrustedListString
    }


    private fun getDisplayManagerList() {
        val displayDetectedList = verifyScreenCasting()
        if (displayDetectedList.isEmpty()) {
            hideDisplay()
        } else {
            binding.textViewDisplay.text = displayDetectedList
        }
    }
    private fun hideDisplay() {
        binding.textViewTitleDisplay.visibility = View.GONE
        binding.textViewDisplay.visibility = View.GONE
    }
    private fun showDisplaySection() {
        binding.textViewTitleDisplay.visibility = View.VISIBLE
        binding.textViewDisplay.visibility = View.VISIBLE
        binding.textViewDisplay.setOnClickListener {
            startActivity(Intent(Settings.ACTION_CAST_SETTINGS))
        }
    }

    private fun hideAccessibility() {
        binding.textViewTitleAccessibility.visibility = View.GONE
        binding.textViewAccessibility.visibility = View.GONE
    }

    private fun showAccessibilitySection() {
        binding.textViewTitleAccessibility.visibility = View.VISIBLE
        binding.textViewAccessibility.visibility = View.VISIBLE
        binding.textViewAccessibility.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }

    private fun hideAllAccessibilityEnabled() {
        binding.textViewTitleAllAccessibility.visibility = View.GONE
        binding.textViewAllAccessibility.visibility = View.GONE
    }

    private fun showAllAccessibilityEnabledSection() {
        binding.textViewTitleAllAccessibility.visibility = View.VISIBLE
        binding.textViewAllAccessibility.visibility = View.VISIBLE
    }

}