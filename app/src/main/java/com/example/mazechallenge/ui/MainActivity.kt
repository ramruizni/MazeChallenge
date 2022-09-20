package com.example.mazechallenge.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.example.mazechallenge.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // prevent multiple toasts from queuing
        Toasty.Config.getInstance().allowQueue(false).apply()
    }

    fun showProgressIndicator(isLoading: Boolean) {
        binding.rlLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}