package co.omisego.omgshop.delegator

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 15/7/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

import android.view.View

interface ErrorViewHandler {
    fun showError(show: Boolean, content: View, error: View)
}

class ShowErrorViewDelegator : ErrorViewHandler {
    override fun showError(show: Boolean, content: View, error: View) {
        if (show) {
            content.visibility = View.GONE
            error.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            error.visibility = View.GONE
        }
    }
}
