package co.omisego.omgshop.extensions

import java.math.BigInteger
import java.text.DecimalFormat


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/12/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

fun BigInteger.thousandSeparator(): String {
    return DecimalFormat("#,###,###").format(this)
}