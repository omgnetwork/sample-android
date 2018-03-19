package co.omisego.omgshop.helpers

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import java.util.regex.Pattern

class Validator {
    private val EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"
    private val pattern = Pattern.compile(EMAIL_PATTERN)
    fun validateEmail(email: String): Boolean = pattern.matcher(email).matches()

    fun validatePassword(password: String): Boolean = password.length >= 8

    fun validateFirstName(firstName: String): Boolean = firstName.isNotEmpty()

    fun validateLastName(lastName: String): Boolean = lastName.isNotEmpty()

}