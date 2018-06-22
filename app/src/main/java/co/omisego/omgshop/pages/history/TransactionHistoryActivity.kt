package co.omisego.omgshop.pages.history

import android.os.Bundle
import android.support.v4.content.ContextCompat
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
import co.omisego.omisego.model.pagination.PaginationList
import co.omisego.omisego.model.transaction.list.Transaction
import co.omisego.omisego.model.transaction.list.TransactionSource
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.viewholder_transaction_record.view.*
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class TransactionHistoryActivity : BaseActivity<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller, TransactionHistoryContract.Presenter>(), TransactionHistoryContract.View {
    override val mPresenter: TransactionHistoryContract.Presenter by lazy { TransactionHistoryPresenter() }
    private var myAddress: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        setupToolbar()
        mPresenter.caller?.loadTransactionList(mPresenter.createTransactionListParams())
        mPresenter.loadCurrentAddress()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_transaction_history_list_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

    override fun showTransactionList(transactionList: PaginationList<Transaction>) {
        recyclerView.adapter = TransactionHistoryAdapter(transactionList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun showLoadTransactionListFail() {
        // Do something
        logi("Failed to fetch transaction list")
    }

    inner class TransactionHistoryAdapter(private val transactionList: PaginationList<Transaction>) : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHistoryViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_transaction_record, parent, false)
            return TransactionHistoryViewHolder(rootView)
        }

        override fun getItemCount() = transactionList.data.size

        override fun onBindViewHolder(holder: TransactionHistoryViewHolder, position: Int) {
            holder.bindItem(transactionList.data[position])
        }

        inner class TransactionHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val dateFormat by lazy { SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US) }

            fun bindItem(record: Transaction) {
                with(record) {
                    val isSameAddress = isSameAddress(record.from)

                    setTransactionDirection(isSameAddress)
                    record.setTransactionAddress(isSameAddress)
                    colorizedTransactionAmount(isSameAddress)
                    record.from.formatTransactionAmount(isSameAddress)
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

            private fun TransactionSource.formatTransactionAmount(sameAddress: Boolean) {
                val amount = this.amount.divide(this.token.subunitToUnit, RoundingMode.FLOOR)
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
    }
}
