package com.security.anti.fraud.model

import android.accessibilityservice.AccessibilityServiceInfo
import android.content.pm.ServiceInfo


data class Model(
    var packageName: String,
    var appName: String?,
    var installerID: String?,
    var serviceInfo: ServiceInfo,
    var isTrustedApp: Boolean = false
)
