package co.omisego.omgshop.pages.products

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.delegator.ErrorViewHandler
import co.omisego.omgshop.delegator.ShowErrorViewDelegator
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.checkout.CheckoutActivity
import co.omisego.omgshop.pages.products.caller.ProductListCallerContract
import co.omisego.omgshop.pages.products.listener.ProductListener
import co.omisego.omgshop.pages.profile.MyProfileActivity
import co.omisego.omgshop.pages.qrcode.TransactionRequestFlowActivity
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.view_data_not_available.*

class ProductListActivity(
    errorViewDelegator: ShowErrorViewDelegator = ShowErrorViewDelegator()
) : BaseActivity<ProductListContract.View, ProductListCallerContract.Caller, ProductListContract.Presenter>(),
    ProductListContract.View,
    ErrorViewHandler by errorViewDelegator {

    private lateinit var adapter: ProductListRecyclerAdapter
    override val mPresenter: ProductListContract.Presenter by lazy {
        ProductListPresenter()
    }
    private var productList: MutableList<Product.Get.Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        initInstance()
    }

    private fun initInstance() {
        toolbar.navigationIcon = null
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_product_list_toolbar_title)

        adapter = ProductListRecyclerAdapter()
        adapter.productListener = object : ProductListener {
            override fun onProductClick(id: String) {
                mPresenter.handleClickProductItem(id)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        mPresenter.caller?.loadProductList()

        btnReload.setOnClickListener {
            mPresenter.caller?.loadProductList()
            recyclerView.visibility = View.VISIBLE
            viewError.visibility = View.GONE
            adapter.state = ProductListState.Loading()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                startActivity(Intent(this, MyProfileActivity::class.java))
                return true
            }
            R.id.scan -> {
                startActivity(Intent(this, TransactionRequestFlowActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProductList(items: List<Product.Get.Item>) {
        adapter.state = ProductListState.Success(items, adapter)
    }

    override fun showLoadProductFail(response: Error) {
        showError(true, recyclerView, viewError)
        Toast.makeText(this, response.description, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        adapter.state = ProductListState.Loading()
    }

    override fun showClickProductItem(item: Product.Get.Item) {
        val intent = Intent(this@ProductListActivity, CheckoutActivity::class.java)
        intent.putExtra(CheckoutActivity.INTENT_EXTRA_PRODUCT_ITEM, item)
        startActivity(intent)
    }
}
