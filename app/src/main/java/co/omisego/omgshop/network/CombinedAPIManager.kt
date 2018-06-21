package co.omisego.omgshop.network

import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.models.Response
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Logout
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 14/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object CombinedAPIManager {
    inline fun getWallets(
        authToken: String,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<WalletList>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.getWallets()
            .enqueue(object : OMGCallback<WalletList> {
                override fun fail(response: OMGResponse<APIError>) = fail.invoke(response)
                override fun success(response: OMGResponse<WalletList>) = success.invoke(response)
            })
    }

    inline fun loadUser(
        authToken: String,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<co.omisego.omisego.model.User>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.getCurrentUser()
            .enqueue(object : OMGCallback<co.omisego.omisego.model.User> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<co.omisego.omisego.model.User>) {
                    success.invoke(response)
                }
            })
    }

    inline fun logout(
        authToken: String,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<Logout>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.logout()
            .enqueue(object : OMGCallback<Logout> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<Logout>) {
                    success.invoke(response)
                }
            })
    }

    fun getProductList(credential: Credential): Observable<Response<Product.Get.Response>> {
        val (userId, authenticationToken) = credential
        return ClientProvider.provideShopClient(userId, authenticationToken).client
            .getProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun buy(credential: Credential, request: Product.Buy.Request): Observable<Response<Nothing>> {
        return ClientProvider.provideShopClient(credential.userId, credential.authenticationToken).client
            .buy(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun login(request: Login.Request): Observable<Response<Credential>> {
        return ClientProvider.provideShopClient().client
            .login(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun register(request: Register.Request): Observable<Response<Credential>> {
        return ClientProvider.provideShopClient().client
            .signup(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}