package co.omisego.omgshop.network

import co.omisego.omgshop.deserialize.OMGConverterFactory
import co.omisego.omgshop.helpers.Contextor
import co.omisego.omgshop.helpers.SharePrefsManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object ApiClient {
    private val BASE_URL = Endpoints.BASE_URL
    private val headerManager by lazy { HeaderManager() }
    var omiseGO: OmiseGOAPI

    init {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            retryOnConnectionFailure(true) // actually, it's auto-retry by default, just add it to be clear.
            addNetworkInterceptor {
                val endpoint = it.request().url().encodedPath().substringAfterLast("/")
                val credential = SharePrefsManager(Contextor.context).loadCredential()
                val authorization = headerManager.createAuthorization(endpoint, credential)
                val request: Request = it.request().newBuilder()
                        .addHeader("Authorization", authorization)
                        .build()
                it.proceed(request)
            }
        }

        val retrofit = Retrofit.Builder()
                .addConverterFactory(OMGConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .build()

        omiseGO = retrofit.create(OmiseGOAPI::class.java)
    }
}