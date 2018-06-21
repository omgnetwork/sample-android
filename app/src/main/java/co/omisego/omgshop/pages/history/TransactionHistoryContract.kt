package co.omisego.omgshop.pages.history

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.TransactionRecord
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract


/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface TransactionHistoryContract {
    interface View : BaseContract.BaseView {
        fun showTransactionList(transactionList: List<TransactionRecord>)
        fun showLoadTransactionListFail()
    }

    interface Presenter : BaseContract.BasePresenter<View, TransactionHistoryCallerContract.Caller> {

    }
}