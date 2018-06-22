package co.omisego.omgshop.pages.checkout.caller

import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Contextor
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.network.CombinedAPIManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class CheckoutCaller(
    private val handler: CheckoutCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), CheckoutCallerContract.Caller {

    override fun buy(request: Product.Buy.Request) {

        if (request.tokenId.isEmpty()) {
            val error = Throwable(Contextor.context.getString(R.string.activity_checkout_pay_error_token_not_set))
            handler.handleBuyFailed(error)
        }

        mCompositeSubscription += CombinedAPIManager.buy(credential, request)
            .doOnSubscribe { handler.showLoading() }
            .subscribe(handler::handleBuySuccess, handler::handleBuyFailed)
    }

    override fun getWallets(authToken: String) {
        handler.showLoading()
        CombinedAPIManager.getWallets(authToken, handler::handleLoadWalletFailed, handler::handleLoadWalletSuccess)
    }
}
