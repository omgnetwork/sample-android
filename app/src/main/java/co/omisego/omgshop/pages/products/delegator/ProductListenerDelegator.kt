package co.omisego.omgshop.pages.products.delegator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.pages.products.listener.ProductListener
import co.omisego.omgshop.pages.products.listener.ProductListenerHolder

class ProductListenerDelegator : ProductListenerHolder {
    override var productListener: ProductListener? = null
}
