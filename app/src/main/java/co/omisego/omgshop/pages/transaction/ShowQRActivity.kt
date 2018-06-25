package co.omisego.omgshop.pages.transaction

import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.qrcode.generator.generateQRCode
import kotlinx.android.synthetic.main.activity_show_qr.*

class ShowQRActivity : AppCompatActivity() {

    companion object {
        const val INTENT_TRANSACTION_REQUEST = "transaction_request"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr)
        setupToolbar()
        val transactionRequest = intent.getParcelableExtra<TransactionRequest>(INTENT_TRANSACTION_REQUEST)

        ivQRCode.visibility = View.INVISIBLE
        TransitionManager.beginDelayedTransition(rootLayout)
        showQR(transactionRequest)
    }

    private fun showQR(transactionRequest: TransactionRequest) {
        val bitmap = transactionRequest.generateQRCode(size = 1024)
        ivQRCode.setImageBitmap(bitmap)
        ivQRCode.visibility = View.VISIBLE
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_show_qr_transaction_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
