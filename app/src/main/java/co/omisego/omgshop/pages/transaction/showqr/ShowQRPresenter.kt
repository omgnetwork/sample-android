package co.omisego.omgshop.pages.transaction.showqr

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.readableAmount
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCaller
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionStatus
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestType

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ShowQRPresenter : BasePresenterImpl<ShowQRContract.View, ShowQRCallerContract.Caller>(),
    ShowQRContract.Presenter,
    ShowQRCallerContract.Handler {
    override var caller: ShowQRCallerContract.Caller? = ShowQRCaller(this)

    override fun leaveChannel(request: TransactionRequest) {
        caller?.leaveChannel(request = request)
    }

    override fun handleTransactionConsumptionRequest(transactionConsumption: TransactionConsumption) {
        mView?.showIncomingTransactionConsumptionDialog(transactionConsumption)
    }

    override fun handleTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
        val consumerName = transactionConsumption.user?.username
        when (transactionConsumption.status) {
            TransactionConsumptionStatus.REJECTED -> {
                mView?.showTransactionFinalizedFailed("The transaction consumption from $consumerName has been rejected")
            }
            TransactionConsumptionStatus.APPROVED, TransactionConsumptionStatus.CONFIRMED -> {
                val isSent = transactionConsumption.transactionRequest.type == TransactionRequestType.SEND
                val direction = if (isSent) "sent" else "received"
                val amount = transactionConsumption.readableAmount()
                val tokenSymbol = transactionConsumption.token.symbol
                val status = if (transactionConsumption.approvedAt == null) "confirmed" else "approved"
                val msg = String.format(
                    "The transaction consumption from %s has been %s. You have %s %s %s successfully.",
                    consumerName,
                    status,
                    direction,
                    amount,
                    tokenSymbol
                )
                mView?.showTransactionFinalizedSuccess(msg)
            }
        }
    }

    override fun handleTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
        mView?.showTransactionFinalizedFailed(apiError.description)
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}