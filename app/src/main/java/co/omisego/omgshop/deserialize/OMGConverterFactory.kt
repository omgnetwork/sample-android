package co.omisego.omgshop.deserialize

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class OMGConverterFactory(val gson: Gson) : Converter.Factory() {
    override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
        val adapter: TypeAdapter<*> = gson.getAdapter(TypeToken.get(type))
        return OMGResponseBodyConverter(gson, adapter)
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>, methodAnnotations: Array<out Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        return GsonConverterFactory.create(gson).requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    companion object {
        fun create(): OMGConverterFactory = OMGConverterFactory(
            GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        )
    }
}