package co.omisego.omgshop.extensions

import android.util.Log

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
fun Any.logi(msg: Any) {
    Log.i(this.javaClass.simpleName, msg.toString())
}

fun Any.logd(msg: Any) {
    Log.d(this.javaClass.simpleName, msg.toString())
}
