package co.omisego.omgshop.pages.checkout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract

interface RedeemDialogContract {
    interface View : BaseContract.BaseView {
        fun setTextRedeemAmount(redeem: String)
        fun setTextDiscount(discount: String)
        fun sendDiscountToCheckoutPage(discount: Int)
    }

    interface Presenter : BaseContract.BasePresenter<View, BaseContract.BaseCaller> {
        fun redeemChanged(value: Int, symbol: String)
        fun handleClickRedeem()
    }
}