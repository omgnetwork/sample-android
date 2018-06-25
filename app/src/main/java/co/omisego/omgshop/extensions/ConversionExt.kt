package co.omisego.omgshop.extensions

import android.content.res.Resources

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 24/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
val Int.dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()