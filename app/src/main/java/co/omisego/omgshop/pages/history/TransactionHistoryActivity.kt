package co.omisego.omgshop.pages.history

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omgshop.pages.history.adapter.TransactionHistoryAdapter
import co.omisego.omgshop.pages.history.viewholder.listener.LoadMoreCommand
import co.omisego.omisego.model.transaction.list.Transaction
import kotlinx.android.synthetic.main.activity_transaction_history.*

class TransactionHistoryActivity : BaseActivity<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller, TransactionHistoryContract.Presenter>(), TransactionHistoryContract.View, LoadMoreCommand {
    override val mPresenter: TransactionHistoryContract.Presenter by lazy { TransactionHistoryPresenter() }
    private val transactionListAdapter by lazy {
        TransactionHistoryAdapter(loadMoreCommand = this)
    }
    private var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        setupToolbar()
        setupRecyclerView()
        loadTransactions()
        mPresenter.loadCurrentAddress()
        swipeRefresh.setOnRefreshListener {
            transactionListAdapter.clear()
            loadTransactions()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_transaction_history_list_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = transactionListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun loadTransactions(page: Int = 1) {
        mPresenter.caller?.loadTransactionList(mPresenter.createTransactionListParams(page))
    }

    override fun onLoadMore() {
        loadTransactions(currentPage)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showCurrentAddress(address: String) {
        transactionListAdapter.myAddress = address
        tvAddress.text = address
    }

    override fun addTransactions(transactionList: List<Transaction>, page: Int) {
        swipeRefresh.isRefreshing = false
        transactionListAdapter.getLoadingListener()?.onFinished()
        transactionListAdapter.addTransactions(transactionList)
        currentPage = page
    }

    override fun showLoadTransactionListFail() {
        swipeRefresh.isRefreshing = false
        transactionListAdapter.getLoadingListener()?.onFinished()
        logi("Failed to fetch transaction list")
    }
}
