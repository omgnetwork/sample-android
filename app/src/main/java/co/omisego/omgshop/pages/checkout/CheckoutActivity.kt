package co.omisego.omgshop.pages.checkout

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import co.omisego.omgshop.R
import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_product_list_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnRedeem.setOnClickListener {
            RedeemDialogFragment.newInstance().show(supportFragmentManager, "")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
