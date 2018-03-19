package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

data class Response<out T>(val version: String, val success: Boolean, val data: T)