package co.omisego.omgshop.pages.transaction.generate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.custom.MinimalTextChangeListener
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.pages.transaction.generate.caller.GenerateTransactionCallerContract.Caller
import co.omisego.omgshop.pages.transaction.showqr.ShowQRActivity
import co.omisego.omgshop.view.SpinnerField
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.Token
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.model.transaction.request.TransactionRequestCreateParams
import kotlinx.android.synthetic.main.activity_generate_transaction.*

class GenerateTransactionActivity :
    BaseActivity<GenerateTransactionContract.View, Caller, GenerateTransactionContract.Presenter>(), GenerateTransactionContract.View {
    override val mPresenter: GenerateTransactionContract.Presenter by lazy {
        GenerateTransactionPresenter()
    }
    private var selectedToken: Token? = null
    private var amountErrorText: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_transaction)
        tokenField.selectionTokenListener = object : SpinnerField.OnSelectionTokenListener {
            override fun onItemSelected(token: Token) {
                selectedToken = token
            }
        }
        setupToolbar()
        initDefaultValue()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_generate_transaction_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_generate_transaction, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.generate -> {
                try {
                    val params = createTransactionRequestCreateParams()
                    if (params != null)
                        mPresenter.caller?.generate(request = params)
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    amountErrorText = e.message
                    amountField.setError(true)
                    amountField.setErrorText(amountErrorText!!)
                    amountField?.requestFocus()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTransactionRequestCreateParams(): TransactionRequestCreateParams? {
        return retrieveTransactionRequestCreateParams()
    }

    private fun retrieveTransactionRequestCreateParams(): TransactionRequestCreateParams? {
        if (tokenField.isTokenAvailable()) {
            return mPresenter.sanitizeTransactionRequestCreateParams(
                tokenField.selectedToken,
                transactionType.value,
                amountField.getText(),
                requiredConfirmation.value,
                allowAmountField.value,
                maxConsumption.progress * maxConsumption.multiplier,
                maxConsumptionPerUser.progress * maxConsumptionPerUser.multiplier,
                addressField.getText(),
                consumptionTimeField.getText(),
                correlationIdField.getText()
            )
        } else {
            Toast.makeText(this, "There's no tokens available.", Toast.LENGTH_SHORT).show()
            return null
        }
    }

    private fun initDefaultValue() {
        addressField.editText?.hint = Preference.loadWalletAddress()
        allowAmountField.value = true
        amountField.editText?.addTextChangedListener(MinimalTextChangeListener {
            if (it.isEmpty() && amountErrorText != null) {
                amountField?.setError(true)
                amountField?.setErrorText(amountErrorText!!)
            } else {
                amountField?.setError(false)
            }
        })
    }

    override fun showCreateTransactionSuccess(response: TransactionRequest) {
        startActivity(Intent(this, ShowQRActivity::class.java).apply {
            putExtra(ShowQRActivity.INTENT_TRANSACTION_REQUEST, response)
        })
    }

    override fun showCreateTransactionFailed(response: APIError) {
        Toast.makeText(this, response.description, Toast.LENGTH_LONG).show()
    }
}
