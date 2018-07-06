package co.omisego.omgshop.pages.transaction.consume

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionParams
import co.omisego.omisego.model.transaction.request.TransactionRequest

interface ConsumeTransactionContract {
    interface View : BaseContract.BaseView {
        fun showConsumeTransactionSuccess(response: TransactionConsumption)
        fun showConsumeTransactionFailed(response: APIError)
        fun showTransactionFinalizedFailed(msg: String)
        fun showTransactionFinalizedSuccess(msg: String)
        fun setEnableBtnConsume(enable: Boolean)
    }

    interface Presenter : BaseContract.BasePresenter<View, ConsumeTransactionCallerContract.Caller> {
        fun sanitizeRequestParams(
            transactionRequest: TransactionRequest,
            amount: String,
            token: Token?,
            address: String,
            correlationId: String?
        ): TransactionConsumptionParams
    }
}