package com.hhh.paws

import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.hhh.paws.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.fragment_start)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            setContentView(mBinding.root)
            Navigation.findNavController(this@MainActivity, R.id.navHostFragment)
                .navigate(R.id.action_mainFragment_to_authFragment)
        }
    }
}