package co.omisego.omgshop.models

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 20/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

enum class TransactionDirection(val value: String) {
    FROM("from"),
    TO("to");

    override fun toString(): String = this.value

    companion object {
        fun from(value: String): TransactionDirection {
            try {
                return TransactionDirection.values().first { it.value == value.toLowerCase() }
            } catch (e: NoSuchElementException) {
                throw IllegalStateException("$value doesn't existed in TransactionDirection")
            }
        }
    }
}

data class TransactionRecord(
    val transactionId: String,
    val transactionDirection: TransactionDirection,
    val transactionAmount: String,
    val transactionDateTime: String
)
