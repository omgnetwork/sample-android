package co.omisego.omgshop.network.prototype

import co.omisego.omgshop.network.OMGClient
import co.omisego.omgshop.network.ShopClient

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface Provider {
    fun provideOMGClient(authToken: String): OMGClient
    fun provideShopClient(userId: String = "", authToken: String = ""): ShopClient
}