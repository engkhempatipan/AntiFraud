package com.security.anti.fraud.checker.callback

import com.security.anti.fraud.model.Model

interface AccessibilityUntrustedEnabledListCallback {
    fun onDetected(models: List<Model>?, customString: String)
}
