package co.omisego.omgshop.pages.transaction.dialog

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.transaction.dialog.caller.TransactionConfirmCallerContract

interface TransactionConfirmContract {
    interface View : BaseContract.BaseView {
        fun showRejectSuccess(msg: String)
        fun showApproveSuccess(msg: String)
        fun showConfirmationFail(msg: String)
    }

    interface Presenter : BaseContract.BasePresenter<View, TransactionConfirmCallerContract.Caller> {
    }
}