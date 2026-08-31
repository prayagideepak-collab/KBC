package com.example.util

import android.app.UiModeManager
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration

object DeviceCapabilities {
    @Suppress("DEPRECATION")
    fun isTv(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        if (uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true
        }
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_TELEVISION) ||
               pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK)
    }

    @Suppress("DEPRECATION")
    fun hasCamera(context: Context): Boolean {
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
               pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) ||
               pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
    }

    fun hasMicrophone(context: Context): Boolean {
        val pm = context.packageManager
        return pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)
    }
}
