package co.omisego.omgshop.pages.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.models.Token
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.viewholder_content_my_profile.view.*

class MyProfileActivity : AppCompatActivity() {
    private val mockListToken = listOf(
            Token("OMG", 0, true),
            Token("KNC", 0, false),
            Token("BTC", 0, false),
            Token("MNT", 0, false),
            Token("ETH", 0, false)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_my_profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val myProfileContentAdapter = MyProfileContentAdapter(mockListToken.toMutableList())
        recyclerView.adapter = myProfileContentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> finish()
            }
        }
        return false
    }

    inner class MyProfileContentAdapter(private var listToken: MutableList<Token>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val TYPE_HEADER = 0
        private val TYPE_CONTENT = 1

        init {
            listToken.add(0, Token("", 0))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            if (position > 0) {
                val contentViewHolder = holder as? MyProfileContentViewHolder
                contentViewHolder?.bind(listToken[position])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                TYPE_HEADER -> {
                    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_header_my_profile, parent, false)
                    MyProfileHeaderViewHolder(itemView)
                }
                else -> {
                    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_content_my_profile, parent, false)
                    MyProfileContentViewHolder(itemView)
                }
            }

        }

        override fun getItemViewType(position: Int) = if (position == 0) TYPE_HEADER else TYPE_CONTENT
        override fun getItemCount(): Int = listToken.size

        inner class MyProfileContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvAmount = itemView.tvAmount
            private val tvToken = itemView.tvToken
            private val ivSelected = itemView.ivSelected
            private val layoutContainer = itemView.layoutContainer

            @SuppressLint("SetTextI18n")
            fun bind(token: Token) {
                tvAmount.text = "${token.amount}"
                tvToken.text = token.token
                val drawable = if (token.selected) ContextCompat.getDrawable(itemView.context, R.drawable.ic_check_24dp) else null
                ivSelected.setImageDrawable(drawable)
                layoutContainer.setOnClickListener {
                    val previousToken = listToken.first { it.selected }
                    val previousTokenIndex = listToken.indexOf(previousToken)

                    previousToken.selected = false
                    listToken[layoutPosition].selected = true

                    notifyItemChanged(layoutPosition)
                    notifyItemChanged(previousTokenIndex)
                }
            }
        }

        inner class MyProfileHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    }
}