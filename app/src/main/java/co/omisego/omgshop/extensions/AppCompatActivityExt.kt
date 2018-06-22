package co.omisego.omgshop.extensions

import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 23/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
fun AppCompatActivity.checkPermission(permission: String) =
    ContextCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.shouldShowRequestPermission(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestPermission(permission: String, requestId: Int) =
    ActivityCompat.requestPermissions(this, arrayOf(permission), requestId)
