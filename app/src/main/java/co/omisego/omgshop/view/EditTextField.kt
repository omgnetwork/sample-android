package co.omisego.omgshop.view

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import co.omisego.omgshop.R
import com.santalu.maskedittext.MaskEditText

class EditTextField : ConstraintLayout {
    var editText: MaskEditText? = null
    private var tilEditText: TextInputLayout? = null
    private var editTextEnabled: Boolean = true
        set(value) {
            field = value
            editText?.isEnabled = value
        }
    private var editTextCounterEnabled: Boolean = false
        set(value) {
            field = value
            tilEditText?.isCounterEnabled = value
        }
    private var editTextCounterMaxLength: Int = 100
        set(value) {
            field = value
            tilEditText?.counterMaxLength = value
        }
    private var mask: String? = ""
        set(value) {
            if (value == null) return
            field = value
            editText?.text?.clear()
            editText?.setMask(value)
        }
    private var inputType: Int = EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
        set(value) {
            field = value
            editText?.text?.clear()
            editText?.inputType = value
        }
    private var tvTitle: TextView? = null
    var title: String = "Title"
        set(value) {
            field = value
            tvTitle?.text = value
        }

    var hint: String = ""
        set(value) {
            field = value
            editText?.hint = value
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
        val rootView = inflate(context, R.layout.layout_edit_text_field, this)
        tvTitle = rootView.findViewById(R.id.tvTitle)
        editText = rootView.findViewById(R.id.editText)

        if (this.isInEditMode) {

        } else {
            tilEditText = rootView.findViewById(R.id.tilEditText)
        }
        attrs?.apply()
        adjustPaddingIfNeeded()
    }

    private fun adjustPaddingIfNeeded() {
        if (tilEditText?.isCounterEnabled == true ||
            tilEditText?.isErrorEnabled == true ||
            tilEditText?.isHintEnabled == true) {
            val paddingTop = tilEditText?.getChildAt(0)?.paddingTop ?: 0
            val paddingLeft = tilEditText?.getChildAt(0)?.paddingLeft ?: 0
            val paddingRight = tilEditText?.getChildAt(0)?.paddingRight ?: 0
            tilEditText?.getChildAt(1)?.setPadding(paddingLeft, paddingTop, paddingRight, 0)
        }
    }

    fun getText(): String = editText?.text.toString()

    fun setError(error: Boolean) {
        tilEditText?.isErrorEnabled = error
    }

    fun setErrorText(text: String) {
        tilEditText?.error = text
    }

    private fun AttributeSet?.apply() {
        val attrs = context.theme.obtainStyledAttributes(
            this,
            R.styleable.EditTextField,
            0, 0)
        try {
            title = attrs.getString(R.styleable.EditTextField_editTextTitle) ?: title
            hint = attrs.getString(R.styleable.EditTextField_editTextHint) ?: hint
            inputType = attrs.getInt(R.styleable.EditTextField_android_inputType, EditorInfo.TYPE_NUMBER_FLAG_DECIMAL)
            mask = attrs.getString(R.styleable.EditTextField_editTextMask) ?: mask
            editTextCounterEnabled = attrs.getBoolean(R.styleable.EditTextField_editTextCounterEnabled, false)
            editTextEnabled = attrs.getBoolean(R.styleable.EditTextField_editTextEnabled, true)
            editTextCounterMaxLength = attrs.getInteger(R.styleable.EditTextField_editTextCounterMaxLength, 100)
        } finally {
            attrs.recycle()
        }
    }
}
