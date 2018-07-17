package co.omisego.omgshop.pages.profile

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.transition.TransitionManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.pages.history.TransactionHistoryActivity
import co.omisego.omgshop.pages.login.LoginActivity
import co.omisego.omgshop.pages.profile.caller.MyProfileCallerContract
import co.omisego.omgshop.pages.profile.viewholder.BalanceListener
import co.omisego.omisego.model.Balance
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_data_not_available.view.*

@Suppress("OVERRIDE_BY_INLINE")
class MyProfileActivity : BaseActivity<MyProfileContract.View, MyProfileCallerContract.Caller, MyProfileContract.Presenter>(),
        MyProfileContract.View,
        BalanceListener {

    override val mPresenter: MyProfileContract.Presenter by lazy {
        MyProfilePresenter()
    }
    private lateinit var myProfileBalanceAdapter: MyProfileBalanceAdapter
    private lateinit var mLoadingDialog: ProgressDialog
    private var mBalanceList: List<Balance> = listOf()
    private var mSelectedBalance: Balance? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_my_profile_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mLoadingDialog = ProgressDialog(this)
        mLoadingDialog.setTitle(R.string.activity_my_profile_dialog_title)
        mLoadingDialog.setMessage(getString(R.string.activity_my_profile_dialog_message))
        mLoadingDialog.setCancelable(false)

        myProfileBalanceAdapter = MyProfileBalanceAdapter()
        recyclerView.adapter = myProfileBalanceAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        tvLogout.setOnClickListener { mPresenter.caller?.logout() }

        mPresenter.caller?.loadWallets()

        viewError.btnReload.setOnClickListener {
            mPresenter.caller?.loadWallets()
            recyclerView.visibility = View.VISIBLE
            viewError.visibility = View.GONE
            myProfileBalanceAdapter.state = MyBalanceListState.Loading()
        }
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

    override fun setSelectedBalance(balance: Balance) {
        mSelectedBalance = balance
    }

    override fun showBalances(listBalance: List<Balance>, selectedBalance: Balance) {
        mBalanceList = listBalance
        myProfileBalanceAdapter.state = MyBalanceListState.MyBalance(
                listBalance.map { it to (it.token.id == selectedBalance.token.id) }.toMutableList(),
                myProfileBalanceAdapter
        ).also { it.balanceListener = this }
    }

    override fun showLoadBalanceFailed() {
        TransitionManager.beginDelayedTransition(viewContainer)
        viewError.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    override fun save(balance: Balance) {
        mPresenter.saveSelectedBalance(balance)
    }

    override fun showLogout() {
        val intent = Intent(this, LoginActivity::class.java)
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
}
