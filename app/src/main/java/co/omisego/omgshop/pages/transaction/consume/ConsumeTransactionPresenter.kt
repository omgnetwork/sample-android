package co.omisego.omgshop.pages.transaction.consume

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCaller
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ConsumeTransactionPresenter : BasePresenterImpl<ConsumeTransactionContract.View, ConsumeTransactionCallerContract.Caller>(),
    ConsumeTransactionContract.Presenter,
    ConsumeTransactionCallerContract.Handler {
    override var caller: ConsumeTransactionCallerContract.Caller? = ConsumeTransactionCaller(this)

    override fun handleConsumeTransactionFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showConsumeTransactionFailed(error.data)
    }

    override fun handleConsumeTransactionSuccess(response: OMGResponse<TransactionConsumption>) {
        mView?.hideLoading()
        mView?.showConsumeTransactionSuccess(response.data)
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
