package co.omisego.omgshop.models

import com.google.gson.annotations.SerializedName


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */


object User {
    data class Response(val id: String,
                        val email: String,
                        @SerializedName("first_name") val firstName: String,
                        @SerializedName("last_name") val lastName: String)
}
