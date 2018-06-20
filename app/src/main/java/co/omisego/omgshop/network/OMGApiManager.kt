package co.omisego.omgshop.network

import co.omisego.omgshop.helpers.OMGClientProvider
import co.omisego.omgshop.models.*
import co.omisego.omisego.Callback
import co.omisego.omisego.models.Address
import co.omisego.omisego.models.ApiError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import co.omisego.omisego.models.Response as SDKResponse

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object OMGApiManager {
    inline fun listBalances(authToken: String, crossinline fail: (SDKResponse<ApiError>) -> Unit, crossinline success: (SDKResponse<List<Address>>) -> Unit) {
        OMGClientProvider.retrieve(authToken).listBalances(object : Callback<List<Address>> {
            override fun fail(response: SDKResponse<ApiError>) {
                fail.invoke(response)
            }

            override fun success(response: SDKResponse<List<Address>>) {
                success.invoke(response)
            }
        })
    }

    inline fun loadUser(authToken: String, crossinline fail: (SDKResponse<ApiError>) -> Unit, crossinline success: (SDKResponse<co.omisego.omisego.models.User>) -> Unit) {
        OMGClientProvider.retrieve(authToken).getCurrentUser(object : Callback<co.omisego.omisego.models.User> {
            override fun fail(response: SDKResponse<ApiError>) {
                fail.invoke(response)
            }

            override fun success(response: SDKResponse<co.omisego.omisego.models.User>) {
                success.invoke(response)
            }
        })
    }


    inline fun logout(authToken: String, crossinline fail: (SDKResponse<ApiError>) -> Unit, crossinline success: (SDKResponse<String>) -> Unit) {
        OMGClientProvider.retrieve(authToken).logout(object : Callback<String> {
            override fun fail(response: SDKResponse<ApiError>) {
                fail.invoke(response)
            }

            override fun success(response: SDKResponse<String>) {
                success.invoke(response)
                OMGClientProvider.clearToken()
            }
        })
    }

    fun buy(request: Product.Buy.Request): Observable<Response<Nothing>> {
        return ApiClient.omiseGO.buy(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun login(request: Login.Request): Observable<Response<Credential>> {
        return ApiClient.omiseGO.login(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun register(request: Register.Request): Observable<Response<Credential>> {
        return ApiClient.omiseGO.signup(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}