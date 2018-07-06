package co.omisego.omgshop.pages.transaction.dialog

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.pages.transaction.dialog.caller.TransactionConfirmCaller
import co.omisego.omgshop.pages.transaction.dialog.caller.TransactionConfirmCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class TransactionConfirmPresenter : BasePresenterImpl<TransactionConfirmContract.View, TransactionConfirmCallerContract.Caller>(),
    TransactionConfirmCallerContract.Handler,
    TransactionConfirmContract.Presenter {
    override var caller: TransactionConfirmCallerContract.Caller? = TransactionConfirmCaller(this)

    override fun handleConfirmationFailed(error: OMGResponse<APIError>) {
        mView?.showConfirmationFail(error.data.description)
    }

    override fun handleApproveSuccess(transactionConsumption: OMGResponse<TransactionConsumption>) {
        mView?.showApproveSuccess("Successfully approved.")
    }

    override fun handleRejectSuccess(transactionConsumption: OMGResponse<TransactionConsumption>) {
        mView?.showRejectSuccess("Successfully rejected.")
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
