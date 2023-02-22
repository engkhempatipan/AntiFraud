package com.security.anti.fraud

import android.os.Bundle
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

    private fun findFraud() {
        getDisplayManagerList()
        getAccessibilityEnabledList()
    }

    private fun getDisplayManagerList() {
        val displayDetectedList = ""
        //TODO FIND LIST
        if (displayDetectedList.isEmpty()) {
            hideDisplay()
        } else {
            textViewDisplayManager.text = displayDetectedList
        }

    }

    private fun getAccessibilityEnabledList() {
        val accessibilityDetectedList = ""
        //TODO FIND LIST
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