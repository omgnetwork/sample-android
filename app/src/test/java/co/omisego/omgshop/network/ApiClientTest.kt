package co.omisego.omgshop.network

import co.omisego.omgshop.helpers.Config
import co.omisego.omgshop.models.Credential
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.models.Response
import co.omisego.omgshop.network.prototype.ShopAPI
import co.omisego.omgshop.testutils.RecordingObserver
import co.omisego.omgshop.testutils.readFile
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.amshove.kluent.mock
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

/**
 * OmiseGO
 *
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

@Suppress("IllegalIdentifier")
class ApiClientTest {
    private lateinit var shopAPI: ShopAPI

    private val observerRule = RecordingObserver.Rule()
    private val mockWebServer: MockWebServer = MockWebServer()

    private lateinit var userFile: File
    private lateinit var registerFile: File
    private lateinit var loginFile: File
    private lateinit var productGetFile: File
    private lateinit var productBuyFile: File
    private lateinit var errorFile: File
    private lateinit var responseRegister: Response<Credential>
    private lateinit var responseLogin: Response<Credential>
    private lateinit var responseProductBuy: Response<Nothing>
    private lateinit var responseProductGet: Response<Product.Get.Response>
    private lateinit var responseError: Response<Error>

    @Before
    fun setUp() {
        Config.HOST_URL = "https://your.api.com/"

        // Retrieves mocked /me.get response from local json file
        userFile = readFile("user.response.success.json")
        userFile shouldNotBe null

        // Retrieves mocked /signup response from local json file
        registerFile = readFile("signup.response.success.json")
        registerFile shouldNotBe null

        // Retrieves mocked /login response from local json file
        loginFile = readFile("login.response.success.json")
        loginFile shouldNotBe null

        // Retrieves mocked /products.get response from local json file
        productGetFile = readFile("products.get.response.success.json")
        productGetFile shouldNotBe null

        // Retrieves mocked /products.buy response from local json file
        productBuyFile = readFile("products.buy.response.success.json")
        productBuyFile shouldNotBe null

        // Retrieves mocked error response from local json file
        errorFile = readFile("response.error.json")
        errorFile shouldNotBe null

        // Convert response using Gson converter

        val typeRegisterToken = object : TypeToken<Response<Credential>>() {}.type
        responseRegister = Gson().fromJson<Response<Credential>>(registerFile.readText(), typeRegisterToken)

        val typeLoginToken = object : TypeToken<Response<Credential>>() {}.type
        responseLogin = Gson().fromJson<Response<Credential>>(loginFile.readText().replace("\n ", "").replace("\n  ", ""), typeLoginToken)

        val typeProductGetToken = object : TypeToken<Response<Product.Get.Response>>() {}.type
        responseProductGet = Gson().fromJson<Response<Product.Get.Response>>(productGetFile.readText(), typeProductGetToken)

        val typeProductBuyToken = object : TypeToken<Response<Nothing>>() {}.type
        responseProductBuy = Gson().fromJson<Response<Nothing>>(productBuyFile.readText(), typeProductBuyToken)

        val typeErrorToken = object : TypeToken<Response<Error>>() {}.type
        responseError = Gson().fromJson<Response<Error>>(errorFile.readText(), typeErrorToken)

        // Setup retrofit with mock server
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(mockWebServer.url("/"))
            .build()

        shopAPI = retrofit.create(ShopAPI::class.java)

        // Disable unused logged from mock webserver
        LogManager.getLogManager().reset()
        Logger.getGlobal().level = Level.OFF
    }

    @Test
    fun `verify sign up api return observable register response correctly`() {
        // Enqueued response from mock server
        mockWebServer.enqueue(MockResponse().setBody(registerFile.readText()))

        // create mock observable
        val observer = observerRule.create<Response<Credential>>()

        // subscribe mocked response to observable
        shopAPI.signup(mock()).subscribe(observer)

        // assert observable value
        observer.assertValue(responseRegister).assertComplete()
    }

    @Test
    fun `verify login api return observable register response correctly`() {

        // Enqueued response from mock server
        mockWebServer.enqueue(MockResponse().setBody(loginFile.readText()))

        // create mock observable
        val observer = observerRule.create<Response<Credential>>()

        // subscribe mocked response to observable
        shopAPI.login(mock()).subscribe(observer)

        // assert observable value
        observer.assertValue(responseLogin).assertComplete()
    }

    @Test
    fun `verify product buy api return observable register response correctly`() {
        // Enqueued response from mock server
        mockWebServer.enqueue(MockResponse().setBody(productBuyFile.readText()))

        // create mock observable
        val observer = observerRule.create<Response<Nothing>>()

        // subscribe mocked response to observable
        shopAPI.buy(mock()).subscribe(observer)

        // assert observable value
        observer.assertValue(responseProductBuy).assertComplete()
    }

    @Test
    fun `verify product get api return observable register response correctly`() {
        // Enqueued response from mock server
        mockWebServer.enqueue(MockResponse().setBody(productGetFile.readText()))

        // create mock observable
        val observer = observerRule.create<Response<Product.Get.Response>>()

        // subscribe mocked response to observable
        shopAPI.getProducts().subscribe(observer)

        // assert observable value
        observer.assertValue(responseProductGet).assertComplete()
    }
}