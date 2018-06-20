package co.omisego.omgshop.pages.products.caller

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Product
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 19/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface ProductListCallerContract {
    interface Caller : BaseContract.BaseCaller {
        val credential: Credential
        fun loadProductList(credential: Credential = this.credential)
        fun loadWallets(authToken: String = this.credential.omisegoAuthenticationToken)
    }

    interface Handler {
        fun handleLoadProductListSuccess(response: List<Product.Get.Item>)
        fun handleLoadProductListFailed(throwable: Throwable)
        fun handleLoadWalletSuccess(response: OMGResponse<WalletList>)
        fun handleLoadWalletFailed(error: OMGResponse<APIError>)
        fun showLoading()
    }
}