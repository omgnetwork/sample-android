package co.omisego.omgshop.network

import co.omisego.omgshop.helpers.Config
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.Objects

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ShopClientInterceptor(
    val userId: String = "",
    val authToken: String = "",
    private val apiKey: String = Config.API_KEY,
    private val apiKeyId: String = Config.API_KEY_ID
) {
    private val headerManager by lazy { HeaderManager(userId, authToken, apiKey, apiKeyId) }

    val logInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    val headerInterceptor by lazy {
        Interceptor {
            val endpoint = it.request().url().encodedPath().substringAfterLast("/")
            val authorization = headerManager.createAuthorization(endpoint)
            val request: Request = it.request().newBuilder()
                .addHeader("Authorization", authorization)
                .build()
            it.proceed(request)
        }
    }

    override fun equals(other: Any?) =
        other is ShopClientInterceptor && other.userId == userId && other.authToken == authToken

    override fun hashCode() = Objects.hash(userId, authToken)
}