package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

data class Credential(
    val userId: String,
    val authenticationToken: String,
    val omisegoAuthenticationToken: String
)
