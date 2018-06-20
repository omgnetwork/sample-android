package co.omisego.omgshop.pages.checkout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 4/12/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.app.ProgressDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseActivity
import co.omisego.omgshop.helpers.Preference
import co.omisego.omgshop.models.Product
import co.omisego.omgshop.pages.checkout.caller.CheckoutCallerContract
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.activity_checkout.*
import java.math.BigDecimal

class CheckoutActivity : BaseActivity<CheckoutContract.View, CheckoutCallerContract.Caller, CheckoutContract.Presenter>(), CheckoutContract.View {
    override val mPresenter: CheckoutContract.Presenter by lazy { CheckoutPresenter() }
    private lateinit var mProductItem: Product.Get.Item
    private var mDiscount: Int = 0
    private lateinit var mLoadingDialog: ProgressDialog

    companion object {
        const val INTENT_EXTRA_PRODUCT_ITEM = "product_item"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        initInstance()
    }

    private fun initInstance() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.activity_product_list_toolbar_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mProductItem = intent.getParcelableExtra(INTENT_EXTRA_PRODUCT_ITEM)
        mDiscount = mProductItem.price

        mLoadingDialog = ProgressDialog(this)
        mLoadingDialog.setTitle(R.string.activity_checkout_loading_dialog_title)
        mLoadingDialog.setMessage(getString(R.string.activity_checkout_loading_dialog_message) + " ${mProductItem.name}...")
        mLoadingDialog.setCancelable(false)

        btnRedeem.setOnClickListener {
            mPresenter.redeem()
        }

        btnPay.setOnClickListener {
            val subUnitToUnit = mPresenter.getCurrentTokenBalance().token.subunitToUnit
            val tokenId = Preference.loadSelectedTokenBalance()?.token?.id ?: ""
            val tokenValue = subUnitToUnit.multiply(BigDecimal.valueOf(mDiscount.toDouble()))
            val productId = mProductItem.id
            mPresenter.caller?.buy(Product.Buy.Request(tokenId, tokenValue, productId))
        }

        log(mProductItem.toString())
        mPresenter.handleProductDetail(mProductItem)
        mPresenter.calculateTotal(mProductItem.price.toDouble(), 0.0)
        mPresenter.resolveRedeemButtonName()
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
        mDiscount = discount
    }

    override fun showRedeemDialog() {
        val currentBalance = mPresenter.getCurrentTokenBalance()
        val balanceAmount = currentBalance.amount.divide(currentBalance.token.subunitToUnit)
        val dialog = RedeemDialogFragment.newInstance(mProductItem.price, balanceAmount.toBigInteger().toInt(), currentBalance.token.symbol)
        dialog.setRedeemDialogListener(object : RedeemDialogFragment.RedeemDialogListener {
            override fun onSetRedeem(amount: Int) {
                log(amount.toString())
                mPresenter.calculateTotal(mProductItem.price.toDouble(), amount.toDouble())
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    override fun showSummary(subTotal: String, discount: String, total: String) {
        tvSubtotal.text = getString(R.string.activity_checkout_price_format, subTotal)
        tvDiscount.text = getString(R.string.activity_checkout_price_format, discount)
        tvTotal.text = getString(R.string.activity_checkout_price_format, total)
    }

    override fun showRedeemButton(tokenSymbol: String) {
        btnRedeem.text = getString(R.string.activity_checkout_redeem_button, tokenSymbol)
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
        mLoadingDialog.show()
    }

    override fun hideLoading() {
        mLoadingDialog.dismiss()
    }
}
