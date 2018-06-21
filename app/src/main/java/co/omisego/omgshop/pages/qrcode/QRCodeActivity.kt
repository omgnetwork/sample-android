package co.omisego.omgshop.pages.qrcode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import co.omisego.omgshop.R
import co.omisego.omgshop.pages.scan.ScanActivity
import kotlinx.android.synthetic.main.activity_qrcode.*

class QRCodeActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE_SCAN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        setupToolbar()

        btnScan.setOnClickListener {
            startActivityForResult(Intent(this, ScanActivity::class.java), REQUEST_CODE_SCAN)
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_qr_code_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_SCAN -> {
                    // do something
                    Log.d("test", "Finish Scan QR")
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
