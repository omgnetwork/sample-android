package co.omisego.omgshop.pages.products

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.models.Error
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.checkout.CheckoutActivity
import co.omisego.omgshop.pages.profile.MyProfileActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.viewholder_product.view.*

class ProductListActivity : BaseActivity<ProductListContract.View, ProductListContract.Presenter>(), ProductListContract.View {
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

        adapter = ProductListRecyclerAdapter(productList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        mPresenter.loadProductList()
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

    override fun showProductList(response: Product.Get.Response) {
        adapter.updateItem(response.data)
    }

    override fun showLoadProductFail(response: Error) {
        // TODO
    }

    override fun showClickProductItem(item: Product.Get.Item) {
        startActivity(Intent(this@ProductListActivity, CheckoutActivity::class.java))
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
                    tvTitle.text = title
                    tvDescription.text = description
                    Glide.with(this@ProductListActivity)
                            .load(imageUrl)
                            .apply(RequestOptions().transforms(RoundedCorners(20)))
                            .into(ivLogo)
                    btnPrice.text = "฿${price.asThousandCommaFormat()}"
                    btnPrice.setOnClickListener { mPresenter.handleClickProductItem(id) }
                }
            }
        }

    }
}
