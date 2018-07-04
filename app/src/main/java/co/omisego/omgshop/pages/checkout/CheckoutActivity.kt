package co.omisego.omgshop.pages.checkout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.extensions.readableAmount
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.checkout.caller.CheckoutCallerContract
import co.omisego.omisego.extension.bd
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.toolbar.*

class CheckoutActivity : BaseActivity<CheckoutContract.View, CheckoutCallerContract.Caller, CheckoutContract.Presenter>(), CheckoutContract.View {
    override val mPresenter: CheckoutContract.Presenter by lazy { CheckoutPresenter() }
    private lateinit var productItem: Product.Get.Item
    private lateinit var loadingDialog: ProgressDialog
    private var discount: Int = 0

    companion object {
        const val INTENT_EXTRA_PRODUCT_ITEM = "product_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initInstance()
    }

    private fun initInstance() {
        productItem = intent.getParcelableExtra(INTENT_EXTRA_PRODUCT_ITEM)
        setupToolbar()
        initLoadingDialog()
        btnRedeem.setOnClickListener { mPresenter.redeem() }
        btnPay.setOnClickListener {
            val params = mPresenter.createBuyRequestParams(discount.bd, productItem.id)
            mPresenter.caller?.buy(params)
        }

        mPresenter.prepareProductToShow(productItem)
        mPresenter.calculateTotalAmountToPay(productItem.price.toDouble(), 0.0)
        mPresenter.resolveRedeemButtonName()
        mPresenter.checkIfBalanceAvailable()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_checkout_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initLoadingDialog() {
        loadingDialog = ProgressDialog(this)
        loadingDialog.setTitle(R.string.activity_checkout_loading_dialog_title)
        loadingDialog.setMessage(getString(R.string.activity_checkout_loading_dialog_message) + " ${productItem.name}...")
        loadingDialog.setCancelable(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showProductDetail(imageUrl: String, productTitle: String, productPrice: String) {
        tvProductTitle.text = productTitle
        tvPrice.text = productPrice
        Glide.with(this)
            .load(imageUrl)
            .apply(RequestOptions().transforms(RoundedCorners(20)))
            .into(ivProductDetailLogo)
    }

    override fun setDiscount(discount: Int) {
        this.discount = discount
    }

    override fun showRedeemDialog() {
        val balance = mPresenter.getCurrentTokenBalance()
        val readableTokenAmount = balance.readableAmount()
        val dialog = RedeemDialogFragment.newInstance(
            "${productItem.price}",
            readableTokenAmount ?: "0",
            balance.token.symbol
        )
        dialog.setRedeemDialogListener(object : RedeemDialogFragment.RedeemDialogListener {
            override fun onConfirm(amount: Int) {
                mPresenter.calculateTotalAmountToPay(productItem.price.toDouble(), amount.toDouble())
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    override fun showSummary(subTotal: String, discount: String, total: String) {
        tvSubtotal.text = getString(R.string.activity_checkout_price_format, subTotal)
        tvDiscount.text = getString(R.string.activity_checkout_price_format, discount)
        tvTotal.text = getString(R.string.activity_checkout_price_format, total)
    }

    override fun showTokenRedeemButtonText(tokenSymbol: String) {
        btnRedeem.text = getString(R.string.activity_checkout_redeem_button, tokenSymbol)
    }

    override fun showTokenRedeemButtonNotAvailable() {
        btnRedeem.text = getString(R.string.activity_checkout_no_balance_available)
        btnRedeem.isEnabled = false
        btnRedeem.setTextColor(ContextCompat.getColor(this, R.color.colorGrayDarkerDarker))
    }

    override fun showBuyFailed(msg: String) {
        log(msg)
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun showBuySuccess() {
        val message = getString(R.string.activity_checkout_pay_success)
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        finish()
    }

    override fun showLoading() {
        loadingDialog.show()
    }

    override fun hideLoading() {
        loadingDialog.dismiss()
    }
}
