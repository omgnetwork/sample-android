package co.omisego.omgshop.extensions

import co.omisego.omisego.constant.enums.ErrorCode
import co.omisego.omisego.model.APIError

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
fun APIError.isAuthError() = this.code == ErrorCode.CLIENT_INVALID_API_KEY || this.code == ErrorCode.USER_ACCESS_TOKEN_NOT_FOUND
