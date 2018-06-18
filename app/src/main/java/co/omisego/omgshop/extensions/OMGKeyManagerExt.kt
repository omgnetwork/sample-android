package co.omisego.omgshop.extensions

import co.omisego.omisego.security.OMGKeyManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun OMGKeyManager.encrypt(data: String) = this.encrypt(data.toByteArray())
fun OMGKeyManager.decrypt(data: String) = this.decrypt(data.toByteArray())
