package co.omisego.omgshop.pages.transaction.consume

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.pages.transaction.consume.caller.ConsumeTransactionCallerContract
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.toTransactionConsumptionParams
import kotlinx.android.synthetic.main.activity_consume_transaction.*

class ConsumeTransactionActivity : BaseActivity<ConsumeTransactionContract.View, ConsumeTransactionCallerContract.Caller, ConsumeTransactionContract.Presenter>(),
    ConsumeTransactionContract.View {
    override val mPresenter: ConsumeTransactionContract.Presenter by lazy { ConsumeTransactionPresenter() }

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
            val address = if (addressField.getText().isEmpty()) null else addressField.getText()
            val amount = if (amountField.getText().isEmpty()) null else amountField.getText().toBigDecimal()
            val request = transactionRequest.toTransactionConsumptionParams(
                amount = amount?.multiply(tokenField?.selectedToken?.subunitToUnit),
                tokenId = tokenField.selectedToken?.id,
                address = address,
                correlationId = correlationField.getText()
            ) ?: return@setOnClickListener
            mPresenter.caller?.consume(request = request)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_consume_transaction_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupUIData() {
        tokenField.selectToken(transactionRequest.token)
        tokenField.isEnabled = false
        addressFromField.editText?.setText(transactionRequest.address ?: "")
    }

    override fun showConsumeTransactionFailed(response: APIError) {
        Toast.makeText(this, response.description, Toast.LENGTH_LONG).show()
        logi(response)
    }

    override fun showConsumeTransactionSuccess(response: TransactionConsumption) {
        Toast.makeText(this, "Consumed transaction id ${response.id} successful", Toast.LENGTH_LONG).show()
        logi(response)
    }
}
