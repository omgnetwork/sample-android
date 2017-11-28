package co.omisego.omgshop.pages.checkout

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.*
import android.widget.SeekBar
import android.widget.Toast
import co.omisego.omgshop.R
import kotlinx.android.synthetic.main.dialog_redeem.*
import kotlinx.android.synthetic.main.dialog_redeem.view.*


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 11/24/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class RedeemDialogFragment : DialogFragment() {

    private var mAmount: Int = 0
    private lateinit var mCurrency: String

    companion object {
        fun newInstance(amount: Int = 10, currency: String = "OMG"): RedeemDialogFragment {
            val fragment = RedeemDialogFragment()
            val args = Bundle()
            args.putInt("amount", amount)
            args.putString("currency", currency)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mAmount = it.getInt("amount", 0)
            mCurrency = it.getString("currency", "OMG")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_redeem, container)
    }

    override fun onResume() {
        super.onResume()
        val size = Point()
        val window = dialog.window
        val display = window.windowManager.defaultDisplay
        display.getSize(size)
        window.setLayout((size.x * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)
        window.setGravity(Gravity.CENTER)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.btnRedeem.setOnClickListener {
            Toast.makeText(this@RedeemDialogFragment.context, "Redeem", Toast.LENGTH_SHORT).show()
            dismiss()
        }
        view.btnCancel.setOnClickListener { dismiss() }

        view.tvDescription.text = getString(R.string.dialog_redeem_description).replace("#amount", "$mAmount $mCurrency")
        view.tvRedeemAmount.text = getString(R.string.dialog_redeem_redeem_amount).replace("#amount", "0 $mCurrency")
        view.tvTotalDiscount.text = getString(R.string.dialog_redeem_total_discount).replace("#amount", "฿0")

        seekbarAmount.max = mAmount
        seekbarAmount.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, v: Int, b: Boolean) {
                tvRedeemAmount.text = getString(R.string.dialog_redeem_redeem_amount).replace("#amount", "$v $mCurrency")
                view.tvTotalDiscount.text = getString(R.string.dialog_redeem_total_discount).replace("#amount", "฿$v")
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }
}