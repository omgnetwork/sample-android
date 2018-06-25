package co.omisego.omgshop.pages.transaction.consume

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

interface ConsumeTransactionContract {
    interface View : BaseContract.BaseView {
        fun showConsumeTransactionSuccess(response: TransactionConsumption)
        fun showConsumeTransactionFailed(response: APIError)
    }

    interface Presenter : BaseContract.BasePresenter<View, ConsumeTransactionCallerContract.Caller> {

    }
}