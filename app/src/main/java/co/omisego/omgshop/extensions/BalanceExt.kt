package co.omisego.omgshop.extensions

import co.omisego.omisego.model.Balance

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
fun Balance.readableAmount() = this.token.fromSubunitToUnit(this.amount).toPlainString()
