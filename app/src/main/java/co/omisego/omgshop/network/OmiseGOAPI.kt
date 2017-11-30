package co.omisego.omgshop.network

import co.omisego.omgshop.models.*
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

interface OmiseGOAPI {
    @POST("signup")
    fun signup(@Body request: Register.Request): Observable<Response<Register.Response>>

    @POST("login")
    fun login(@Body request: Login.Request): Observable<Response<Login.Response>>

    @POST("me.get")
    fun getUser(): Observable<Response<User.Response>>

    @POST("products.get")
    fun getProducts(): Observable<Response<Product.Get.Response>>

    @POST("products.buy")
    fun buy(@Body request: Observable<Response<Product.Buy.Request>>): Observable<Response<Nothing>>
}