package co.omisego.omgshop.pages.products

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.products.caller.ProductListCaller
import co.omisego.omgshop.pages.products.caller.ProductListCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ProductListPresenter : BasePresenterImpl<ProductListContract.View, ProductListCallerContract.Caller>(),
    ProductListContract.Presenter,
    ProductListCallerContract.Handler {
    private var productList: List<Product.Get.Item>? = null
    override var caller: ProductListCallerContract.Caller? = ProductListCaller(this)

    override fun handleLoadProductListSuccess(response: List<Product.Get.Item>) {
        mView?.hideLoading()
        productList = response
        if (Preference.loadSelectedTokenBalance() != null) {
            mView?.showProductList(response)
            return
        }
        caller?.loadWallets()
    }

    override fun handleLoadProductListFailed(throwable: Throwable) {
        mView?.hideLoading()
        mView?.showMessage(throwable.errorResponse().data.description)
        mView?.showLoadProductFail(throwable.errorResponse().data)
        goBackToLoginIfNeeded(throwable.errorResponse().data)
    }

    override fun handleLoadWalletSuccess(response: OMGResponse<WalletList>) {
        mView?.hideLoading()
        Preference.saveSelectedTokenBalance(response.data.data[0].balances[0])
        mView?.showProductList(productList ?: return)
    }

    override fun handleLoadWalletFailed(error: OMGResponse<APIError>) {
        mView?.hideLoading()
        mView?.showMessage(error.data.description)
        goBackToLoginIfNeeded(error.data)
    }

    override fun handleClickProductItem(itemId: String) {
        productList?.let {
            mView?.showClickProductItem(productList!!.first { it.id == itemId })
        }
    }

    override fun showLoading() {
        mView?.showLoading()
    }
}
