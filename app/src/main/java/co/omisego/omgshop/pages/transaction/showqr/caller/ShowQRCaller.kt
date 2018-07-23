package co.omisego.omgshop.pages.transaction.showqr.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.ClientProvider
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionActionParams
import co.omisego.omisego.model.transaction.request.TransactionRequest

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ShowQRCaller(
    private val handler: ShowQRCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), ShowQRCallerContract.Caller {

    override fun listenSocketConnection(authToken: String) {
        CombinedAPIManager.listenSocketConnection(
            authToken,
            handler::handleOnConnected,
            handler::handleOnDisconnected
        )
    }

    override fun joinChannel(authToken: String, request: TransactionRequest) {
        CombinedAPIManager.listenTransactionRequest(
            authToken,
            request,
            handler::handleTransactionConsumptionRequest,
            handler::handleTransactionConsumptionFinalizedFail,
            handler::handleTransactionConsumptionFinalizedSuccess
        )
    }

    override fun leaveChannel(authToken: String, request: TransactionRequest) {
        val client = ClientProvider.provideSocketClient(authToken).client
        request.stopListening(client)
    }

    override fun approve(authToken: String, id: String) {
        CombinedAPIManager.approve(
            authToken,
            TransactionConsumptionActionParams(id),
            handler::handleConfirmationFailed,
            handler::handleApproveSuccess
        )
    }

    override fun reject(authToken: String, id: String) {
        CombinedAPIManager.reject(
            authToken,
            TransactionConsumptionActionParams(id),
            handler::handleConfirmationFailed,
            handler::handleRejectSuccess
        )
    }
}
