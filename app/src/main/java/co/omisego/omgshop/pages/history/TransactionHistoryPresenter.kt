package co.omisego.omgshop.pages.history

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omgshop.pages.history.caller.TransactionHistoryCaller
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.list.Transaction
import co.omisego.omisego.model.transaction.list.TransactionListParams

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionHistoryPresenter : BasePresenterImpl<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller>(),
    TransactionHistoryContract.Presenter,
    TransactionHistoryCallerContract.Handler {

    override fun createTransactionListParams(page: Int): TransactionListParams {
        return TransactionListParams.create(
            page = page,
            perPage = PaginationConfig.PER_PAGE,
            sortBy = PaginationConfig.SORT_BY,
            sortDirection = PaginationConfig.SORT_DIR,
            searchTerm = null
        )
    }

    override fun loadCurrentAddress() {
        mView?.showCurrentAddress(Preference.loadWalletAddress())
    }

    override fun handleLoadTransactionListSuccess(response: OMGResponse<PaginationList<Transaction>>) {
        logi(response)
        mView?.hideLoading()
        val pagination = response.data.pagination
        mView?.addTransactions(response.data.data, pagination.currentPage, pagination.isLastPage)
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
