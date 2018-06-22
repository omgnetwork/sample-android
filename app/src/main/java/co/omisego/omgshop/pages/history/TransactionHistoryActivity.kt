package co.omisego.omgshop.pages.history

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.logi
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omisego.model.pagination.Paginable
import co.omisego.omisego.model.pagination.Paginable.Transaction.TransactionStatus
import co.omisego.omisego.model.transaction.list.Transaction
import co.omisego.omisego.model.transaction.list.TransactionSource
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.viewholder_load_more.view.*
import kotlinx.android.synthetic.main.viewholder_transaction_record.view.*
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class TransactionHistoryActivity : BaseActivity<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller, TransactionHistoryContract.Presenter>(), TransactionHistoryContract.View {
    override val mPresenter: TransactionHistoryContract.Presenter by lazy { TransactionHistoryPresenter() }
    private var myAddress: String = ""
    private val transactionListAdapter by lazy { TransactionHistoryAdapter() }
    private var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        setupToolbar()
        setupRecyclerView()
        mPresenter.caller?.loadTransactionList(mPresenter.createTransactionListParams(currentPage + 1))
        mPresenter.loadCurrentAddress()
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
        transactionListAdapter.getLoadingListener()?.onFinished()
        transactionListAdapter.addTransactions(transactionList)
        currentPage = page
    }

    override fun showLoadTransactionListFail() {
        // Do something
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
                    TransactionHistoryViewHolder(view)
                }
                else -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_load_more, parent, false)
                    val loadMoreViewHolder = LoadMoreViewHolder(view)
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

        inner class TransactionHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val dateFormat by lazy { SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US) }

            fun bindItem(record: Transaction) {
                with(record) {
                    val isSameAddress = isSameAddress(record.from)

                    setTransactionDirection(isSameAddress)
                    colorizedTransactionAmount(isSameAddress)
                    record.setTransactionAddress(isSameAddress)
                    record.status.colorizedTransactionStatus()
                    record.from.formatTransactionAmount(isSameAddress)
                    itemView.tvTransactionStatus.text = "- ${record.status.value.capitalize()}"
                    itemView.tvTransactionDate.text = dateFormat.format(record.createdAt)
                }
            }

            private fun Transaction.setTransactionAddress(sameAddress: Boolean) {
                itemView.tvTransactionAddress.text = if (sameAddress) {
                    this.to.address
                } else {
                    this.from.address
                }
            }

            private fun colorizedTransactionAmount(sameAddress: Boolean) {
                val color = if (sameAddress) {
                    ContextCompat.getColor(itemView.context, R.color.colorRed)
                } else {
                    ContextCompat.getColor(itemView.context, R.color.colorGreen)
                }

                itemView.tvTransactionAmount.setTextColor(color)
            }

            private fun Paginable.Transaction.TransactionStatus.colorizedTransactionStatus() {
                val color = when (this) {
                    TransactionStatus.PENDING -> ContextCompat.getColor(itemView.context, R.color.colorYellow)
                    TransactionStatus.CONFIRMED -> ContextCompat.getColor(itemView.context, R.color.colorGreen)
                    TransactionStatus.FAILED, TransactionStatus.UNKNOWN -> ContextCompat.getColor(itemView.context, R.color.colorRed)
                }

                itemView.tvTransactionStatus.setTextColor(color)
            }

            private fun TransactionSource.formatTransactionAmount(sameAddress: Boolean) {
                val amount = String.format("%.1f", this.amount.divide(this.token.subunitToUnit, RoundingMode.FLOOR))
                val prefix = if (sameAddress) {
                    "-"
                } else {
                    "+"
                }
                itemView.tvTransactionAmount.text = "$prefix $amount OMG"
            }

            private fun setTransactionDirection(sameAddress: Boolean) {
                val direction = if (sameAddress) "To" else "From"
                itemView.tvTransactionDirection.text = direction
            }

            private fun isSameAddress(source: TransactionSource) = myAddress == source.address
        }

        inner class LoadMoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ItemLoadingListener {
            fun bindClick() {
                itemView.tvLoadMore.text = getString(R.string.load_more, mPresenter.getPerPage())
                itemView.setOnClickListener {
                    setViewLoading(true)
                    val request = mPresenter.createTransactionListParams(currentPage + 1)
                    mPresenter.caller?.loadTransactionList(request)
                }
            }

            override fun onFinished() {
                setViewLoading(false)
            }

            private fun setViewLoading(loading: Boolean) {
                if (loading) {
                    itemView.tvLoadMore.visibility = View.INVISIBLE
                    itemView.progressBar.visibility = View.VISIBLE
                    itemView.isEnabled = false
                } else {
                    itemView.isEnabled = true
                    itemView.tvLoadMore.visibility = View.VISIBLE
                    itemView.progressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    interface ItemLoadingListener {
        fun onFinished()
    }
}
