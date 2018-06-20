package co.omisego.omgshop.pages.checkout

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.models.Response
import co.omisego.omgshop.pages.checkout.caller.CheckoutCaller
import co.omisego.omgshop.pages.checkout.caller.CheckoutCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class CheckoutPresenter : BasePresenterImpl<CheckoutContract.View, CheckoutCallerContract.Caller>(),
    CheckoutContract.Presenter,
    CheckoutCallerContract.Handler {

    override var caller: CheckoutCallerContract.Caller? = CheckoutCaller(this)

    override fun handleBuySuccess(response: Response<Credential>) {
        mView?.hideLoading()
        caller?.getWallets()
    }

    override fun handleBuyFailed(error: Throwable) {
        mView?.hideLoading()
        val errorDescription = error.errorResponse().data.description
        mView?.showBuyFailed(errorDescription)
    }

    override fun handleLoadWalletSuccess(response: OMGResponse<WalletList>) {
        mView?.hideLoading()
        // Update current balance to share preference
        var currentBalance = getCurrentTokenBalance()
        currentBalance = response.data.data[0].balances.first { it.token.id == currentBalance.token.id }
        Preference.saveSelectedTokenBalance(currentBalance)
        mView?.showBuySuccess()
    }

    override fun handleLoadWalletFailed(response: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showBuySuccess()
    }

    override fun showLoading() {
        mView?.showLoading()
    }

    override fun redeem() {
        mView?.showRedeemDialog()
    }

    override fun calculateTotal(subTotal: Double, discount: Double) {
        val total = (subTotal - discount).thousandSeparator()
        mView?.showSummary(subTotal.thousandSeparator(), discount.thousandSeparator(), total)
        mView?.setDiscount(discount.toInt())
    }

    override fun handleProductDetail(productItem: Product.Get.Item) {
        mView?.showProductDetail(productItem.imageUrl, productItem.name, "฿${productItem.price.toDouble().thousandSeparator()}")
    }

    override fun resolveRedeemButtonName() {
        val symbol = Preference.loadSelectedTokenBalance()?.token?.symbol ?: ""
        mView?.showRedeemButton(symbol)
    }

    override fun getCurrentTokenBalance(): Balance {
        return Preference.loadSelectedTokenBalance()!!
    }
}