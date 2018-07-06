package co.omisego.omgshop.pages.transaction.showqr.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface ShowQRCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun joinChannel(authToken: String = credential.omisegoAuthenticationToken, request: TransactionRequest)
        fun leaveChannel(authToken: String = credential.omisegoAuthenticationToken, request: TransactionRequest)
    }

    interface Handler {
        fun handleTransactionConsumptionRequest(transactionConsumption: TransactionConsumption)
        fun handleTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption)
        fun handleTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError)
        fun showLoading()
    }
}