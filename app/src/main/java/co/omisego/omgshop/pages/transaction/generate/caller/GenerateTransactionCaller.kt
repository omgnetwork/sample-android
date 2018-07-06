package co.omisego.omgshop.pages.transaction.generate.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class GenerateTransactionCaller(
    private val handler: GenerateTransactionCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), GenerateTransactionCallerContract.Caller {

    override fun generate(authToken: String, request: TransactionRequestCreateParams) {
        handler.showLoading()
        CombinedAPIManager.createTransactionRequest(
            authToken,
            request,
            handler::handleGenerateTransactionFailed,
            handler::handleGenerateTransactionSuccess
        )
    }
}
