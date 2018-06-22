package co.omisego.omgshop.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.deserialize.OMGException
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Response

fun Throwable.errorResponse(): Response<Error> = when {
    this is OMGException -> this.error
    else -> Response("1", false, Error(this.javaClass.name, this.localizedMessage.trim().capitalize()))
}
