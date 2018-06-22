package co.omisego.omgshop.pages.products

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/28/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.thousandSeparator
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.checkout.CheckoutActivity
import co.omisego.omgshop.pages.products.caller.ProductListCallerContract
import co.omisego.omgshop.pages.profile.MyProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.viewholder_product.view.*

class ProductListActivity : BaseActivity<ProductListContract.View, ProductListCallerContract.Caller, ProductListContract.Presenter>(), ProductListContract.View {
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
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_product_list_toolbar_title)
        setViewLoading(viewLoading)

        adapter = ProductListRecyclerAdapter(productList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        mPresenter.caller?.loadProductList()
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
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProductList(items: List<Product.Get.Item>) {
        adapter.updateItem(items)
    }

    override fun showLoadProductFail(response: Error) {
        Toast.makeText(this, response.description, Toast.LENGTH_SHORT).show()
    }

    override fun showClickProductItem(item: Product.Get.Item) {
        val intent = Intent(this@ProductListActivity, CheckoutActivity::class.java)
        intent.putExtra(CheckoutActivity.INTENT_EXTRA_PRODUCT_ITEM, item)
        startActivity(intent)
    }

    inner class ProductListRecyclerAdapter(private var productList: MutableList<Product.Get.Item>) : RecyclerView.Adapter<ProductListRecyclerAdapter.ProductViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product, parent, false)
            return ProductViewHolder(itemView)
        }

        fun updateItem(productList: List<Product.Get.Item>) {
            this.productList.addAll(productList)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(productList[position])
        override fun getItemCount(): Int = productList.size

        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvTitle = itemView.tvTitle
            private val tvDescription = itemView.tvDescription
            private val ivLogo = itemView.ivLogo
            private val btnPrice = itemView.btnPrice

            @SuppressLint("SetTextI18n")
            fun bind(model: Product.Get.Item) {
                with(model) {
                    tvTitle.text = name
                    tvDescription.text = description
                    Glide.with(this@ProductListActivity)
                        .load(imageUrl)
                        .apply(RequestOptions().transforms(RoundedCorners(20)))
                        .into(ivLogo)
                    btnPrice.text = "฿${price.toDouble().thousandSeparator()}"
                    btnPrice.setOnClickListener { mPresenter.handleClickProductItem(id) }
                }
            }
        }
    }
}
