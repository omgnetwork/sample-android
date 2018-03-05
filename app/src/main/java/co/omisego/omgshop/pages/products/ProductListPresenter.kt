package co.omisego.omgshop.pages.products

import android.util.Log
import co.omisego.omisego.Callback
import co.omisego.omisego.models.Address
import co.omisego.omisego.models.ApiError
import co.omisego.omisego.models.Response
import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
import co.omisego.omgshop.helpers.OMGClientProvider
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.network.ApiClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
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

                    OMGClientProvider.retrieve(authToken).listBalances(object : Callback<List<Address>> {
                        override fun fail(response: Response<ApiError>) {
                            mView?.showMessage(response.data.description)
                            mView?.hideLoading()
                        }

                        override fun success(response: Response<List<Address>>) {
                            sharePrefsManager.saveSelectedTokenBalance(response.data[0].balances[0])
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