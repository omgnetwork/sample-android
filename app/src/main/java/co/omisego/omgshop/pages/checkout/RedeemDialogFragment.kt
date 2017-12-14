package co.omisego.omgshop.pages.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import co.omisego.omgshop.R
import co.omisego.omgshop.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_redeem.*
import kotlinx.android.synthetic.main.dialog_redeem.view.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/24/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class RedeemDialogFragment : BaseDialogFragment<RedeemDialogContract.View, RedeemDialogContract.Presenter>(), RedeemDialogContract.View {

    override val mPresenter: RedeemDialogContract.Presenter by lazy { RedeemDialogPresenter() }
    private var mRedeemDialogListener: RedeemDialogListener? = null
    private val STEP_SIZE = 50.0 // default SeekBar step size
    private var mAmount: Int = 0
    private var mItemPrice: Int = 0
    private lateinit var mCurrency: String

    companion object {
        fun newInstance(itemPrice: Int = 1000, amount: Int = 10, currency: String = "OMG"): RedeemDialogFragment {
            val fragment = RedeemDialogFragment()
            val args = Bundle()
            args.putInt("itemPrice", itemPrice)
            args.putInt("amount", amount)
            args.putString("currency", currency)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mItemPrice = it.getInt("itemPrice", 0)
            mAmount = it.getInt("amount", 0)
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

        view.tvDialogTitle.text = getString(R.string.dialog_redeem_title).replace("#symbol", mCurrency)
        view.tvDescription.text = getString(R.string.dialog_redeem_description).replace("#amount", "$mAmount $mCurrency")
        view.tvRedeemAmount.text = getString(R.string.dialog_redeem_redeem_amount).replace("#amount", "0 $mCurrency")
        view.tvTotalDiscount.text = getString(R.string.dialog_redeem_total_discount).replace("#amount", "฿0")

        val maxSeekBar = if (mAmount < mItemPrice) mAmount / STEP_SIZE else mItemPrice / STEP_SIZE
        seekbarAmount.max = Math.ceil(maxSeekBar).toInt()

        seekbarAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, v: Int, b: Boolean) {
                when (v) {
                    seekbarAmount.max -> {
                        if (mItemPrice < mAmount) {
                            mPresenter.redeemChanged(mItemPrice, mCurrency)
                        } else {
                            mPresenter.redeemChanged(mAmount, mCurrency)
                        }
                    }
                    else -> mPresenter.redeemChanged(v * STEP_SIZE.toInt(), mCurrency)
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
        tvRedeemAmount.text = getString(R.string.dialog_redeem_redeem_amount).replace("#amount", "$redeem $mCurrency")
    }

    override fun setTextDiscount(discount: String) {
        tvTotalDiscount.text = getString(R.string.dialog_redeem_total_discount).replace("#amount", "฿$discount")
    }

    override fun sendDiscountToCheckoutPage(discount: Int) {
        mRedeemDialogListener?.onSetRedeem(discount)
        dismiss()
    }

    interface RedeemDialogListener {
        fun onSetRedeem(amount: Int)
    }
}