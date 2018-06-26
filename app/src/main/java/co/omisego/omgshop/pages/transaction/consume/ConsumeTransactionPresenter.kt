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
import co.omisego.omisego.model.transaction.request.TransactionRequest
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
            amount = if (amount.isEmpty()) 0.bd else amount.toBigDecimal(),
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
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
