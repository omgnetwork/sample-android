package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.pages.history.TransactionHistoryActivity
import co.omisego.omgshop.pages.login.LoginActivity
import co.omisego.omgshop.pages.profile.caller.MyProfileCallerContract
import co.omisego.omisego.model.Balance
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.viewholder_content_my_profile.view.*

class MyProfileActivity : BaseActivity<MyProfileContract.View, MyProfileCallerContract.Caller, MyProfileContract.Presenter>(), MyProfileContract.View {

    override val mPresenter: MyProfileContract.Presenter by lazy {
        MyProfilePresenter()
    }
    private lateinit var myProfileContentAdapter: MyProfileContentAdapter
    private var mCurrentSelectedTokenId: String = ""
    private lateinit var mLoadingDialog: ProgressDialog

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

        mLoadingDialog = ProgressDialog(this)
        mLoadingDialog.setTitle(R.string.activity_my_profile_dialog_title)
        mLoadingDialog.setMessage(getString(R.string.activity_my_profile_dialog_message))
        mLoadingDialog.setCancelable(false)

        myProfileContentAdapter = MyProfileContentAdapter(mutableListOf())
        recyclerView.adapter = myProfileContentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        tvLogout.setOnClickListener { mPresenter.caller?.logout() }

        mPresenter.caller?.loadWallets()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_my_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when (item.itemId) {
                android.R.id.home -> finish()
                R.id.history -> startActivity(Intent(this, TransactionHistoryActivity::class.java))
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

    override fun showLoadingDialog() {
        mLoadingDialog.show()
    }

    override fun hideLoadingDialog() {
        mLoadingDialog.dismiss()
    }

    inner class MyProfileContentAdapter(private var listToken: MutableList<Balance>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val TYPE_HEADER = 0
        private val TYPE_CONTENT = 1

        fun updateListToken(listToken: MutableList<Balance>) {
            this.listToken = listToken
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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
                tvToken.text = token.token.symbol

                val drawable = when (mCurrentSelectedTokenId) {
                    token.token.id -> ContextCompat.getDrawable(itemView.context, R.drawable.ic_check_24dp)
                    else -> null
                }
                ivSelected.setImageDrawable(drawable)

                // bind token click listener
                layoutContainer.setOnClickListener {
                    val currentToken = listToken.firstOrNull { it.token.id == mCurrentSelectedTokenId } ?: listToken[0]
                    val currentTokenIndex = listToken.indexOf(currentToken) + 1

                    // Save new token id
                    mPresenter.saveSelectedToken(listToken[layoutPosition - 1])

                    notifyItemChanged(layoutPosition)
                    notifyItemChanged(currentTokenIndex)
                }
            }
        }

        inner class MyProfileHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }
}
