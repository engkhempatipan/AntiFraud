package com.security.anti.fraud.checker

import android.app.Activity
import com.security.anti.fraud.checker.callback.AccessibilityEnabledListCallback
import com.security.anti.fraud.checker.callback.AccessibilityUntrustedEnabledListCallback
import com.security.anti.fraud.checker.callback.DisplayCheckerCallback

interface SecurityCheckerInterface {
    fun checkDisplayPresentation(
        activity: Activity,
        callback: DisplayCheckerCallback
    )

    fun getAccessibilityEnabledList(
        activity: Activity,
        callback: AccessibilityEnabledListCallback
    )

    fun getUntrustedApp(
        activity: Activity,
        callback: AccessibilityUntrustedEnabledListCallback
    )
}