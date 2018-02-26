package co.omisego.omgshop.helpers

import android.content.Context
import co.omisego.omgshop.R
import java.util.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/2/2018 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object Config {
    lateinit var OMG_HOST_URL: String
    lateinit var HOST_URL: String
    lateinit var API_KEY: String
    lateinit var API_KEY_ID: String
    lateinit var OMG_API_KEY: String
    lateinit var AUTHENTICATION_SCHEME: String


    fun init(context: Context) {
        val resource = context.resources
        val configRes = resource.openRawResource(R.raw.config)
        val properties = Properties().apply { load(configRes) }
        with(properties) {
            OMG_HOST_URL = getProperty("omisego_host_url")
            HOST_URL = getProperty("host_url")
            API_KEY = getProperty("api_key")
            API_KEY_ID = getProperty("api_key_id")
            OMG_API_KEY = getProperty("omisego_api_key")
            AUTHENTICATION_SCHEME = getProperty("authentication_scheme")
        }
    }
}
