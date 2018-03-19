package co.omisego.omgshop.pages.checkout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Product
import co.omisego.omisego.model.Balance
import java.math.BigDecimal

interface CheckoutContract {
    interface View : BaseContract.BaseView {
        fun showProductDetail(imageUrl: String, productTitle: String, productPrice: String)
        fun showRedeemDialog()
        fun showSummary(subTotal: String, discount: String, total: String)
        fun showRedeemButton(tokenSymbol: String)
        fun showBuySuccess()
        fun showBuyFailed(msg: String = "")
        fun setDiscount(discount: Int)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun pay(tokenValue: BigDecimal, productId: String)
        fun redeem()
        fun calculateTotal(subTotal: Double, discount: Double)
        fun handleProductDetail(productItem: Product.Get.Item)
        fun resolveRedeemButtonName()
        fun getCurrentTokenBalance(): Balance
    }
}