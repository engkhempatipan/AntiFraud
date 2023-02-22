package com.security.anti.fraud

data class AccessibilityCheckerModel(
    val isVerifyPass: Boolean,
    val listOfVerifyError: Map<String, String>,
)