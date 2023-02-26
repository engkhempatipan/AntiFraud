package com.security.anti.fraud

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.security.anti.fraud.checker.SecurityChecker
import com.security.anti.fraud.databinding.ActivityMainBinding
import com.security.anti.fraud.model.DisplayManagerModel
import com.security.anti.fraud.model.Model

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var checker: SecurityChecker
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checker = SecurityChecker()
    }

    override fun onResume() {
        super.onResume()
        scanUnSecureApps()
    }

    private fun scanUnSecureApps() {
        //hide Ui
        hideDisplay()
        hideUntrustedAccessibility()
        hideAllAccessibilityEnabled()
        //scan
        checker.checkDisplayPresentation(this, object : SecurityChecker.DisplayCheckerCallback {
            override fun onDetected(displayManagerModel: DisplayManagerModel) {
                showDisplaySection()
                binding.textViewDisplay.text = displayManagerModel.customWording
            }
        })

        checker.getUntrustedApp(
            this,
            object : SecurityChecker.AccessibilityUntrustedEnabledListCallback {
                override fun onDetected(models: List<Model>?, customString: String) {
                    showUntrustedAccessibilitySection()
                    binding.textViewAccessibility.text = customString
                }
            })

        checker.getAccessibilityEnabledList(
            this,
            object : SecurityChecker.AccessibilityEnabledListCallback {
                override fun onDetected(models: List<Model>?, customString: String) {
                    showAccessibilityEnabledListSection()
                    binding.textViewAllAccessibility.text = customString
                }
            })
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

    private fun hideUntrustedAccessibility() {
        binding.textViewTitleAccessibility.visibility = View.GONE
        binding.textViewAccessibility.visibility = View.GONE
    }

    private fun showUntrustedAccessibilitySection() {
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

    private fun showAccessibilityEnabledListSection() {
        binding.textViewTitleAllAccessibility.visibility = View.VISIBLE
        binding.textViewAllAccessibility.visibility = View.VISIBLE
    }

}