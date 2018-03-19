package co.omisego.omgshop.network

import co.omisego.omgshop.helpers.OMGClientProvider
import co.omisego.omgshop.models.*
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.BalanceList
import co.omisego.omisego.model.Logout
import co.omisego.omisego.model.OMGResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object OMGApiManager {
    inline fun listBalances(authToken: String, crossinline fail: (OMGResponse<APIError>) -> Unit, crossinline success: (OMGResponse<BalanceList>) -> Unit) {
        OMGClientProvider.retrieve(authToken).listBalances().enqueue(object : OMGCallback<BalanceList> {
            override fun fail(response: OMGResponse<APIError>) = fail.invoke(response)
            override fun success(response: OMGResponse<BalanceList>) = success.invoke(response)
        })
    }

    inline fun loadUser(authToken: String, crossinline fail: (OMGResponse<APIError>) -> Unit, crossinline success: (OMGResponse<co.omisego.omisego.model.User>) -> Unit) {
        OMGClientProvider.retrieve(authToken).getCurrentUser().enqueue(object : OMGCallback<co.omisego.omisego.model.User> {
            override fun fail(response: OMGResponse<APIError>) {
                fail.invoke(response)
            }

            override fun success(response: OMGResponse<co.omisego.omisego.model.User>) {
                success.invoke(response)
            }
        })
    }

    inline fun logout(authToken: String, crossinline fail: (OMGResponse<APIError>) -> Unit, crossinline success: (OMGResponse<Logout>) -> Unit) {
        OMGClientProvider.retrieve(authToken).logout().enqueue(object : OMGCallback<Logout> {
            override fun fail(response: OMGResponse<APIError>) {
                fail.invoke(response)
            }

            override fun success(response: OMGResponse<Logout>) {
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