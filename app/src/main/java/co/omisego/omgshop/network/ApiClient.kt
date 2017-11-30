package co.omisego.omgshop.network

import android.util.Base64
import co.omisego.omgshop.BuildConfig
import co.omisego.omgshop.deserialize.OMGConverterFactory
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
    private val BASE_URL = "https://omgshop.omisego.io/api/"
    var omiseGO: OmiseGOAPI

    init {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            addNetworkInterceptor {
                val header = "${BuildConfig.API_KEY_ID}:${BuildConfig.API_KEY}".toByteArray()
                val request: Request = it.request().newBuilder()
                        .addHeader("Authorization", "OMGShop ${String(Base64.encode(header, Base64.NO_WRAP))}").build()
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