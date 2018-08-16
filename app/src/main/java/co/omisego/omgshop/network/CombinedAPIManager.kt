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
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionActionParams
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionParams
import co.omisego.omisego.model.transaction.list.TransactionListParams
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams
import co.omisego.omisego.operation.startListeningEvents
import co.omisego.omisego.websocket.listener.SocketConnectionListener
import co.omisego.omisego.websocket.listener.TransactionConsumptionListener
import co.omisego.omisego.websocket.listener.TransactionRequestListener
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.net.ssl.SSLException

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

    inline fun getTransactions(
        authToken: String,
        request: TransactionListParams,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<PaginationList<Transaction>>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.getTransactions(request)
            .enqueue(object : OMGCallback<PaginationList<Transaction>> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<PaginationList<Transaction>>) {
                    success.invoke(response)
                }
            })
    }

    inline fun createTransactionRequest(
        authToken: String,
        request: TransactionRequestCreateParams,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<TransactionRequest>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.createTransactionRequest(request)
            .enqueue(object : OMGCallback<TransactionRequest> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<TransactionRequest>) {
                    success.invoke(response)
                }
            })
    }

    inline fun consumeTransactionRequest(
        authToken: String,
        request: TransactionConsumptionParams,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<TransactionConsumption>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.consumeTransactionRequest(request)
            .enqueue(object : OMGCallback<TransactionConsumption> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<TransactionConsumption>) {
                    success.invoke(response)
                }
            })
    }

    inline fun approve(
        authToken: String,
        request: TransactionConsumptionActionParams,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<TransactionConsumption>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.approveTransactionConsumption(request)
            .enqueue(object : OMGCallback<TransactionConsumption> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<TransactionConsumption>) {
                    success.invoke(response)
                }
            })
    }

    inline fun reject(
        authToken: String,
        request: TransactionConsumptionActionParams,
        crossinline fail: (OMGResponse<APIError>) -> Unit,
        crossinline success: (OMGResponse<TransactionConsumption>) -> Unit
    ) {
        ClientProvider.provideOMGClient(authToken).client.rejectTransactionConsumption(request)
            .enqueue(object : OMGCallback<TransactionConsumption> {
                override fun fail(response: OMGResponse<APIError>) {
                    fail.invoke(response)
                }

                override fun success(response: OMGResponse<TransactionConsumption>) {
                    success.invoke(response)
                }
            })
    }

    inline fun listenSocketConnection(
        authToken: String,
        crossinline onConnected: () -> Unit,
        crossinline onDisconnected: () -> Unit
    ) {
        val client = ClientProvider.provideSocketClient(authToken).client
        client.addConnectionListener(object : SocketConnectionListener {
            override fun onConnected() {
                onConnected()
            }

            override fun onDisconnected(throwable: Throwable?) {
                if (throwable is SSLException) {
                    onDisconnected()
                }
            }
        })
    }

    inline fun listenTransactionRequest(
        authToken: String,
        request: TransactionRequest,
        crossinline onRequest: (TransactionConsumption) -> Unit,
        crossinline onFinalizedFail: (TransactionConsumption, APIError) -> Unit,
        crossinline onFinalizedSuccess: (TransactionConsumption) -> Unit
    ) {
        val client = ClientProvider.provideSocketClient(authToken).client
        request.startListeningEvents(client, listener = object : TransactionRequestListener() {
            override fun onTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) =
                onFinalizedFail.invoke(transactionConsumption, apiError)

            override fun onTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) =
                onFinalizedSuccess(transactionConsumption)

            override fun onTransactionConsumptionRequest(transactionConsumption: TransactionConsumption) =
                onRequest(transactionConsumption)
        })
    }

    inline fun listenTransactionConsumption(
        authToken: String,
        request: TransactionConsumption,
        crossinline onFinalizedFail: (TransactionConsumption, APIError) -> Unit,
        crossinline onFinalizedSuccess: (TransactionConsumption) -> Unit
    ) {
        val client = ClientProvider.provideSocketClient(authToken).client
        request.startListeningEvents(client, listener = object : TransactionConsumptionListener() {
            override fun onTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) =
                onFinalizedFail.invoke(transactionConsumption, apiError)

            override fun onTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) =
                onFinalizedSuccess(transactionConsumption)
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