package co.omisego.omgshop.helpers

import java.util.regex.Pattern


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class Validator {
    private val EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"
    private val pattern = Pattern.compile(EMAIL_PATTERN)

    fun validateEmail(email: String): Boolean = pattern.matcher(email).matches()

    fun validatePassword(password: String): Boolean = password.length >= 8
}