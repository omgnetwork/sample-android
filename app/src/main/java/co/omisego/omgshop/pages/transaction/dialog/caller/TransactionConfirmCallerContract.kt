package co.omisego.omgshop.pages.transaction.dialog.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface TransactionConfirmCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun approve(
            authToken: String = credential.omisegoAuthenticationToken,
            id: String
        )

        fun reject(
            authToken: String = credential.omisegoAuthenticationToken,
            id: String
        )
    }

    interface Handler {
        fun handleConfirmationFailed(error: OMGResponse<APIError>)
        fun handleApproveSuccess(transactionConsumption: OMGResponse<TransactionConsumption>)
        fun handleRejectSuccess(transactionConsumption: OMGResponse<TransactionConsumption>)
        fun showLoading()
    }
}