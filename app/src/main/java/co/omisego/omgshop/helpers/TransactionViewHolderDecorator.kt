package co.omisego.omgshop.helpers

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 6/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Context
import android.support.v4.content.ContextCompat
import co.omisego.omgshop.R
import co.omisego.omgshop.extensions.fromSubunitToUnit
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omisego.extension.bd
import co.omisego.omisego.model.pagination.Paginable
import co.omisego.omisego.model.transaction.Transaction
import co.omisego.omisego.model.transaction.TransactionSource
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.consumption.TransactionConsumptionStatus
import java.math.RoundingMode

class TransactionViewHolderDecorator(val context: Context) {
    inline fun TransactionSource.formatTransactionAmount(
        sameAddress: Boolean,
        draw: (formattedAmount: String) -> Unit
    ) {
        val numberOfDecimals = Math.log10(this.token.subunitToUnit.toDouble()).toInt()
        val readableAmount = this.token.fromSubunitToUnit(this.amount)
        val amount = readableAmount
            .setScale(numberOfDecimals, RoundingMode.HALF_EVEN)
            .thousandSeparator(numberOfDecimal = numberOfDecimals)

        val symbol = token.symbol
        if (sameAddress) {
            draw(context.getString(R.string.transaction_amount_to, amount, symbol))
        } else {
            draw(context.getString(R.string.transaction_amount_from, amount, symbol))
        }
    }

    inline fun TransactionConsumption.formatTransactionAmount(
        sameAddress: Boolean,
        draw: (formattedAmount: String) -> Unit
    ) {
        val numberOfDecimals = Math.log10(this.token.subunitToUnit.toDouble()).toInt()
        val readableAmount = this.token.fromSubunitToUnit(this.amount ?: this.transactionRequest.amount ?: 0.bd)
        val amount = readableAmount
            .setScale(numberOfDecimals, RoundingMode.HALF_EVEN)
            .thousandSeparator(numberOfDecimal = numberOfDecimals)

        val symbol = token.symbol
        if (sameAddress) {
            draw(context.getString(R.string.transaction_amount_to, amount, symbol))
        } else {
            draw(context.getString(R.string.transaction_amount_from, amount, symbol))
        }
    }

    inline fun Transaction.setTransactionAddress(
        sameAddress: Boolean,
        draw: (address: String) -> Unit
    ) {
        val address = if (sameAddress) {
            this.to.address
        } else {
            this.from.address
        }

        draw(address)
    }

    inline fun colorizedTransactionAmount(
        sameAddress: Boolean,
        draw: (color: Int) -> Unit
    ) {
        val color = if (sameAddress) {
            ContextCompat.getColor(context, R.color.colorRed)
        } else {
            ContextCompat.getColor(context, R.color.colorGreen)
        }

        draw(color)
    }

    inline fun TransactionConsumptionStatus.colorizedTransactionStatus(
        draw: (color: Int) -> Unit
    ) {
        val color = when (this) {
            TransactionConsumptionStatus.PENDING -> ContextCompat.getColor(context, R.color.colorYellow)
            TransactionConsumptionStatus.CONFIRMED,
            TransactionConsumptionStatus.APPROVED -> ContextCompat.getColor(context, R.color.colorGreen)
            TransactionConsumptionStatus.REJECTED,
            TransactionConsumptionStatus.FAILED,
            TransactionConsumptionStatus.UNKNOWN -> ContextCompat.getColor(context, R.color.colorRed)
        }

        draw(color)
    }

    fun Paginable.Transaction.TransactionStatus.colorizedTransactionStatus(
        draw: (color: Int) -> Unit
    ) {
        val color = when (this) {
            Paginable.Transaction.TransactionStatus.PENDING -> ContextCompat.getColor(context, R.color.colorYellow)
            Paginable.Transaction.TransactionStatus.CONFIRMED -> ContextCompat.getColor(context, R.color.colorGreen)
            Paginable.Transaction.TransactionStatus.FAILED, Paginable.Transaction.TransactionStatus.UNKNOWN ->
                ContextCompat.getColor(context, R.color.colorRed)
        }

        draw(color)
    }

    inline fun setTransactionDirection(
        sameAddress: Boolean,
        draw: (direction: String) -> Unit
    ) {
        val direction = if (sameAddress) "To" else "From"
        draw(direction)
    }
}