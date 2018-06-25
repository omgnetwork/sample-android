package co.omisego.omgshop.pages.transaction.generate

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCaller
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.request.TransactionRequest

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class GenerateTransactionPresenter : BasePresenterImpl<GenerateTransactionContract.View, GenerateTransactionCallerContract.Caller>(),
    GenerateTransactionContract.Presenter,
    GenerateTransactionCallerContract.Handler {
    override var caller: GenerateTransactionCallerContract.Caller? = GenerateTransactionCaller(this)

    override fun handleGenerateTransactionSuccess(response: OMGResponse<TransactionRequest>) {
        mView?.hideLoading()
        mView?.showCreateTransactionSuccess(response.data)
    }

    override fun handleGenerateTransactionFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showCreateTransactionFailed(error.data)
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
