package co.omisego.omgshop.pages.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import co.omisego.androidsdk.models.Balance
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.helpers.SharePrefsManager
import co.omisego.omgshop.pages.login.LoginActivity
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.viewholder_content_my_profile.view.*

class MyProfileActivity : BaseActivity<MyProfileContract.View, MyProfileContract.Presenter>(), MyProfileContract.View {

    override val mPresenter: MyProfileContract.Presenter by lazy {
        MyProfilePresenter(SharePrefsManager(this))
    }
    private lateinit var myProfileContentAdapter: MyProfileContentAdapter
    private var mCurrentSelectedTokenId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        initInstance()
    }


    private fun initInstance() {
        setViewLoading(viewLoading)

        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_my_profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        myProfileContentAdapter = MyProfileContentAdapter(mutableListOf())
        recyclerView.adapter = myProfileContentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        tvLogout.setOnClickListener { mPresenter.logout() }

        mPresenter.loadSettings()
        mPresenter.loadUser()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> finish()
            }
        }
        return false
    }

    override fun setCurrentSelectedTokenId(id: String) {
        mCurrentSelectedTokenId = id
    }

    override fun showBalances(listBalance: List<Balance>) {
        myProfileContentAdapter.updateListToken(listBalance.toMutableList())
    }

    override fun showLogout() {
        val intent = Intent(this, LoginActivity::class.java)

        // Start login activity with clear all history stack.
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)
    }

    override fun showUsername(email: String) {
        tvUsername.text = email
    }

    inner class MyProfileContentAdapter(private var listToken: MutableList<Balance>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val TYPE_HEADER = 0
        private val TYPE_CONTENT = 1

        fun updateListToken(listToken: MutableList<Balance>) {
            this.listToken = listToken
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
            // Ignore the first position which is the header section
            if (position > 0) {
                val contentViewHolder = holder as? MyProfileContentViewHolder
                contentViewHolder?.bind(listToken[position - 1])
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            // Get view holder layout by type
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
        override fun getItemCount(): Int = listToken.size + 1

        inner class MyProfileContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvAmount = itemView.tvAmount
            private val tvToken = itemView.tvToken
            private val ivSelected = itemView.ivSelected
            private val layoutContainer = itemView.layoutContainer

            @SuppressLint("SetTextI18n")
            fun bind(token: Balance) {
                // Set data
                tvAmount.text = token.displayAmount(0)
                tvToken.text = token.mintedToken.symbol

                val drawable = when (mCurrentSelectedTokenId) {
                    token.mintedToken.id -> ContextCompat.getDrawable(itemView.context, R.drawable.ic_check_24dp)
                    else -> null
                }
                ivSelected.setImageDrawable(drawable)

                // bind token click listener
                layoutContainer.setOnClickListener {
                    val currentToken = listToken.first { it.mintedToken.id == mCurrentSelectedTokenId }
                    val currentTokenIndex = listToken.indexOf(currentToken) + 1

                    // Save new token id
                    mPresenter.saveSelectedBalance(listToken[layoutPosition - 1])

                    notifyItemChanged(layoutPosition)
                    notifyItemChanged(currentTokenIndex)
                }
            }
        }

        inner class MyProfileHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
    }
}