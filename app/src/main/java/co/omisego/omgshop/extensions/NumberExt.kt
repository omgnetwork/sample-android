package co.omisego.omgshop.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import java.math.BigInteger
import java.text.DecimalFormat

fun BigInteger.thousandSeparator(): String {
    return DecimalFormat("#,###,###").format(this)
}

fun Double.thousandSeparator(): String {
    return "%,.2f".format(this)
}