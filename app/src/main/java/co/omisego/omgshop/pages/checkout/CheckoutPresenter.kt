package co.omisego.omgshop.pages.checkout

import android.util.Base64
import co.omisego.androidsdk.Callback
import co.omisego.androidsdk.OMGApiClient
import co.omisego.androidsdk.models.Address
import co.omisego.androidsdk.models.ApiError
import co.omisego.androidsdk.models.Balance
import co.omisego.androidsdk.models.Response
import co.omisego.omgshop.BuildConfig
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omgshop.helpers.Contextor
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class CheckoutPresenter(private val sharePrefsManager: SharePrefsManager) : BasePresenterImpl<CheckoutContract.View>(), CheckoutContract.Presenter {
    private val omgApiClient by lazy {
        val apiKey = BuildConfig.KUBERA_API_KEY
        val authToken = sharePrefsManager.readLoginResponse().omisegoAuthenticationToken
        val apiClientHeader = "OMGClient ${Base64.encodeToString("$apiKey:$authToken".toByteArray(), Base64.NO_WRAP)}"
        OMGApiClient.Builder {
            setAuthorizationToken(apiClientHeader)
        }.build()
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
        mCompositeSubscription += ApiClient.omiseGO.buy(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    log(it.toString())

                    // Refresh remain balance
                    omgApiClient.listBalances(object : Callback<List<Address>> {
                        override fun fail(response: Response<ApiError>) {
                            log(response.data.toString())
                            mView?.showMessage(response.data.description)
                            mView?.hideLoading()
                            mView?.showBuySuccess()
                        }

                        override fun success(response: Response<List<Address>>) {
                            // Update current balance to share preference
                            var currentBalance = getCurrentTokenBalance()
                            currentBalance = response.data[0].balances.first { it.mintedToken.id == currentBalance.mintedToken.id }
                            sharePrefsManager.saveSelectedTokenBalance(currentBalance)
                            mView?.hideLoading()
                            mView?.showBuySuccess()
                        }
                    })

                }, {
                    val errorDescription = it.errorResponse().data.description
                    log(errorDescription)
                    mView?.showBuyFailed(errorDescription)
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