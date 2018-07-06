package co.omisego.omgshop

import co.omisego.omisego.extension.bd
import org.amshove.kluent.shouldEqual
import org.junit.Test

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class UtilityTest {
    @Test
    fun `test BigDecimal represents an appropriate decimal precision`() {
        1.bd.divide("1000000000000000000000000000000000000000".toBigDecimal()) shouldEqual 0.1.bd.pow(39)
    }

    @Test
    fun `test insert list immutable should be working fine`() {
        val numbers = listOf(1, 2, 3, 4)
        numbers.subList(0, 2) + 5 + numbers.subList(3, 4) shouldEqual listOf(1, 2, 5, 4)
        numbers shouldEqual listOf(1, 2, 3, 4)
    }
}
