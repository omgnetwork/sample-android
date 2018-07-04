package co.omisego.omgshop.pages.checkout

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/24/2017 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseContract
import co.omisego.omgshop.base.BaseDialogFragment
import co.omisego.omisego.extension.bd
import kotlinx.android.synthetic.main.dialog_redeem.*
import kotlinx.android.synthetic.main.dialog_redeem.view.*
import java.math.BigDecimal
import java.math.RoundingMode

class RedeemDialogFragment : BaseDialogFragment<RedeemDialogContract.View, BaseContract.BaseCaller, RedeemDialogContract.Presenter>(), RedeemDialogContract.View {

    override val mPresenter: RedeemDialogContract.Presenter by lazy { RedeemDialogPresenter() }
    private var mRedeemDialogListener: RedeemDialogListener? = null
    private val stepSize = 50.bd // default SeekBar step size
    private var totalTokenAmount: BigDecimal = 0.bd
    private var itemPrice: BigDecimal = 0.bd
    private lateinit var mCurrency: String

    companion object {
        fun newInstance(itemPrice: String, amount: String, currency: String = "OMG"): RedeemDialogFragment {
            val fragment = RedeemDialogFragment()
            val args = Bundle()
            args.putString("itemPrice", itemPrice)
            args.putString("amount", amount)
            args.putString("currency", currency)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            itemPrice = it.getString("itemPrice").toBigDecimal()
            totalTokenAmount = it.getString("amount").toBigDecimal()
            mCurrency = it.getString("currency", "OMG")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_redeem, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btnRedeem.setOnClickListener { mPresenter.handleClickRedeem() }
        view.btnCancel.setOnClickListener { dismiss() }

        view.tvDialogTitle.text = getString(R.string.dialog_redeem_title, mCurrency)
        view.tvDescription.text = getString(R.string.dialog_redeem_description, "$totalTokenAmount $mCurrency")
        view.tvRedeemAmount.text = getString(R.string.dialog_redeem_redeem_amount, "0 $mCurrency")
        view.tvTotalDiscount.text = getString(R.string.dialog_redeem_total_discount, "฿0")

        // Set max seekbar to item amount or token value
        val maxSeekBar = if (totalTokenAmount < itemPrice) {
            totalTokenAmount.divide(stepSize, RoundingMode.CEILING)
        } else {
            itemPrice.divide(stepSize, RoundingMode.CEILING)
        }

        seekbarAmount.max = maxSeekBar.toInt()

        seekbarAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, v: Int, b: Boolean) {
                when (v) {
                    seekbarAmount.max -> {
                        if (itemPrice < totalTokenAmount) {
                            mPresenter.redeemChanged(itemPrice, mCurrency)
                        } else {
                            mPresenter.redeemChanged(totalTokenAmount, mCurrency)
                        }
                    }
                    else -> mPresenter.redeemChanged(v.bd.multiply(stepSize), mCurrency)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    fun setRedeemDialogListener(redeemDialogListener: RedeemDialogListener) {
        this.mRedeemDialogListener = redeemDialogListener
    }

    override fun setTextRedeemAmount(redeem: String) {
        tvRedeemAmount.text = getString(R.string.dialog_redeem_redeem_amount, "$redeem $mCurrency")
    }

    override fun setTextDiscount(discount: String) {
        tvTotalDiscount.text = getString(R.string.dialog_redeem_total_discount, "฿$discount")
    }

    override fun sendDiscountToCheckoutPage(discount: Int) {
        mRedeemDialogListener?.onSetRedeem(discount)
        dismiss()
    }

    interface RedeemDialogListener {
        fun onSetRedeem(amount: Int)
    }
}