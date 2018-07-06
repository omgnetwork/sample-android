package co.omisego.omgshop.network

import co.omisego.omgshop.helpers.Config
import co.omisego.omisego.model.ClientConfiguration
import co.omisego.omisego.websocket.OMGSocketClient
import java.util.Objects

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 25/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
class SocketClient(
    internal val authToken: String,
    private val apiKey: String = Config.OMG_API_KEY,
    private val baseURL: String = Config.OMG_SOCKET_HOST_URL
) {
    val client by lazy {
        OMGSocketClient.Builder {
            clientConfiguration = ClientConfiguration(baseURL, apiKey, authToken)
            debug = true
        }.build()
    }

    override fun equals(other: Any?) = other is SocketClient && other.authToken == authToken
    override fun hashCode() = Objects.hash(authToken)
}