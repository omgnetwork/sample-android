package co.omisego.omgshop.pages.qrcode

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import co.omisego.omgshop.extensions.checkPermission
import co.omisego.omgshop.extensions.shouldShowRequestPermission

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object CameraPermission {
    const val REQUEST_PERMISSION_CAMERA_ID: Int = 100

    inline fun handle(
        activity: AppCompatActivity,
        showRationale: () -> Unit,
        onPermissionGranted: () -> Unit,
        onPermissionManuallyGranted: () -> Unit
    ): Boolean {
        if (activity.checkPermission(Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, ask for the permission here.
            if (activity.shouldShowRequestPermission(Manifest.permission.CAMERA)) {
                // Need explanation because the user just deny and doesn't select `don't show again`
                showRationale()
            } else {
                // No explanation needed because the user selected `don't show again`
                onPermissionManuallyGranted()
            }
            return false
        } else {
            // Permission is already granted
            onPermissionGranted()
            return true
        }
    }
}
