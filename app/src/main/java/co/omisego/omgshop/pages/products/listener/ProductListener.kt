package co.omisego.omgshop.pages.products.listener

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

interface ProductListener {
    fun onProductClick(id: String)
}

interface HandleProductListener {
    var listener: ProductListener?
    fun setProductListener(listener: ProductListener)
}
