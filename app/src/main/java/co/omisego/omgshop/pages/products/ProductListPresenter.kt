package co.omisego.omgshop.pages.products

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.network.CombinedAPIManager
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.WalletList
import io.reactivex.Observable

/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ProductListPresenter : BasePresenterImpl<ProductListContract.View>(), ProductListContract.Presenter {
    private var productList: List<Product.Get.Item>? = null

    override fun fetchProductList() {
        mCompositeSubscription += getProductListObservable()
            .subscribe(this::fetchProductListSuccess, this::fetchProductListFailed)
    }

    private fun getProductListObservable(): Observable<List<Product.Get.Item>> {
        return CombinedAPIManager.getProductList(Preference.loadCredential())
            .doOnSubscribe { mView?.showLoading() }
            .map {
                it.data.data.map {
                    it.copy(price = it.price / 100)
                }
            }
    }

    private fun fetchProductListFailed(throwable: Throwable) {
        mView?.hideLoading()
        mView?.showMessage(throwable.errorResponse().data.description)
        mView?.showLoadProductFail(throwable.errorResponse().data)
        goBackToLoginIfNeeded(throwable.errorResponse().data)
    }

    private fun fetchProductListSuccess(response: List<Product.Get.Item>) {
        productList = response
        if (Preference.loadSelectedTokenBalance() != null) {
            mView?.showProductList(response)
            mView?.hideLoading()
            return
        }
        fetchWalletList()
    }

    private fun fetchWalletList() {
        val (_, _, authToken) = Preference.loadCredential()
        CombinedAPIManager.getWallets(authToken, this::fetchWalletFailed, this::fetchWalletSuccess)
    }

    private fun fetchWalletSuccess(response: OMGResponse<WalletList>) {
        Preference.saveSelectedTokenBalance(response.data.data[0].balances[0])
        mView?.showProductList(productList ?: return)
        mView?.hideLoading()
    }

    private fun fetchWalletFailed(error: OMGResponse<APIError>) {
        mView?.showMessage(error.data.description)
        mView?.hideLoading()
        goBackToLoginIfNeeded(error.data)
    }

    override fun handleClickProductItem(itemId: String) {
        productList?.let {
            mView?.showClickProductItem(productList!!.first { it.id == itemId })
        }
    }
}
