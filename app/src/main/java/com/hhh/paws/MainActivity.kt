package com.hhh.paws

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.replace
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.hhh.paws.databinding.ActivityMainBinding
import com.hhh.paws.ui.auth.AuthFragment
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
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.fragment_start)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            setContentView(mBinding.root)
            Navigation.findNavController(this@MainActivity, R.id.navHostFragment)
                .navigate(R.id.action_startFragment_to_authFragment)
        }
    }
}