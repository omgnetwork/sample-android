package co.omisego.omgshop.pages.products

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.OMGClientProvider
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.network.ApiClient
import co.omisego.omisego.custom.OMGCallback
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.BalanceList
import co.omisego.omisego.model.OMGResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class ProductListPresenter(private val sharePrefsManager: SharePrefsManager) : BasePresenterImpl<ProductListContract.View>(), ProductListContract.Presenter {
    private var productList: List<Product.Get.Item>? = null
    private val authToken: String by lazy {
        sharePrefsManager.loadCredential().omisegoAuthenticationToken
    }

    override fun loadProductList() {
        mCompositeSubscription += ApiClient.omiseGO.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { mView?.showLoading() }
                .doOnError { mView?.hideLoading() }
                .subscribe({
                    val balance = sharePrefsManager.loadSelectedTokenBalance()
                    productList = it.data.data.map {
                        it.copy(price = it.price / 100)
                    }

                    if (balance != null) {
                        mView?.showProductList(productList!!)
                        mView?.hideLoading()
                        return@subscribe
                    }

                    OMGClientProvider.retrieve(authToken).listBalances().enqueue(object : OMGCallback<BalanceList> {
                        override fun fail(response: OMGResponse<APIError>) {
                            mView?.showMessage(response.data.description)
                            mView?.hideLoading()
                        }

                        override fun success(response: OMGResponse<BalanceList>) {
                            sharePrefsManager.saveSelectedTokenBalance(response.data.data[0].balances[0])
                            mView?.showProductList(productList!!)
                            mView?.hideLoading()

                        }
                    })

                }, {
                    mView?.showMessage(it.errorResponse().data.description)
                    mView?.showLoadProductFail(it.errorResponse().data)
                })
    }

    override fun handleClickProductItem(itemId: String) {
        productList?.let {
            mView?.showClickProductItem(productList!!.first { it.id == itemId })
        }
    }
}