package co.omisego.omgshop.pages.history

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omgshop.pages.history.adapter.TransactionHistoryAdapter
import co.omisego.omgshop.pages.history.viewholder.listener.LoadMoreCommand
import co.omisego.omisego.model.transaction.Transaction
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.toolbar.*

class TransactionHistoryActivity : BaseActivity<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller, TransactionHistoryContract.Presenter>(), TransactionHistoryContract.View, LoadMoreCommand {
    override val mPresenter: TransactionHistoryContract.Presenter by lazy { TransactionHistoryPresenter() }
    private val transactionListAdapter by lazy {
        TransactionHistoryAdapter()
    }
    private var currentPage: Int = 0
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0
    private var loading: Boolean = false

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
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!loading && totalItemCount <= (lastVisibleItem + PaginationConfig.PER_PAGE)) {
                    onLoadMore()
                }
            }
        })
    }

    private fun loadTransactions(page: Int = 1) {
        loading = true
        mPresenter.caller?.loadTransactionList(mPresenter.createTransactionListParams(page))
    }

    override fun onLoadMore() {
        loadTransactions(currentPage + 1)
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

    override fun addTransactions(transactionList: List<Transaction>, page: Int, isLastPage: Boolean) {
        loading = false
        swipeRefresh.isRefreshing = false
        transactionListAdapter.addTransactions(transactionList)
        currentPage = page
        transactionListAdapter.getLoadingListener()?.onFinished()

        if (page == 1 && !isLastPage)
            transactionListAdapter.getLoadingListener()?.onReloaded()
        else if (isLastPage)
            transactionListAdapter.getLoadingListener()?.onReachedLastPage()
    }

    override fun showLoadTransactionListFail() {
        loading = false
        swipeRefresh.isRefreshing = false
        transactionListAdapter.getLoadingListener()?.onReachedLastPage()
        logi("Failed to fetch transaction list")
    }
}
