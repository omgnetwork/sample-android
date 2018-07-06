package co.omisego.omgshop.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import co.omisego.omgshop.R
import kotlinx.android.synthetic.main.layout_switch_field.view.*

class ToggleField : ConstraintLayout {
    private var toggleView: Switch? = null
    private var tvValue: TextView? = null
    private var showText: Boolean = false
        set(value) {
            field = value
            if (value) {
                tvValue?.visibility = View.VISIBLE
            } else {
                tvValue?.visibility = View.INVISIBLE
            }
        }
    var title: String = "Title"
        set(value) {
            field = value
            rootView.tvTitle.text = value
        }
    var subTitle: String = ""
        set(value) {
            field = value
            rootView.tvSubTitle.text = value
        }
    var textOn: String = "On"
        set(value) {
            field = value
            tvValue?.text = value
        }
    var textOff: String = "Off"
        set(value) {
            field = value
            tvValue?.text = value
        }
    var value: Boolean = false
        set(value) {
            field = value
            toggleView?.isChecked = value
            if (value) {
                tvValue?.text = textOn
            } else {
                tvValue?.text = textOff
            }
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
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        inflate(context, R.layout.layout_switch_field, this)
        toggleView = rootView.findViewById(R.id.toggle)
        tvValue = rootView.findViewById(R.id.tvValue)
        attrs?.apply()
        bindOnSwitchChangeListener()
    }

    private fun bindOnSwitchChangeListener() {
        toggleView?.setOnCheckedChangeListener { _, isChecked ->
            value = isChecked
        }
    }

    private fun AttributeSet?.apply() {
        val attrs = context.theme.obtainStyledAttributes(
            this,
            R.styleable.ToggleField,
            0, 0)

        try {
            title = attrs.getString(R.styleable.ToggleField_toggleTitle) ?: title
            textOn = attrs.getString(R.styleable.ToggleField_textOn) ?: textOn
            textOff = attrs.getString(R.styleable.ToggleField_textOff) ?: textOff
            subTitle = attrs.getString(R.styleable.ToggleField_toggleSubTitle) ?: subTitle
            showText = attrs.getBoolean(R.styleable.ToggleField_showText, false)
        } finally {
            attrs.recycle()
        }
    }
}
