package co.omisego.omgshop.network

import co.omisego.omgshop.deserialize.OMGConverterFactory
import co.omisego.omgshop.helpers.Config
import co.omisego.omgshop.network.prototype.ShopAPI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.Objects

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ShopClient(val interceptor: ShopClientInterceptor) {
    private val okHTTPClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(interceptor.logInterceptor)
            .addNetworkInterceptor(interceptor.headerInterceptor)
            .build()
    }

    val client: ShopAPI by lazy {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(OMGConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Config.HOST_URL)
            .client(okHTTPClient)
            .build()

        retrofit.create(ShopAPI::class.java)
    }

    override fun equals(other: Any?) = other is ShopClient && other.interceptor == interceptor
    override fun hashCode() = Objects.hash(interceptor)
}
