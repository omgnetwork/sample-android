package co.omisego.omgshop.pages.transaction.consume

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.custom.MinimalTextChangeListener
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestType
import kotlinx.android.synthetic.main.activity_consume_transaction.*
import kotlinx.android.synthetic.main.toolbar.*

class ConsumeTransactionActivity : BaseActivity<ConsumeTransactionContract.View, ConsumeTransactionCallerContract.Caller, ConsumeTransactionContract.Presenter>(),
    ConsumeTransactionContract.View {
    override val mPresenter: ConsumeTransactionContract.Presenter by lazy { ConsumeTransactionPresenter() }
    private var transactionConsumption: TransactionConsumption? = null

    companion object {
        const val INTENT_EXTRA_TRANSACTION_REQUEST = "transaction_request"
    }

    private lateinit var transactionRequest: TransactionRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consume_transaction)
        transactionRequest = intent.getParcelableExtra(INTENT_EXTRA_TRANSACTION_REQUEST)
        setupToolbar()
        setupUIData()
        btnConsume.setOnClickListener {
            sendTransactionConsumptionParams()
        }
        amountField.editText?.addTextChangedListener(MinimalTextChangeListener {
            if (it.isEmpty()) {
                amountField.setError(true)
                amountField.setErrorText("Amount should not be null")
            } else {
                amountField.setError(false)
            }
        })
    }

    private fun sendTransactionConsumptionParams() {
        if (amountField.getText().isEmpty()) {
            amountField.setError(true)
            amountField.setErrorText("Amount should not be null")
            return
        }
        val params = mPresenter.sanitizeRequestParams(
            transactionRequest,
            amountField.getText(),
            transactionRequest.token,
            addressField.getText(),
            correlationField.getText()
        )
        mPresenter.caller?.consume(request = params)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val txConsumption = transactionConsumption ?: return
        mPresenter.caller?.listenTransactionConsumption(transactionConsumption = txConsumption)
    }

    override fun onStop() {
        super.onStop()
        val txConsumption = transactionConsumption ?: return
        mPresenter.caller?.stopListeningTransactionConsumption(transactionConsumption = txConsumption)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_consume_transaction_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupUIData() {
        if (!transactionRequest.allowAmountOverride) {
            amountField.editTextEnabled = false
            val tokenAmount = transactionRequest.amount?.divide(transactionRequest.token.subunitToUnit)?.toPlainString()
            amountField.editText?.setText(tokenAmount)
        }
        tvToken.text = transactionRequest.token.symbol
        addressFromField.editText?.setText(transactionRequest.address ?: "")
        addressField.hint = Preference.loadWalletAddress()
        if (transactionRequest.type == TransactionRequestType.RECEIVE)
            addressFromField.title = "Send to"
    }

    override fun showConsumeTransactionFailed(response: APIError) {
        Toast.makeText(this, response.description, Toast.LENGTH_LONG).show()
    }

    override fun showConsumeTransactionSuccess(response: TransactionConsumption) {
        transactionConsumption = response
        Toast.makeText(this, "Consumed transaction id ${response.id} successful", Toast.LENGTH_LONG).show()
    }

    override fun showTransactionFinalizedFailed(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showTransactionFinalizedSuccess(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        finish()
    }
}
