package co.omisego.omgshop.pages.products.caller

import co.omisego.omgshop.base.BaseCaller
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.network.CombinedAPIManager

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class ProductListCaller(
    private val handler: ProductListCallerContract.Handler,
    override val credential: Credential = Preference.loadCredential()
) : BaseCaller(), ProductListCallerContract.Caller {

    override fun loadProductList(credential: Credential) {
        handler.showLoading()
        mCompositeSubscription += CombinedAPIManager.getProductList(credential)
            .map {
                it.data.data.map {
                    it.copy(price = it.price / 100)
                }
            }
            .subscribe(handler::handleLoadProductListSuccess, handler::handleLoadProductListFailed)
    }

    override fun loadWallets(authToken: String) {
        handler.showLoading()
        CombinedAPIManager.getWallets(authToken, handler::handleLoadWalletFailed, handler::handleLoadWalletSuccess)
    }
}
