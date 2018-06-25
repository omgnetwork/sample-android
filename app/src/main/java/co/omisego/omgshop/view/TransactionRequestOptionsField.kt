package co.omisego.omgshop.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.transition.ChangeBounds
import android.support.transition.TransitionManager
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import co.omisego.omgshop.R
import kotlinx.android.synthetic.main.layout_generate_transaction_optional.view.*

class TransactionRequestOptionsField : ConstraintLayout {

    private val groupSizeMin by lazy {
        parentGroup.measuredHeight
    }
    private val groupSizeMax by lazy {
        parentGroup.measuredHeight
    }

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        inflate(context, R.layout.layout_generate_transaction_optional, this)
        val transition = ChangeBounds()
        transition.interpolator = AccelerateDecelerateInterpolator()
        transition.duration = 600
        rootView.ivToggle.setOnClickListener {
            val parentLayout = parent
            if (parentLayout is ViewGroup) {
                TransitionManager.beginDelayedTransition(parentLayout, transition)
            }
            if (rootView.fieldGroup.visibility == View.GONE) {
                rootView.fieldGroup.visibility = View.VISIBLE
                rootView.ivToggle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_24dp))
            } else {
                rootView.fieldGroup.visibility = View.GONE
                rootView.ivToggle.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_24dp))
            }
        }
    }
}
