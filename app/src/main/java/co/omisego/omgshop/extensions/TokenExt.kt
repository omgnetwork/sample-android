package co.omisego.omgshop.extensions

import co.omisego.omisego.model.Token
import java.math.BigDecimal

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

/**
 * Return subunit amount
 */
fun Token.fromUnitToSubunit(unitAmount: BigDecimal) = unitAmount.multiply(this.subunitToUnit)

/**
 * Returns unit amount
 */
fun Token.fromSubunitToUnit(subUnitAmount: BigDecimal) = subUnitAmount.divide(subunitToUnit)
