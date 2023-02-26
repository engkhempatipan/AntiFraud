package com.security.anti.fraud

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.security.anti.fraud.databinding.ActivityMainBinding

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
        //show Ui
        showDisplaySection()
        showUntrustedAccessibilitySection()
        showAccessibilityEnabledListSection()
        //scan
        getPresentationDisplay()
        getUntrustedList()
        getAccessibilityEnabledList()
    }

    private fun getAccessibilityEnabledList() {
        val listEnabled = checker.getAccessibilityEnabledList(this)
        if (listEnabled == null) {
            hideAllAccessibilityEnabled()
        } else {
            binding.textViewAllAccessibility.text = listEnabled
        }
    }

    private fun getUntrustedList() {
        val untrustedList = checker.getUntrustedApp(this)
        if (untrustedList == null) {
            hideUntrustedAccessibility()
        } else {
            binding.textViewAccessibility.text = untrustedList
        }

    }

    private fun getPresentationDisplay() {
        val displayDetectedList = checker.checkDisplayPresentation(this)
        if (displayDetectedList == null) {
            hideDisplay()
        } else {
            binding.textViewDisplay.text = displayDetectedList.customWording
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