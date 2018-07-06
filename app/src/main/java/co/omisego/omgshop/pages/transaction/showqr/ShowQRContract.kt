package co.omisego.omgshop.pages.transaction.showqr

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCallerContract
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

interface ShowQRContract {
    interface View : BaseContract.BaseView {
        fun showTransactionFinalizedFailed(transactionConsumption: TransactionConsumption, msg: String)
        fun showTransactionFinalizedSuccess(transactionConsumption: TransactionConsumption, msg: String)
        fun showConfirmationFail(msg: String)
        fun addPendingConsumption(transactionConsumption: TransactionConsumption)
    }

    interface Presenter : BaseContract.BasePresenter<View, ShowQRCallerContract.Caller> {
    }
}