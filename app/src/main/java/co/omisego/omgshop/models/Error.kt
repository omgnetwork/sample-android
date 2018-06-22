package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

data class Error(val code: String, val description: String) {
    fun isAuthError() = code == "client:invalid_api_key" || code == "user:access_token_not_found"
}
