package co.omisego.omgshop.pages.checkout

import co.omisego.androidsdk.models.Balance
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omgshop.helpers.Contextor
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.network.OMGApiManager
import java.math.BigDecimal


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class CheckoutPresenter(private val sharePrefsManager: SharePrefsManager) : BasePresenterImpl<CheckoutContract.View>(), CheckoutContract.Presenter {
    private val authToken by lazy {
        sharePrefsManager.loadCredential().omisegoAuthenticationToken
    }

    override fun pay(tokenValue: BigDecimal, productId: String) {
        val tokenId = sharePrefsManager.loadSelectedTokenBalance()?.mintedToken?.id ?: ""

        if (tokenId.isEmpty()) {
            // This should not be possible, since we always set the default token.
            mView?.showBuyFailed(Contextor.context.getString(R.string.activity_checkout_pay_error_token_not_set))
            return
        }

        val request = Product.Buy.Request(tokenId, tokenValue, productId)

        mView?.showLoading()
        // Buy item
        mCompositeSubscription += OMGApiManager.buy(request)
                .subscribe({
                    OMGApiManager.listBalances(authToken, {
                        mView?.hideLoading()
                        mView?.showBuySuccess()
                    }) { response ->
                        // Update current balance to share preference
                        var currentBalance = getCurrentTokenBalance()
                        currentBalance = response.data[0].balances.first { it.mintedToken.id == currentBalance.mintedToken.id }
                        sharePrefsManager.saveSelectedTokenBalance(currentBalance)
                        mView?.hideLoading()
                        mView?.showBuySuccess()
                    }
                }, {
                    val errorDescription = it.errorResponse().data.description
                    mView?.showBuyFailed(errorDescription)
                    mView?.hideLoading()
                })
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
        val symbol = sharePrefsManager.loadSelectedTokenBalance()?.mintedToken?.symbol ?: ""
        mView?.showRedeemButton(symbol)
    }

    override fun getCurrentTokenBalance(): Balance {
        return sharePrefsManager.loadSelectedTokenBalance()!!
    }
}