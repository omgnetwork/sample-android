package co.omisego.omgshop.pages.transaction.generate

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCaller
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams
import co.omisego.omisego.model.transaction.request.TransactionRequestType

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class GenerateTransactionPresenter : BasePresenterImpl<GenerateTransactionContract.View, GenerateTransactionCallerContract.Caller>(),
    GenerateTransactionContract.Presenter,
    GenerateTransactionCallerContract.Handler {
    override var caller: GenerateTransactionCallerContract.Caller? = GenerateTransactionCaller(this)

    override fun sanitizeTransactionRequestCreateParams(
        token: Token,
        isSend: Boolean,
        amount: String?,
        requiredConfirmation: Boolean,
        allowAmountOverride: Boolean,
        maxConsumption: Int?,
        maxConsumptionPerUser: Int?,
        address: String?,
        consumptionTime: String?,
        correlationId: String?
    ): TransactionRequestCreateParams {
        val type = if (isSend) TransactionRequestType.SEND else TransactionRequestType.RECEIVE
        val maxConsump = if (maxConsumption == 0) null else maxConsumption
        val maxConsumpPerUser = if (maxConsumptionPerUser == 0) null else maxConsumptionPerUser
        return TransactionRequestCreateParams(
            tokenId = token.id,
            type = type,
            amount = if (amount.isNullOrEmpty()) null else amount?.toBigDecimal()?.multiply(token.subunitToUnit),
            requireConfirmation = requiredConfirmation,
            allowAmountOverride = allowAmountOverride,
            maxConsumptions = maxConsump,
            maxConsumptionsPerUser = maxConsumpPerUser,
            address = if (address?.isEmpty() == true) null else address,
            consumptionLifetime = if (consumptionTime?.isEmpty() == true) null else consumptionTime?.toInt(),
            correlationId = correlationId

        )
    }

    override fun handleGenerateTransactionSuccess(response: OMGResponse<TransactionRequest>) {
        mView?.hideLoading()
        mView?.showCreateTransactionSuccess(response.data)
    }

    override fun handleGenerateTransactionFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showCreateTransactionFailed(error.data)
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
