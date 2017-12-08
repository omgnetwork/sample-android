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
    @POST(Endpoints.SIGN_UP)
    fun signup(@Body request: Register.Request): Observable<Response<Register.Response>>

    @POST(Endpoints.LOGIN)
    fun login(@Body request: Login.Request): Observable<Response<Login.Response>>

    @POST(Endpoints.PROFILE)
    fun getUser(): Observable<Response<User.Response>>

    @POST(Endpoints.GET_PRODUCT)
    fun getProducts(): Observable<Response<Product.Get.Response>>

    @POST(Endpoints.BUY_PRODUCT)
    fun buy(@Body request: Product.Buy.Request): Observable<Response<Nothing>>
}