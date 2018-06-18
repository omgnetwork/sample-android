package co.omisego.omgshop.network.prototype

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Login
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.models.Register
import co.omisego.omgshop.models.Response
import co.omisego.omgshop.network.ShopEndpoints
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface ShopAPI {
    @POST(ShopEndpoints.SIGN_UP)
    fun signup(@Body request: Register.Request): Observable<Response<Credential>>

    @POST(ShopEndpoints.LOGIN)
    fun login(@Body request: Login.Request): Observable<Response<Credential>>

    @POST(ShopEndpoints.GET_PRODUCT)
    fun getProducts(): Observable<Response<Product.Get.Response>>

    @POST(ShopEndpoints.BUY_PRODUCT)
    fun buy(@Body request: Product.Buy.Request): Observable<Response<Nothing>>
}