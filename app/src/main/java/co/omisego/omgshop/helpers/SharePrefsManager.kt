package co.omisego.omgshop.helpers

import android.content.Context
import android.content.SharedPreferences
import co.omisego.androidsdk.models.Balance
import co.omisego.androidsdk.security.OMGKeyManager
import co.omisego.omgshop.R
import co.omisego.omgshop.helpers.Constants.REQUEST_KEY_AUTHENTICATION_TOKEN
import co.omisego.omgshop.helpers.Constants.REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN
import co.omisego.omgshop.helpers.Constants.REQUEST_KEY_USER_ID
import co.omisego.omgshop.helpers.Contants.SELECTED_TOKEN_BALANCE
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.models.Register
import com.google.gson.Gson


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class SharePrefsManager(private val context: Context) {
    private val sharePref: SharedPreferences by lazy {
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    }
    private val keyManager: OMGKeyManager by lazy {
        OMGKeyManager.Builder {
            initialize(context, context.getString(R.string.app_name))
            setIV(context.getString(R.string.app_name) + "12345")
        }.build()
    }

    fun saveLoginResponse(loginResponse: Login.Response): Boolean {
        val authenticationToken = keyManager.encrypt(context, loginResponse.authenticationToken.toByteArray())
        val userId = keyManager.encrypt(context, loginResponse.userId.toByteArray())
        val omisegoAuthenticationToken = keyManager.encrypt(context, loginResponse.omisegoAuthenticationToken.toByteArray())

        return with(sharePref.edit()) {
            putString(REQUEST_KEY_AUTHENTICATION_TOKEN, authenticationToken)
            putString(REQUEST_KEY_USER_ID, userId)
            putString(REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN, omisegoAuthenticationToken)
        }.commit()
    }

    fun saveRegisterResponse(registerResponse: Register.Response): Boolean {
        val authenticationToken = keyManager.encrypt(context, registerResponse.authenticationToken.toByteArray())
        val userId = keyManager.encrypt(context, registerResponse.userId.toByteArray())
        val omisegoAuthenticationToken = keyManager.encrypt(context, registerResponse.omisegoAuthenticationToken.toByteArray())

        return with(sharePref.edit()) {
            putString(REQUEST_KEY_AUTHENTICATION_TOKEN, authenticationToken)
            putString(REQUEST_KEY_USER_ID, userId)
            putString(REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN, omisegoAuthenticationToken)
        }.commit()
    }

    fun readLoginResponse(): Login.Response {
        with(sharePref) {
            val userId = getString(REQUEST_KEY_USER_ID, "")
            val authToken = getString(REQUEST_KEY_AUTHENTICATION_TOKEN, "")
            val omiseGoAuthToken = getString(REQUEST_KEY_OMISE_GO_AUTHENTICATION_TOKEN, "")

            return if (userId.isNotEmpty()) {
                Login.Response(
                        keyManager.decrypt(context, userId.toByteArray()),
                        keyManager.decrypt(context, authToken.toByteArray()),
                        keyManager.decrypt(context, omiseGoAuthToken.toByteArray())
                )
            } else {
                Login.Response("", "", "")
            }
        }
    }

    fun saveSelectedTokenBalance(balance: Balance?) {
        if (balance != null) {
            val gson = Gson()
            val tokenBalanceJson = gson.toJson(balance)
            with(sharePref.edit()) {
                putString(SELECTED_TOKEN_BALANCE, tokenBalanceJson)
            }.apply()
        } else {
            with(sharePref.edit()) {
                putString(SELECTED_TOKEN_BALANCE, "")
            }.apply()
        }
    }

    fun loadSelectedTokenBalance(): Balance? {
        val gson = Gson()
        val tokenBalanceJson = sharePref.getString(SELECTED_TOKEN_BALANCE, "")
        return if (tokenBalanceJson.isNotEmpty()) {
            gson.fromJson(tokenBalanceJson, Balance::class.java)
        } else {
            null
        }
    }
}