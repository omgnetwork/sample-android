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
import co.omisego.omgshop.helpers.GsonProvider
import co.omisego.omgshop.models.TransactionDirection
import co.omisego.omgshop.models.TransactionHistory
import co.omisego.omgshop.models.TransactionRecord
import co.omisego.omgshop.pages.checkout.caller.TransactionHistoryCallerContract
import co.omisego.omisego.model.transaction.list.TransactionListParams
import kotlinx.android.synthetic.main.activity_transaction_history.*
import kotlinx.android.synthetic.main.viewholder_transaction_record.view.*

class TransactionHistoryActivity : BaseActivity<TransactionHistoryContract.View, TransactionHistoryCallerContract.Caller, TransactionHistoryContract.Presenter>(), TransactionHistoryContract.View {
    override val mPresenter: TransactionHistoryContract.Presenter by lazy { TransactionHistoryPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_history)
        setupToolbar()
        setupRecyclerView()
        mPresenter.caller?.loadTransactionList(TransactionListParams.create(searchTerm = null))
    }

    private fun getJson(): String {
        val mockJson = """
    {
  "data": [
    {
      "transaction_id": "495a196e-fe0d-4fa0-a11a-6b8b206e45b6",
      "transaction_direction": "From",
      "transaction_date_time": "2017-11-20 18:36:32",
      "transaction_amount": "900.07 OMG"
    },
    {
      "transaction_id": "833697c5-6e7b-47d0-84a1-abda8c2413a8",
      "transaction_direction": "To",
      "transaction_date_time": "2017-10-20 23:46:29",
      "transaction_amount": "742.50 OMG"
    },
    {
      "transaction_id": "7931e5b8-9395-434e-b2f3-71a15c70af0c",
      "transaction_direction": "From",
      "transaction_date_time": "2018-06-01 03:02:47",
      "transaction_amount": "533.24 OMG"
    },
    {
      "transaction_id": "48718fe2-8643-4e7a-8f2e-6c402ba40d00",
      "transaction_direction": "From",
      "transaction_date_time": "2018-03-16 09:57:49",
      "transaction_amount": "874.31 OMG"
    },
    {
      "transaction_id": "7ca55699-8225-4d19-b492-3f50bbba4f18",
      "transaction_direction": "From",
      "transaction_date_time": "2018-01-16 13:49:06",
      "transaction_amount": "200.46 OMG"
    },
    {
      "transaction_id": "55660f99-beb3-403c-b7fd-07e4a6602b3d",
      "transaction_direction": "To",
      "transaction_date_time": "2018-01-26 23:40:41",
      "transaction_amount": "808.55 OMG"
    },
    {
      "transaction_id": "f6ea9e2a-a42f-4665-b213-6d92161bd846",
      "transaction_direction": "From",
      "transaction_date_time": "2018-02-23 07:42:54",
      "transaction_amount": "992.15 OMG"
    },
    {
      "transaction_id": "af1a50af-c5ad-428d-a52a-9ff960e4fc83",
      "transaction_direction": "To",
      "transaction_date_time": "2017-12-22 07:47:43",
      "transaction_amount": "991.95 OMG"
    },
    {
      "transaction_id": "3005138d-4d62-49c3-9ed4-475e81b21e69",
      "transaction_direction": "To",
      "transaction_date_time": "2018-02-19 19:47:13",
      "transaction_amount": "668.37 OMG"
    },
    {
      "transaction_id": "56742db6-fc41-406b-8fe7-ac367620171e",
      "transaction_direction": "From",
      "transaction_date_time": "2018-06-01 23:49:59",
      "transaction_amount": "692.35 OMG"
    },
    {
      "transaction_id": "73ec07de-616b-43fc-a39c-9d781b9a5a07",
      "transaction_direction": "To",
      "transaction_date_time": "2017-09-24 05:44:48",
      "transaction_amount": "732.14 OMG"
    },
    {
      "transaction_id": "45c719cd-3844-4aeb-8cb3-64a9c176f89c",
      "transaction_direction": "To",
      "transaction_date_time": "2018-01-12 16:42:42",
      "transaction_amount": "606.97 OMG"
    },
    {
      "transaction_id": "f9315026-3f28-49f9-85a6-9a0f4bf7c6e3",
      "transaction_direction": "To",
      "transaction_date_time": "2018-01-06 14:56:46",
      "transaction_amount": "562.55 OMG"
    },
    {
      "transaction_id": "9e141bbf-6af8-4703-b6be-5c266601d2aa",
      "transaction_direction": "To",
      "transaction_date_time": "2017-07-11 07:39:43",
      "transaction_amount": "394.27 OMG"
    },
    {
      "transaction_id": "3a2b7b2e-8d01-45d6-9d06-2bc796c29607",
      "transaction_direction": "To",
      "transaction_date_time": "2017-12-25 12:06:54",
      "transaction_amount": "412.49 OMG"
    },
    {
      "transaction_id": "6f85c54b-4a81-469e-ba08-91f5013f4d58",
      "transaction_direction": "To",
      "transaction_date_time": "2018-05-24 05:44:04",
      "transaction_amount": "146.15 OMG"
    },
    {
      "transaction_id": "a6ae68a2-82c6-4805-952d-0ed02f525bd6",
      "transaction_direction": "To",
      "transaction_date_time": "2018-02-15 18:22:45",
      "transaction_amount": "275.37 OMG"
    },
    {
      "transaction_id": "13bef5d2-4dd6-4f58-8729-da06419c918d",
      "transaction_direction": "From",
      "transaction_date_time": "2017-08-17 10:51:35",
      "transaction_amount": "123.30 OMG"
    },
    {
      "transaction_id": "ff975c10-8f0d-4e49-a186-d12742160b9b",
      "transaction_direction": "To",
      "transaction_date_time": "2018-05-16 10:00:09",
      "transaction_amount": "364.83 OMG"
    },
    {
      "transaction_id": "def844a7-a7e7-403e-ab51-593f564931e7",
      "transaction_direction": "From",
      "transaction_date_time": "2017-10-30 07:34:06",
      "transaction_amount": "680.38 OMG"
    }
  ]
}
        """.trimIndent()
        return mockJson
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_transaction_history_list_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        val transactionHistory = GsonProvider.create().fromJson(getJson(), TransactionHistory::class.java)
        recyclerView.adapter = TransactionHistoryAdapter(transactionHistory.data)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showTransactionList(transactionList: List<TransactionRecord>) {
        // Do something
        logi("Show transaction list: $transactionList")
    }

    override fun showLoadTransactionListFail() {
        // Do something
        logi("Failed to fetch transaction list")
    }

    inner class TransactionHistoryAdapter(private val transactionList: List<TransactionRecord>) : RecyclerView.Adapter<TransactionHistoryAdapter.TransactionHistoryViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHistoryViewHolder {
            val rootView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_transaction_record, parent, false)
            return TransactionHistoryViewHolder(rootView)
        }

        override fun getItemCount() = transactionList.size

        override fun onBindViewHolder(holder: TransactionHistoryViewHolder, position: Int) {
            holder.bindItem(transactionList[position])
        }

        inner class TransactionHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bindItem(record: TransactionRecord) {
                with(record) {
                    itemView.tvTransactionId.text = transactionId
                    itemView.tvTransactionDirection.text = transactionDirection.value.capitalize()
                    itemView.tvTransactionDate.text = transactionDateTime
                    itemView.tvTransactionAmount.text = transactionAmount

                    colorizedTransactionAmount(transactionDirection)
                    prefixTransactionAmountByDirection(transactionDirection)
                }
            }

            private fun colorizedTransactionAmount(direction: TransactionDirection) {
                val color = when (direction) {
                    TransactionDirection.FROM -> ContextCompat.getColor(itemView.context, R.color.colorGreen)
                    TransactionDirection.TO -> ContextCompat.getColor(itemView.context, R.color.colorRed)
                }
                itemView.tvTransactionAmount.setTextColor(color)
            }

            private fun prefixTransactionAmountByDirection(direction: TransactionDirection) {
                val amount = itemView.tvTransactionAmount.text
                val prefix = when (direction) {
                    TransactionDirection.FROM -> "+"
                    TransactionDirection.TO -> "-"
                }
                itemView.tvTransactionAmount.text = "$prefix $amount"
            }
        }
    }
}
