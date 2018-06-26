package co.omisego.omgshop.pages.transaction.generate

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams

interface GenerateTransactionContract {
    interface View : BaseContract.BaseView {
        fun showCreateTransactionSuccess(response: TransactionRequest)
        fun showCreateTransactionFailed(response: APIError)
    }

    interface Presenter : BaseContract.BasePresenter<View, GenerateTransactionCallerContract.Caller> {
        fun sanitizeTransactionRequestCreateParams(
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
        ): TransactionRequestCreateParams
    }
}