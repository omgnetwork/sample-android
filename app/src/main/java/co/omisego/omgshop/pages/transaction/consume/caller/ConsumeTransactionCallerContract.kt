package co.omisego.omgshop.pages.transaction.consume.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface ConsumeTransactionCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun consume(authToken: String = credential.omisegoAuthenticationToken, request: TransactionConsumptionParams)
    }

    interface Handler {
        fun handleConsumeTransactionSuccess(response: OMGResponse<TransactionConsumption>)
        fun handleConsumeTransactionFailed(error: OMGResponse<APIError>)
        fun showLoading()
    }
}