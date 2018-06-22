package co.omisego.omgshop.pages.history

import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omgshop.pages.history.viewholder.LoadMoreViewCommand
import co.omisego.omgshop.pages.history.viewholder.LoadMoreViewHolder
import co.omisego.omgshop.pages.history.viewholder.TransactionHistoryViewHolder
import co.omisego.omisego.model.transaction.list.Transaction
import kotlinx.android.synthetic.main.activity_transaction_history.*

class TransactionHistoryActivity : BaseActivity<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller, TransactionHistoryContract.Presenter>(), TransactionHistoryContract.View, LoadMoreViewCommand {
    override val mPresenter: TransactionHistoryContract.Presenter by lazy { TransactionHistoryPresenter() }
    private var myAddress: String = ""
    private val transactionListAdapter by lazy { TransactionHistoryAdapter() }
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

    override fun loadMore() {
        loadTransactions(currentPage)
    }

    private fun loadTransactions(page: Int = 1) {
        mPresenter.caller?.loadTransactionList(mPresenter.createTransactionListParams(page))
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showCurrentAddress(address: String) {
        myAddress = address
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

    inner class TransactionHistoryAdapter(
            private val transactionList: MutableList<Transaction> = mutableListOf()
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val typeItem = 1
        private val typeLoadMore = 2
        private var loadingListener: ItemLoadingListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                typeItem -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_transaction_record, parent, false)
                    TransactionHistoryViewHolder(view, myAddress)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_load_more, parent, false)
                    val loadMoreViewHolder = LoadMoreViewHolder(view, this@TransactionHistoryActivity)
                    loadingListener = loadMoreViewHolder
                    return loadMoreViewHolder
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (transactionList.size == position || transactionList.isEmpty()) typeLoadMore
            else typeItem
        }

        override fun getItemCount() = transactionList.size + 1 // One more row is for load more.

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is TransactionHistoryViewHolder -> holder.bindItem(transactionList[position])
                is LoadMoreViewHolder -> holder.bindClick()
            }
        }

        fun getLoadingListener() = loadingListener

        fun addTransactions(newTransactionList: List<Transaction>) {
            val transactionHistoryDiffCallback = TransactionHistoryDiffCallback(
                    this.transactionList,
                    this.transactionList + newTransactionList
            )
            val diffResult = DiffUtil.calculateDiff(transactionHistoryDiffCallback)
            diffResult.dispatchUpdatesTo(this)
            this.transactionList.addAll(newTransactionList)
        }

        fun clear() {
            val transactionHistoryDiffCallback = TransactionHistoryDiffCallback(
                    this.transactionList,
                    mutableListOf()
            )
            val diffResult = DiffUtil.calculateDiff(transactionHistoryDiffCallback)
            diffResult.dispatchUpdatesTo(this)
            this.transactionList.clear()
        }
    }

    interface ItemLoadingListener {
        fun onFinished()
    }
}
