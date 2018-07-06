package co.omisego.omgshop.network

import co.omisego.omgshop.network.prototype.Provider

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 18/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
object ClientProvider : Provider {
    private var omgClient: OMGClient? = null
    private var shopClient: ShopClient? = null
    private var socketClient: SocketClient? = null

    override fun provideOMGClient(authToken: String): OMGClient {
        val client = omgClient

        if (client != null && client.authToken == authToken) {
            return client
        }

        return OMGClient(authToken).also {
            omgClient = it
        }
    }

    override fun provideSocketClient(authToken: String): SocketClient {
        val client = socketClient

        if (client != null && client.authToken == authToken) {
            return client
        }

        return SocketClient(authToken).also {
            socketClient = it
        }
    }

    override fun provideShopClient(userId: String, authToken: String): ShopClient {
        val client = shopClient
        val interceptor = ShopClientInterceptor(userId, authToken)

        if (client == null || client.interceptor != interceptor) {
            return ShopClient(interceptor).also {
                shopClient = it
            }
        }
        return client
    }
}
