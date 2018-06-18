package co.omisego.omgshop.helpers

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.content.SharedPreferences
import co.omisego.omgshop.R
import co.omisego.omgshop.extensions.decryptWith
import co.omisego.omgshop.extensions.encryptWith
import co.omisego.omgshop.extensions.get
import co.omisego.omgshop.extensions.set
import co.omisego.omgshop.helpers.Constants.REQUEST_KEY_AUTHENTICATION_TOKEN
import co.omisego.omgshop.helpers.Constants.REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN
import co.omisego.omgshop.helpers.Constants.REQUEST_KEY_USER_ID
import co.omisego.omgshop.helpers.Constants.SELECTED_TOKEN_BALANCE
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.Balance
import co.omisego.omisego.security.OMGKeyManager
import com.google.gson.Gson

object Preference {
    private val sharePref: SharedPreferences by lazy {
        Contextor.context.getSharedPreferences(Contextor.context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    private val keyManager: OMGKeyManager by lazy {
        OMGKeyManager.Builder {
            keyAlias = "omg"
            iv = "omg:12345678"
        }.build(Contextor.context)
    }

    fun saveCredential(credential: Credential) {
        sharePref[REQUEST_KEY_USER_ID] = credential.userId encryptWith keyManager
        sharePref[REQUEST_KEY_AUTHENTICATION_TOKEN] = credential.authenticationToken encryptWith keyManager
        sharePref[REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN] = credential.omisegoAuthenticationToken encryptWith keyManager
    }

    fun loadCredential(): Credential {
        return try {
            val userId = sharePref[REQUEST_KEY_USER_ID] decryptWith keyManager
            val authToken = sharePref[REQUEST_KEY_AUTHENTICATION_TOKEN] decryptWith keyManager
            val omiseGOAuthToken = sharePref[REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN] decryptWith keyManager

            Credential(userId, authToken, omiseGOAuthToken)
        } catch (e: Exception) {
            Credential("", "", "")
        }
    }

    fun saveSelectedTokenBalance(balance: Balance?) {
        sharePref[SELECTED_TOKEN_BALANCE] = if (balance != null) {
            val gson = Gson()
            gson.toJson(balance)
        } else {
            ""
        }
    }

    fun loadSelectedTokenBalance(): Balance? {
        val gson = Gson()
        val tokenBalanceJson = sharePref[SELECTED_TOKEN_BALANCE]
        return if (tokenBalanceJson.isNotEmpty()) {
            gson.fromJson(tokenBalanceJson, Balance::class.java)
        } else {
            null
        }
    }
}