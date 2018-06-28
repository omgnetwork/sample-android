package co.omisego.omgshop.pages.transaction.dialog

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/24/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseDialogFragment
import co.omisego.omgshop.pages.transaction.dialog.caller.TransactionConfirmCallerContract
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequestType
import kotlinx.android.synthetic.main.dialog_transaction_confirm.*
import kotlinx.android.synthetic.main.dialog_transaction_confirm.view.*

class TransactionConfirmDialogFragment :
    BaseDialogFragment<TransactionConfirmContract.View, TransactionConfirmCallerContract.Caller, TransactionConfirmContract.Presenter>(),
    TransactionConfirmContract.View {
    override val mPresenter: TransactionConfirmContract.Presenter by lazy { TransactionConfirmPresenter() }
    private lateinit var transactionConsumption: TransactionConsumption

    companion object {
        fun newInstance(transactionConsumption: TransactionConsumption): TransactionConfirmDialogFragment {
            val fragment = TransactionConfirmDialogFragment()
            val args = Bundle().apply {
                putParcelable("transaction_consumption", transactionConsumption)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transactionConsumption = arguments?.getParcelable("transaction_consumption") ?: return dismiss()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_transaction_confirm, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = transactionConsumption.user?.username?.split("|") ?: listOf("unknown", "unknown")
        val direction = if (transactionConsumption.transactionRequest.type == TransactionRequestType.SEND) {
            "take"
        } else {
            "send"
        }
        val amount = transactionConsumption.amount.divide(transactionConsumption.token.subunitToUnit).toPlainString()
        val tokenSymbol = transactionConsumption.token.symbol.toUpperCase()

        view.tvDescription.text = getString(
            R.string.dialog_confirmation_description,
            "${username[0]}",
            direction,
            amount,
            tokenSymbol
        )

        btnApprove.setOnClickListener {
            mPresenter.caller?.approve(id = transactionConsumption.id)
            dismiss()
        }
        btnReject.setOnClickListener {
            mPresenter.caller?.reject(id = transactionConsumption.id)
            dismiss()
        }
    }

    override fun showRejectSuccess(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun showApproveSuccess(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun showConfirmationFail(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }
}