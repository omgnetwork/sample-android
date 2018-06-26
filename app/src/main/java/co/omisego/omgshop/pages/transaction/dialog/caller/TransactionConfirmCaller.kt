package co.omisego.omgshop.pages.transaction.dialog.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionActionParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionConfirmCaller (
    private val handler: TransactionConfirmCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), TransactionConfirmCallerContract.Caller {
    override fun approve(authToken: String, id: String) {
        CombinedAPIManager.approve(
            authToken,
            TransactionConsumptionActionParams(id),
            handler::handleConfirmationFailed,
            handler::handleApproveSuccess
        )
    }

    override fun reject(authToken: String, id: String) {
        CombinedAPIManager.reject(
            authToken,
            TransactionConsumptionActionParams(id),
            handler::handleConfirmationFailed,
            handler::handleRejectSuccess
        )
    }
}