package com.hhh.paws.ui.start

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStartBinding.inflate(inflater, container, false)

        CoroutineScope(Dispatchers.Main).launch {
            delay(1500)
            if (viewPager2StartFinish()) {
                Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(R.id.action_startFragment_to_authFragment)
            } else {
                Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(R.id.action_startFragment_to_viewPager2StartFragment)
            }
        }

        return mBinding.root
    }

    private fun viewPager2StartFinish(): Boolean {
        val sharedPref = requireActivity().getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    override fun onResume() {
        super.onResume()
        if (viewPager2StartFinish()) {
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_startFragment_to_authFragment)
        } else {
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_startFragment_to_viewPager2StartFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}