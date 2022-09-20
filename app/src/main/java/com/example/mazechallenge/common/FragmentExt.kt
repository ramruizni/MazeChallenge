package com.example.mazechallenge.common

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.mazechallenge.R
import com.example.mazechallenge.ui.MainActivity
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch


fun <T> Fragment.collectLifecycleFlow(flow: Flow<T>, collector: FlowCollector<T>) {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect(collector)
        }
    }
}

fun Fragment.onLoading(showProgressIndicator: Boolean) {
    if (requireActivity() is MainActivity) {
        (requireActivity() as MainActivity).showProgressIndicator(showProgressIndicator)
    }
}

fun Fragment.onError(message: String?) {
    message?.let { text ->
        Toasty.error(requireContext(), text, Toast.LENGTH_SHORT, true).show()
    }
}

fun Fragment.onInfo(message: String?) {
    message?.let { text ->
        Toasty.custom(
            requireContext(),
            text,
            R.drawable.ic_information,
            R.color.darkAccent,
            Toast.LENGTH_SHORT,
            true,
            true
        ).show()
    }
}