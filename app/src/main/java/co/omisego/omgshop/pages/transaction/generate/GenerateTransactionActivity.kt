package co.omisego.omgshop.pages.transaction.generate

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.logi
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_transaction)
        tokenField.selectionTokenListener = object : SpinnerField.OnSelectionTokenListener {
            override fun onItemSelected(token: Token) {
                selectedToken = token
            }
        }
        setupToolbar()
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
                val params = createTransactionRequestCreateParams()
                mPresenter.caller?.generate(request = params)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun createTransactionRequestCreateParams(): TransactionRequestCreateParams {
        return TransactionRequestCreateParams(
            tokenId = selectedToken?.id ?: ""
        )
    }

    override fun showCreateTransactionSuccess(response: TransactionRequest) {
        logi("Success $response")
        startActivity(Intent(this, ShowQRActivity::class.java).apply {
            putExtra(ShowQRActivity.INTENT_TRANSACTION_REQUEST, response)
        })
    }

    override fun showCreateTransactionFailed(response: APIError) {
        logi("Fail $response")
    }
}
