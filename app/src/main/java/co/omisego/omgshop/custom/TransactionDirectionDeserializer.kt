package co.omisego.omgshop.custom

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 12/3/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.models.TransactionDirection
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class TransactionDirectionDeserializer : JsonDeserializer<TransactionDirection> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): TransactionDirection {
        return TransactionDirection.from(json.asString)
    }
}
