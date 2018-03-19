package co.omisego.omgshop.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

@Suppress("IllegalIdentifier")
class ResponseTest {
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

        val typeErrorToken = object : TypeToken<Response<Error>>() {}.type
        responseError = Gson().fromJson<Response<Error>>(errorFile.readText(), typeErrorToken)
    }

    @Test
    fun `error response should be parse correctly`() {
        val expectedError = Error("server:internal_server_error", "Server error")
        responseError.data shouldEqual expectedError
        responseError.version shouldEqual "1"
        responseError.success shouldEqual false
    }
}
