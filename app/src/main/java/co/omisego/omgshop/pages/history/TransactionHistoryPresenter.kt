package co.omisego.omgshop.pages.history

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omgshop.pages.history.caller.TransactionHistoryCaller
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.list.Transaction

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionHistoryPresenter() : BasePresenterImpl<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller>(),
        TransactionHistoryContract.Presenter,
        TransactionHistoryCallerContract.Handler {

    override fun handleLoadTransactionListSuccess(response: OMGResponse<PaginationList<Transaction>>) {
        logi(response)
        mView?.hideLoading()
    }

    override fun handleLoadTransactionListFailed(response: OMGResponse<APIError>) {
        logi(response)
        mView?.hideLoading()
    }

    override fun showLoading() {
        mView?.showLoading()
    }

    override var caller: TransactionHistoryCallerContract.Caller? = TransactionHistoryCaller(this)
}
