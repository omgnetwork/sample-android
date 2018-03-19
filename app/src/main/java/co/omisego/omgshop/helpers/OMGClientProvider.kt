package co.omisego.omgshop.helpers

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.util.Base64
import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.network.ewallet.EWalletClient

object OMGClientProvider {
    private var omgApiClient: OMGAPIClient? = null

    fun retrieve(authToken: String): OMGAPIClient {
        return if (omgApiClient == null) {
            val apiKey = Config.OMG_API_KEY
            val authHeader = Base64.encodeToString("$apiKey:$authToken".toByteArray(), Base64.NO_WRAP)

            val eWalletClient = EWalletClient.Builder {
                authenticationToken = authHeader
                baseUrl = Config.OMG_HOST_URL
                debug = true
            }.build()

            omgApiClient = OMGAPIClient(eWalletClient)
            omgApiClient!!
        } else {
            omgApiClient!!
        }
    }

    fun clearToken() {
        omgApiClient = null
    }
}