package co.omisego.omgshop.models

import android.content.Context
import co.omisego.androidsdk.models.Response
import co.omisego.omgshop.helpers.Config
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.amshove.kluent.mock
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldNotBe
import org.junit.Before
import org.junit.Test
import java.io.File

/**
 * OmiseGO
 *
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

@Suppress("IllegalIdentifier")
class ResponseTest {
    private lateinit var responseUser: Response<User.Response>
    private lateinit var responseError: Response<Error>
    private lateinit var userFile: File
    private lateinit var errorFile: File

    @Before
    fun setUp() {
        val resourceUserURL = javaClass.classLoader.getResource("user.response.success.json")
        userFile = File(resourceUserURL.path)
        userFile shouldNotBe null

        val resourceErrorURL = javaClass.classLoader.getResource("response.error.json")
        errorFile = File(resourceErrorURL.path)
        errorFile shouldNotBe null

        val typeUserToken = object : TypeToken<Response<User.Response>>() {}.type
        responseUser = Gson().fromJson<Response<User.Response>>(userFile.readText(), typeUserToken)

        val typeErrorToken = object : TypeToken<Response<Error>>() {}.type
        responseError = Gson().fromJson<Response<Error>>(errorFile.readText(), typeErrorToken)
    }

    @Test
    fun `user response should be parse correctly`() {
        val expectedUser = User.Response("cec34607-0761-4a59-8357-18963e42a1aa", "john.doe@example.com", "John", "Doe")
        responseUser.data shouldEqual expectedUser
        responseUser.version shouldEqual "1"
        responseUser.success shouldEqual true
    }

    @Test
    fun `error response should be parse correctly`() {
        val expectedError = Error("server:internal_server_error", "Server error")
        responseError.data shouldEqual expectedError
        responseError.version shouldEqual "1"
        responseError.success shouldEqual false
    }
}
