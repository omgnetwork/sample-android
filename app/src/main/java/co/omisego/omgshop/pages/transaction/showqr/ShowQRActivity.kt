package co.omisego.omgshop.pages.transaction.showqr

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.pages.transaction.showqr.caller.ShowQRCallerContract
import co.omisego.omgshop.pages.transaction.showqr.consumptions.ConsumptionRecyclerAdapter
import co.omisego.omgshop.pages.transaction.showqr.consumptions.ConsumptionViewHolder
import co.omisego.omisego.model.transaction.consumption.TransactionConsumption
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.qrcode.generator.generateQRCode
import kotlinx.android.synthetic.main.activity_show_qr.*

class ShowQRActivity : BaseActivity<ShowQRContract.View, ShowQRCallerContract.Caller, ShowQRContract.Presenter>(),
    ShowQRContract.View,
    ConsumptionViewHolder.OnConfirmationClickListener {
    private val consumptionRecyclerAdapter = ConsumptionRecyclerAdapter(mutableListOf(), this)
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
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView.adapter = consumptionRecyclerAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun showQR(transactionRequest: TransactionRequest) {
        val bitmap = transactionRequest.generateQRCode(size = 512)
        ivQRCode.setImageBitmap(bitmap)
    }

    private fun setupToolbar() {
        setSupportActionBar(tb as Toolbar)
        supportActionBar?.title = getString(R.string.activity_show_qr_transaction_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        mPresenter.caller?.joinChannel(request = transactionRequest)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.caller?.leaveChannel(request = transactionRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showTransactionFinalizedFailed(transactionConsumption: TransactionConsumption, msg: String) {
        consumptionRecyclerAdapter.update(transactionConsumption)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showTransactionFinalizedSuccess(transactionConsumption: TransactionConsumption, msg: String) {
        consumptionRecyclerAdapter.update(transactionConsumption)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showConfirmationFail(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onClickApprove(transactionConsumption: TransactionConsumption) {
        mPresenter.caller?.approve(id = transactionConsumption.id)
    }

    override fun onClickReject(transactionConsumption: TransactionConsumption) {
        mPresenter.caller?.reject(id = transactionConsumption.id)
    }

    override fun addPendingConsumption(transactionConsumption: TransactionConsumption) {
        consumptionRecyclerAdapter.add(transactionConsumption)
        recyclerView.smoothScrollToPosition(0)
    }
}
