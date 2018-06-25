package co.omisego.omgshop.pages.transaction.showqr

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCaller
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ShowQRPresenter : BasePresenterImpl<ShowQRContract.View, ShowQRCallerContract.Caller>(),
    ShowQRContract.Presenter,
    ShowQRCallerContract.Handler {
    override var caller: ShowQRCallerContract.Caller? = ShowQRCaller(this)

    override fun leaveChannel(request: TransactionRequest) {
        caller?.leaveChannel(request = request)
    }

    override fun handleTransactionConsumptionRequest(transactionConsumption: TransactionConsumption) {
        mView?.showTransactionConfirmation(transactionConsumption)
    }

    override fun handleTransactionConsumptionFinalizedSuccess(transactionConsumption: TransactionConsumption) {
        mView?.showTransactionConfirmation(transactionConsumption)
    }

    override fun handleTransactionConsumptionFinalizedFail(transactionConsumption: TransactionConsumption, apiError: APIError) {
        mView?.showTransactionConfirmation(transactionConsumption)
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}