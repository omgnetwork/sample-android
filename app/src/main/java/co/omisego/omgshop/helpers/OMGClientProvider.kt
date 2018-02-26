package co.omisego.omgshop.helpers

import android.util.Base64
import co.omisego.androidsdk.OMGApiClient


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object OMGClientProvider {
    private var omgApiClient: OMGApiClient? = null

    fun retrieve(authToken: String): OMGApiClient {
        return if (omgApiClient == null) {
            val apiKey = Config.OMG_API_KEY
            val apiClientHeader = "OMGClient ${Base64.encodeToString("$apiKey:$authToken".toByteArray(), Base64.NO_WRAP)}"
            omgApiClient = OMGApiClient.Builder {
                setAuthorizationToken(apiClientHeader)
                setBaseURL(Config.OMG_HOST_URL)
            }.build()
            omgApiClient!!
        } else {
            omgApiClient!!
        }
    }

    fun clearToken() {
        omgApiClient = null
    }
}