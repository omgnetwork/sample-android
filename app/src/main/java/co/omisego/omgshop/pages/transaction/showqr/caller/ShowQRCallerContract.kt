package co.omisego.omgshop.pages.transaction.showqr.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
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
        fun approve(
            authToken: String = credential.omisegoAuthenticationToken,
            id: String
        )

        fun reject(
            authToken: String = credential.omisegoAuthenticationToken,
            id: String
        )

        fun listenSocketConnection(authToken: String = credential.omisegoAuthenticationToken)
    }

    interface Handler {
        fun handleConfirmationFailed(error: OMGResponse<APIError>)
        fun handleApproveSuccess(transactionConsumption: OMGResponse<TransactionConsumption>)
        fun handleRejectSuccess(transactionConsumption: OMGResponse<TransactionConsumption>)
        fun handleTransactionConsumptionRequest(transactionConsumption: TransactionConsumption)
        fun handleTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption)
        fun handleTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError)
        fun handleOnConnected()
        fun handleOnDisconnected()
        fun showLoading()
    }
}