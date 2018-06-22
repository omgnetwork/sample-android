package co.omisego.omgshop.pages.history

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omisego.model.transaction.list.Transaction
import co.omisego.omisego.model.transaction.list.TransactionListParams


/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface TransactionHistoryContract {
    interface View : BaseContract.BaseView {
        fun addTransactions(transactionList: List<Transaction>, page: Int)
        fun showLoadTransactionListFail()
        fun showCurrentAddress(address: String)
    }

    interface Presenter : BaseContract.BasePresenter<View, TransactionHistoryCallerContract.Caller> {
        fun loadCurrentAddress()
        fun createTransactionListParams(page: Int) : TransactionListParams
    }
}