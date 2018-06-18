package co.omisego.omgshop.network

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.util.Base64
import co.omisego.omgshop.helpers.Config

class HeaderManager(
    val userId: String,
    val authToken: String,
    private val apiKey: String = Config.API_KEY,
    private val apiKeyId: String = Config.API_KEY_ID
) {
    fun createAuthorization(endpoint: String): String {
        val header = when (endpoint) {
            ShopEndpoints.PROFILE, ShopEndpoints.BUY_PRODUCT -> "$apiKeyId:$apiKey:$userId:$authToken".toByteArray()
            else -> "$apiKeyId:$apiKey".toByteArray()
        }

        return "${Config.AUTHENTICATION_SCHEME} ${String(Base64.encode(header, Base64.NO_WRAP))}"
    }
}
