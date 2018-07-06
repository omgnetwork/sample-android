package co.omisego.omgshop.pages.transaction.generate.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface GenerateTransactionCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun generate(authToken: String = credential.omisegoAuthenticationToken, request: TransactionRequestCreateParams)
    }

    interface Handler {
        fun handleGenerateTransactionSuccess(response: OMGResponse<TransactionRequest>)
        fun handleGenerateTransactionFailed(error: OMGResponse<APIError>)
        fun showLoading()
    }
}