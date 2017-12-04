package co.omisego.omgshop.pages.products

import co.omisego.omgshop.base.BasePresenterImpl
import co.omisego.omgshop.extensions.errorResponse
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

class ProductListPresenter() : BasePresenterImpl<ProductListContract.View>(), ProductListContract.Presenter {
    private var productList: List<Product.Get.Item>? = null

    override fun loadProductList() {
        mCompositeSubscription += ApiClient.omiseGO.getProducts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    productList = it.data.data
                    mView?.showProductList(it.data)
                    log(it.toString())
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