package co.omisego.omgshop.custom

import android.text.Editable
import android.text.TextWatcher


/**
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 30/11/2017 AD.
 * Copyright © 2017 OmiseGO. All rights reserved.
 */

class MinimalTextChangeListener(private val afterTextChangedAction: (editable: Editable) -> Unit) : TextWatcher {
    override fun afterTextChanged(editable: Editable) = afterTextChangedAction.invoke(editable)
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
}