package co.omisego.omgshop.pages.transaction.showqr

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.pages.transaction.dialog.TransactionConfirmDialogFragment
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCallerContract
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.qrcode.generator.generateQRCode
import kotlinx.android.synthetic.main.activity_show_qr.*

class ShowQRActivity : BaseActivity<ShowQRContract.View, ShowQRCallerContract.Caller, ShowQRContract.Presenter>(), ShowQRContract.View {
    override val mPresenter: ShowQRContract.Presenter by lazy {
        ShowQRPresenter()
    }

    companion object {
        const val INTENT_TRANSACTION_REQUEST = "transaction_request"
    }

    private lateinit var transactionRequest: TransactionRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_qr)
        setupToolbar()
        transactionRequest = intent.getParcelableExtra(INTENT_TRANSACTION_REQUEST)
        showQR(transactionRequest)
    }

    private fun showQR(transactionRequest: TransactionRequest) {
        val bitmap = transactionRequest.generateQRCode(size = 1024)
        ivQRCode.setImageBitmap(bitmap)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_show_qr_transaction_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.caller?.joinChannel(request = transactionRequest)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.leaveChannel(transactionRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showTransactionFinalizedFailed(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showTransactionFinalizedSuccess(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showIncomingTransactionConsumptionDialog(transactionConsumption: TransactionConsumption) {
        val dialog = TransactionConfirmDialogFragment.newInstance(transactionConsumption)
        dialog.show(supportFragmentManager, "")
    }
}
