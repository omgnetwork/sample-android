package co.omisego.omgshop.pages.products

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Product

interface ProductListContract {
    interface View : BaseContract.BaseView {
        fun showProductList(items: List<Product.Get.Item>)
        fun showLoadProductFail(response: Error)
        fun showClickProductItem(item: Product.Get.Item)
    }

    interface Presenter : BaseContract.BasePresenter<View> {
        fun loadProductList()
        fun handleClickProductItem(itemId: String)
    }
}