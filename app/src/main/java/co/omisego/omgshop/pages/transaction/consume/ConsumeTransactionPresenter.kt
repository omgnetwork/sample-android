package co.omisego.omgshop.pages.transaction.consume

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCaller
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCallerContract
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionParams
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionStatus
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestType
import co.omisego.omisego.model.transaction.request.toTransactionConsumptionParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ConsumeTransactionPresenter : BasePresenterImpl<ConsumeTransactionContract.View, ConsumeTransactionCallerContract.Caller>(),
    ConsumeTransactionContract.Presenter,
    ConsumeTransactionCallerContract.Handler {
    override var caller: ConsumeTransactionCallerContract.Caller? = ConsumeTransactionCaller(this)

    override fun sanitizeRequestParams(
        transactionRequest: TransactionRequest,
        amount: String,
        token: Token?,
        address: String,
        correlationId: String?
    ): TransactionConsumptionParams {
        return transactionRequest.toTransactionConsumptionParams(
            amount = if (amount.isEmpty()) 0.bd else amount.toBigDecimal().multiply(token?.subunitToUnit ?: 1.bd),
            address = if (address.isEmpty()) null else address,
            tokenId = token?.id ?: null,
            correlationId = if (correlationId == null || correlationId.isEmpty()) null else correlationId
        )
    }

    override fun handleConsumeTransactionFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showConsumeTransactionFailed(error.data)
    }

    override fun handleConsumeTransactionSuccess(response: OMGResponse<TransactionConsumption>) {
        mView?.hideLoading()
        mView?.showConsumeTransactionSuccess(response.data)
        caller?.listenTransactionConsumption(transactionConsumption = response.data)
    }

    override fun handleTransactionConsumptionFinalizedFail(response: TransactionConsumption, error: APIError) {
    }

    override fun handleTransactionConsumptionFinalizedSuccess(response: TransactionConsumption) {
        val consumerName = response.user?.username
        when (response.status) {
            TransactionConsumptionStatus.REJECTED -> {
                mView?.showTransactionFinalizedFailed("The transaction consumption from $consumerName has been rejected")
            }
            TransactionConsumptionStatus.APPROVED, TransactionConsumptionStatus.CONFIRMED -> {
                val isSent = response.transactionRequest.type == TransactionRequestType.SEND
                val direction = if (isSent) "receive" else "sent"
                val amount = response.amount.divide(response.token.subunitToUnit)
                val tokenSymbol = response.token.symbol
                val status = if (response.approvedAt == null) "confirmed" else "approved"
                val msg = String.format(
                    "The transaction consumption has been %s. You have %s %s %s successfully.",
                    status,
                    direction,
                    amount,
                    tokenSymbol
                )
                mView?.showTransactionFinalizedSuccess(msg)
            }
        }
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
