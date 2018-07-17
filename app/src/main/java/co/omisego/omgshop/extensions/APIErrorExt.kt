package co.omisego.omgshop.extensions

import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun APIError.isAuthError() = code in arrayOf(
    ErrorCode.CLIENT_INVALID_API_KEY,
    ErrorCode.USER_AUTH_TOKEN_NOT_FOUND,
    ErrorCode.USER_AUTH_TOKEN_EXPIRED
)
