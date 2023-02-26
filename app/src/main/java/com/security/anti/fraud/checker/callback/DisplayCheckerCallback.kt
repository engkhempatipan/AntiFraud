package com.security.anti.fraud.checker.callback

import com.security.anti.fraud.model.DisplayManagerModel

interface DisplayCheckerCallback {
    fun onDetected(displayManagerModel: DisplayManagerModel)
}