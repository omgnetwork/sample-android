package co.omisego.omgshop.pages.checkout

import co.omisego.omgshop.base.BasePresenterImpl

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class RedeemDialogPresenter : BasePresenterImpl<RedeemDialogContract.View>(), RedeemDialogContract.Presenter {
    private var mRedeemValue: Int = 0

    override fun handleClickRedeem() {
        mView?.sendDiscountToCheckoutPage(mRedeemValue)
    }

    override fun redeemChanged(value: Int, symbol: String) {
        mRedeemValue = value
        mView?.setTextDiscount("$value")
        mView?.setTextRedeemAmount("$value")
    }
}
