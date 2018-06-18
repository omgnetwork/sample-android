package co.omisego.omgshop.extensions

import android.content.SharedPreferences

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
operator fun SharedPreferences.set(key: String, value: String) {
    this.edit().putString(key, value).apply()
}

operator fun SharedPreferences.get(key: String) = this.getString(key, "")
