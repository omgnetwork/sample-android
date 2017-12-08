package co.omisego.omgshop.pages.products

import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Product


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

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