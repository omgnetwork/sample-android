package co.omisego.omgshop

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.Toast
import co.omisego.omgshop.models.Product
import kotlinx.android.synthetic.main.activity_product_list.*
import kotlinx.android.synthetic.main.viewholder_product.view.*

class ProductListActivity : AppCompatActivity() {
    private val productList = listOf(
            Product(R.drawable.ic_photo_24dp, "OmiseGO T-Shirt", "An amazing t-shirt!", "฿985.00"),
            Product(R.drawable.ic_photo_24dp, "OmiseGO Hoodie", "An amazing hoodie!", "฿480.00"),
            Product(R.drawable.ic_photo_24dp, "OmiseGO Hat", "An amazing hat!", "฿100.00")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_product_list_toolbar_title)

        val adapter = ProductListRecyclerAdapter(productList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> {
                Toast.makeText(this@ProductListActivity, "My Profile", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class ProductListRecyclerAdapter(private val productList: List<Product>) : RecyclerView.Adapter<ProductListRecyclerAdapter.ProductViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_product, parent, false)
            return ProductViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) = holder.bind(productList[position])
        override fun getItemCount(): Int = productList.size

        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val tvTitle = itemView.tvTitle
            private val tvDescription = itemView.tvDescription
            private val ivLogo = itemView.ivLogo
            private val btnPrice = itemView.btnPrice
            fun bind(model: Product) {
                with(model) {
                    tvTitle.text = title
                    tvDescription.text = description
                    ivLogo.setImageDrawable(getDrawable(image))
                    btnPrice.text = price
                }
            }
        }

    }
}
