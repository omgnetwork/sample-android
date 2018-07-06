package co.omisego.omgshop.custom

import android.widget.SeekBar

/*
 * OmiseGO
 *
 * Created by Phuchit Sirimongkolsathien on 24/6/2018 AD.
 * Copyright © 2017-2018 OmiseGO. All rights reserved.
 */

class MinimalSeekBarChangeListener(private val onChange: (progress: Int) -> Unit) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (fromUser)
            onChange(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}