package co.omisego.omgshop.pages.checkout

import co.omisego.omgshop.R
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omgshop.helpers.Contextor
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.models.Response
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Balance
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList
import java.math.BigDecimal

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class CheckoutPresenter : BasePresenterImpl<CheckoutContract.View>(), CheckoutContract.Presenter {
    private val authToken by lazy {
        Preference.loadCredential().omisegoAuthenticationToken
    }

    override fun pay(tokenValue: BigDecimal, productId: String) {
        val tokenId = Preference.loadSelectedTokenBalance()?.token?.id ?: ""

        if (tokenId.isEmpty()) {
            // This should not be possible, since we always set the default token.
            mView?.showBuyFailed(Contextor.context.getString(R.string.activity_checkout_pay_error_token_not_set))
            return
        }

        val request = Product.Buy.Request(tokenId, tokenValue, productId)

        mView?.showLoading()

        // Buy item
        mCompositeSubscription += CombinedAPIManager
            .buy(Preference.loadCredential(), request)
            .subscribe(this::paySuccess, this::payFailed)
    }

    private fun paySuccess(response: Response<Nothing>) {
        CombinedAPIManager.getWallets(authToken, this::updateWalletFailed, this::updateWalletSuccess)
    }

    private fun payFailed(error: Throwable) {
        val errorDescription = error.errorResponse().data.description
        mView?.showBuyFailed(errorDescription)
        mView?.hideLoading()
    }

    private fun updateWalletSuccess(response: OMGResponse<WalletList>) {
        // Update current balance to share preference
        var currentBalance = getCurrentTokenBalance()
        currentBalance = response.data.data[0].balances.first { it.token.id == currentBalance.token.id }
        Preference.saveSelectedTokenBalance(currentBalance)
        mView?.hideLoading()
        mView?.showBuySuccess()
    }

    private fun updateWalletFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showBuySuccess()
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