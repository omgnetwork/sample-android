package co.omisego.omgshop.pages.scan

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import co.omisego.omgshop.R
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.network.ClientProvider
import co.omisego.omisego.model.APIError
import co.omisego.omisego.model.OMGResponse
import co.omisego.omisego.model.transaction.request.TransactionRequest
import co.omisego.omisego.qrcode.scanner.OMGQRScannerContract
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity(), OMGQRScannerContract.Callback {
    private val omgAPIClient by lazy {
        ClientProvider.provideOMGClient(Preference.loadCredential().omisegoAuthenticationToken).client
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        scannerView.startCamera(omgAPIClient, this)
    }

    override fun scannerDidCancel(view: OMGQRScannerContract.View) {
        // Do something
    }

    override fun scannerDidDecode(view: OMGQRScannerContract.View, transactionRequest: OMGResponse<TransactionRequest>) {
        // Do something
    }

    override fun scannerDidFailToDecode(view: OMGQRScannerContract.View, exception: OMGResponse<APIError>) {
        // Do something
    }

    override fun onPause() {
        super.onPause()
        scannerView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView.startCamera(omgAPIClient, this)
    }

}
