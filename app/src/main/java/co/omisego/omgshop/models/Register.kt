package co.omisego.omgshop.models

import com.google.gson.annotations.SerializedName


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

object Register {
    data class Request(
            @SerializedName("first_name") val firstName: String,
            @SerializedName("last_name") val lastName: String,
            @SerializedName("email") val email: String,
            @SerializedName("password") val password: String
    )

    data class Response(
            @SerializedName("user_id") val userId: String,
            @SerializedName("authentication_token") val authenticationToken: String,
            @SerializedName("omisego_authentication_token") val omisegoAuthenticationToken: String
    )
}
