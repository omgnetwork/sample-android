package co.omisego.omgshop.pages.transaction.consume.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ConsumeTransactionCaller(
    private val handler: ConsumeTransactionCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), ConsumeTransactionCallerContract.Caller {

    override fun consume(authToken: String, request: TransactionConsumptionParams) {
        handler.showLoading()
        CombinedAPIManager.consumeTransactionRequest(
            authToken,
            request,
            handler::handleConsumeTransactionFailed,
            handler::handleConsumeTransactionSuccess
        )
    }
}
