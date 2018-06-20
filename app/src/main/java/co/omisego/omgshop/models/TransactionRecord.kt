package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
data class TransactionRecord(
    val transactionId: String,
    val transactionDirection: String,
    val transactionAmount: String,
    val transactionDateTime: String
)
