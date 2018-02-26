package co.omisego.omgshop.network

import android.util.Base64
import co.omisego.omgshop.helpers.Config
import co.omisego.omgshop.models.Credential


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
                "${Config.API_KEY_ID}:${Config.API_KEY}:${credential.userId}:${credential.authenticationToken}".toByteArray()
            }
            else -> "${Config.API_KEY_ID}:${Config.API_KEY}".toByteArray()
        }

        return "${Config.AUTHENTICATION_SCHEME} ${String(Base64.encode(header, Base64.NO_WRAP))}"
    }
}