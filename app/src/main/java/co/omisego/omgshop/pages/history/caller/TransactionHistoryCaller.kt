package co.omisego.omgshop.pages.history.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omisego.model.transaction.list.TransactionListParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionHistoryCaller(
        private val handler: TransactionHistoryCallerContract.Handler,
        override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), TransactionHistoryCallerContract.Caller {
    override fun loadTransactionList(request: TransactionListParams) {
        handler.showLoading()
        CombinedAPIManager.getTransactions(
                credential.omisegoAuthenticationToken,
                request,
                handler::handleLoadTransactionListFailed,
                handler::handleLoadTransactionListSuccess)
    }
}
