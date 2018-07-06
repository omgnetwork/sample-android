package co.omisego.omgshop.extensions

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

fun Number.thousandSeparator(numberOfDecimal: Int = 2): String {
    return "%,.${numberOfDecimal}f".format(this)
}
