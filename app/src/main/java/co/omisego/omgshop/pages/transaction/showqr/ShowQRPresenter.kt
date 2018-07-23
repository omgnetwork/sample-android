package co.omisego.omgshop.pages.transaction.showqr

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.readableAmount
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCaller
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionStatus
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

    override fun handleTransactionConsumptionRequest(transactionConsumption: TransactionConsumption) {
        mView?.addPendingConsumption(transactionConsumption)
    }

    override fun handleTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
        val consumerName = transactionConsumption.user?.username?.split("|")?.get(0)
        when (transactionConsumption.status) {
            TransactionConsumptionStatus.REJECTED, TransactionConsumptionStatus.UNKNOWN -> {
                mView?.showTransactionFinalizedFailed(
                    transactionConsumption,
                    "The transaction consumption from $consumerName has been ${transactionConsumption.status.value}"
                )
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
                mView?.showTransactionFinalizedSuccess(transactionConsumption, msg)
            }
        }
    }

    override fun handleTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
        mView?.showTransactionFinalizedFailed(transactionConsumption, apiError.description)
    }

    override fun handleConfirmationFailed(error: OMGResponse<APIError>) {
        mView?.showConfirmationFail(error.data.description)
    }

    override fun handleApproveSuccess(transactionConsumption: OMGResponse<TransactionConsumption>) {
        log("Approved ${transactionConsumption.data.id}")
    }

    override fun handleRejectSuccess(transactionConsumption: OMGResponse<TransactionConsumption>) {
        log("Rejected ${transactionConsumption.data.id}")
    }

    override fun handleOnConnected() {
        mView?.showOnConnected()
    }

    override fun handleOnDisconnected() {
        mView?.showOnDisconnected()
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}