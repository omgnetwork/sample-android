package co.omisego.omgshop.network

import android.util.Base64
import co.omisego.omgshop.BuildConfig
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Login


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class HeaderManager {
    fun createAuthorization(endpoint: String, credential: Credential): String {
        val header = when (endpoint) {
            Endpoints.PROFILE, Endpoints.BUY_PRODUCT -> {
                "${BuildConfig.API_KEY_ID}:${BuildConfig.API_KEY}:${credential.userId}:${credential.authenticationToken}".toByteArray()
            }
            else -> "${BuildConfig.API_KEY_ID}:${BuildConfig.API_KEY}".toByteArray()
        }

        return "OMGShop ${String(Base64.encode(header, Base64.NO_WRAP))}"
    }
}