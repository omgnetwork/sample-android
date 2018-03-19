package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/27/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

object Login {
    data class Request(val email: String, val password: String)
}
