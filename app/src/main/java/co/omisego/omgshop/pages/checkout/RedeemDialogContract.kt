package co.omisego.omgshop.pages.checkout

import co.omisego.omisego.models.MintedToken
import co.omisego.omgshop.base.BaseContract


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface RedeemDialogContract {
    interface View : BaseContract.BaseView {
        fun setTextRedeemAmount(redeem: String)
        fun setTextDiscount(discount: String)
        fun sendDiscountToCheckoutPage(discount: Int)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun redeemChanged(value: Int, symbol: String)
        fun handleClickRedeem()
    }
}