package co.omisego.omgshop.extensions

import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 29/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
fun TransactionConsumption.readableAmount() =
    this.token.fromSubunitToUnit(this.amount ?: this.transactionRequest.amount ?: 0.bd).toPlainString()
