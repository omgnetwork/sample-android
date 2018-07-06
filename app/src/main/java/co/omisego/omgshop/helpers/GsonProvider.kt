package co.omisego.omgshop.helpers

import co.omisego.omgshop.custom.TransactionDirectionDeserializer
import co.omisego.omgshop.models.TransactionDirection
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 21/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
object GsonProvider {
    fun create(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(TransactionDirection::class.java, TransactionDirectionDeserializer())
            .create()
    }
}