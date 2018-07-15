package co.omisego.omgshop.network

import co.omisego.omgshop.helpers.Config
import co.omisego.omisego.OMGAPIClient
import co.omisego.omisego.model.ClientConfiguration
import co.omisego.omisego.network.ewallet.EWalletClient
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Objects

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class OMGClient(
    val authToken: String,
    private val apiKey: String = Config.OMG_API_KEY,
    private val baseURL: String = Config.OMG_HOST_URL
) {
    private val eWalletClient by lazy {
        EWalletClient.Builder {
            clientConfiguration = ClientConfiguration(baseURL, apiKey, authToken)
            debug = true
            debugOkHttpInterceptors = mutableListOf(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                },
                StethoInterceptor()
            )
        }.build()
    }

    val client: OMGAPIClient by lazy {
        OMGAPIClient(eWalletClient)
    }

    override fun equals(other: Any?) = other is OMGClient && other.authToken == authToken
    override fun hashCode() = Objects.hash(authToken)
}
