package co.omisego.omgshop.pages.checkout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.checkout.caller.CheckoutCallerContract
import co.omisego.omisego.model.Balance
import java.math.BigDecimal

interface CheckoutContract {
    interface View : BaseContract.BaseView {
        fun showProductDetail(imageUrl: String, productTitle: String, productPrice: String)
        fun showRedeemDialog()
        fun showSummary(subTotal: String, discount: String, total: String)
        fun showTokenRedeemButtonNotAvailable()
        fun showTokenRedeemButtonText(tokenSymbol: String)
        fun showBuySuccess()
        fun showBuyFailed(msg: String = "")
        fun setDiscount(discount: BigDecimal)
    }

    interface Presenter : BaseContract.BasePresenter<View, CheckoutCallerContract.Caller> {
        fun redeem()
        fun createBuyRequestParams(discount: BigDecimal, productId: String): Product.Buy.Request
        fun calculateTotalAmountToPay(subTotal: BigDecimal, discount: BigDecimal)
        fun prepareProductToShow(productItem: Product.Get.Item)
        fun resolveRedeemButtonName()
        fun getCurrentTokenBalance(): Balance
        fun checkIfBalanceAvailable()
    }
}