package co.omisego.omgshop.deserialize

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Response
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class OMGResponseBodyConverter<T>(private val gson: Gson, private val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {
    override fun convert(value: ResponseBody): T {
        val responseData = value.string()
        val json = JSONObject(responseData)
        val version = json.getString("version")
        val success = json.getBoolean("success")
        val data = json.getJSONObject("data")
        val reader = InputStreamReader(ByteArrayInputStream(responseData.toByteArray()))

        if (!success) {
            val errorResponse = Error(data.getString("code"), data.getString("description"))
            throw OMGException(Response(version, false, errorResponse))
        }

        val jsonReader = gson.newJsonReader(reader)
        jsonReader.use { _ ->
            return adapter.read(jsonReader)
        }
    }
}
