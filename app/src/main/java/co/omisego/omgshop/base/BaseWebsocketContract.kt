package co.omisego.omgshop.base

import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 26/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */
interface BaseWebsocketContract {
    interface View {
        fun showTransactionFinalizedFailed(msg: String)
        fun showTransactionFinalizedSuccess(msg: String)
        fun showIncomingTransactionConsumptionDialog(transactionConsumption: TransactionConsumption)
    }

    interface Presenter {
        fun approve(transactionConsumption: TransactionConsumption)
        fun reject(transactionConsumption: TransactionConsumption)
        fun listenTransactionRequest(transactionRequest: TransactionRequest)
        fun stopListeningTransactionRequest(transactionRequest: TransactionRequest)
        fun listenTransactionConsumption(transactionConsumption: TransactionConsumption)
        fun stopListeningTraansactionConsumption(transactionConsumption: TransactionConsumption)
    }
}