package co.omisego.omgshop.pages.qrcode

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import co.omisego.omgshop.R
import co.omisego.omgshop.extensions.requestPermission
import co.omisego.omgshop.pages.scan.ScanActivity
import co.omisego.omisego.model.transaction.request.TransactionRequest
import kotlinx.android.synthetic.main.activity_qrcode.*

class QRCodeActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_CODE_SCAN = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)
        setupToolbar()

        btnScan.setOnClickListener {
            CameraPermission.handle(
                this,
                this::handleShowPermissionRationale,
                this::handlePermissionGranted,
                this::handlePermissionShouldManuallyGranted)
        }
    }

    private fun handleShowPermissionRationale() {
        Snackbar.make(rootLayout, R.string.permission_camera_rationale, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.ok) {
                requestPermission(Manifest.permission.CAMERA, CameraPermission.REQUEST_PERMISSION_CAMERA_ID)
            }
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CameraPermission.REQUEST_PERMISSION_CAMERA_ID -> {
                handlePermissionGranted()
            }
        }
    }

    private fun handlePermissionGranted() {
        setLoading(true)
        startActivityForResult(Intent(this, ScanActivity::class.java), REQUEST_CODE_SCAN)
    }

    private fun handlePermissionShouldManuallyGranted() {
        Snackbar.make(rootLayout, R.string.permission_should_manually_granted, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.ok) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .show()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_qr_code_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        setLoading(false)
    }

    private fun setLoading(isLoad: Boolean) {
        if (isLoad) {
            progressBar.visibility = View.VISIBLE
            btnScan.isEnabled = false
            btnGenerate.isEnabled = false
            divider.alpha = 0.1f
        } else {
            progressBar.visibility = View.GONE
            btnScan.isEnabled = true
            btnGenerate.isEnabled = true
            divider.alpha = 1f
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SCAN -> {
                    val transactionRequest = data?.getParcelableExtra<TransactionRequest>(ScanActivity.ACTIVITY_RESULT_TRANSACTION_REQUEST)
                    Log.d("test", transactionRequest?.toString())
                    // TODO: Go to consume transaction
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
