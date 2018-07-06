package co.omisego.omgshop.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TextView
import co.omisego.omgshop.R
import co.omisego.omgshop.custom.MinimalSeekBarChangeListener
import kotlinx.android.synthetic.main.layout_seekbar_field.view.*

class SeekBarField : ConstraintLayout {
    private val tvValue: TextView by lazy {
        rootView.findViewById(R.id.tvValue) as TextView
    }

    var title: String = "Title"
        set(value) {
            field = value
            rootView.tvTitle.text = value
        }
    var max: Int = 15
        set(value) {
            field = value
            rootView.seekBar.max = value * multiplier
        }
    var progress: Int = 0
        set(value) {
            field = value
            if (value == 0)
                tvValue.text = "∞"
            else
                tvValue.text = "${value * multiplier}"
        }

    var multiplier: Int = 1
        set(value) {
            field = value
            if (progress == 0)
                tvValue.text = "∞"
            else
                tvValue.text = "${value * progress}"
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
        inflate(context, R.layout.layout_seekbar_field, this)
        attrs?.apply()
        bindOnSeekListener()
    }

    private fun bindOnSeekListener() {
        rootView.seekBar.setPadding(24, 0, 24, 0)
        rootView.seekBar.setOnSeekBarChangeListener(MinimalSeekBarChangeListener { currentValue ->
            progress = currentValue
        })
    }

    private fun AttributeSet?.apply() {
        val attrs = context.theme.obtainStyledAttributes(
            this,
            R.styleable.SeekBarField,
            0, 0)

        try {
            max = attrs.getInteger(R.styleable.SeekBarField_max, max)
            progress = attrs.getInteger(R.styleable.SeekBarField_progress, progress)
            title = attrs.getString(R.styleable.SeekBarField_seekTitle) ?: title
            multiplier = attrs.getInt(R.styleable.SeekBarField_multiplier, 1)
        } finally {
            attrs.recycle()
        }
    }
}
