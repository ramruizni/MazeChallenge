package com.example.mazechallenge.common

import android.text.Editable
import android.text.TextWatcher
import java.util.*

class TextWatcherDelayed(
    private val onTextChanged: (query: CharSequence?) -> Unit,
    private val afterTextChanged: () -> Unit,
    private val delayMillis: Long = 500
) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    var timer = Timer()

    override fun onTextChanged(query: CharSequence?, start: Int, before: Int, count: Int) {
        onTextChanged.invoke(query)

        timer.cancel()
        timer.purge()
    }

    override fun afterTextChanged(editable: Editable?) {
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                afterTextChanged.invoke()
            }
        }, delayMillis)
    }
}