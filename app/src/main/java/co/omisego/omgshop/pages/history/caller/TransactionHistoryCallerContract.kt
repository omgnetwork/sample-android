package co.omisego.omgshop.pages.checkout.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.list.TransactionListParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface TransactionHistoryCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun loadTransactionList(request: TransactionListParams)
    }

    interface Handler {
        fun handleLoadTransactionListSuccess(response: OMGResponse<PaginationList<Transaction>>)
        fun handleLoadTransactionListFailed(response: OMGResponse<APIError>)
        fun showLoading()
    }
}