package co.omisego.omgshop.pages.checkout.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.models.Response
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface CheckoutCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun buy(request: Product.Buy.Request)
        fun getWallets(authToken: String = credential.authenticationToken)
    }

    interface Handler {
        fun handleBuySuccess(response: Response<Credential>)
        fun handleBuyFailed(error: Throwable)
        fun handleLoadWalletSuccess(response: OMGResponse<WalletList>)
        fun handleLoadWalletFailed(response: OMGResponse<APIError>)
        fun showLoading()
    }
}