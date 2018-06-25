package co.omisego.omgshop.pages.transaction.generate

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.request.TransactionRequest

interface GenerateTransactionContract {
    interface View : BaseContract.BaseView {
        fun showCreateTransactionSuccess(response: TransactionRequest)
        fun showCreateTransactionFailed(response: APIError)
    }

    interface Presenter : BaseContract.BasePresenter<View, GenerateTransactionCallerContract.Caller> {

    }
}