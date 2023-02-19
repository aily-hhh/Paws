package com.hhh.paws.ui.viewPager2Start

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentSecondBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val finishTV = mBinding.finishTV
        finishTV.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_viewPager2StartFragment_to_authFragment)
            viewPager2StartFinish()
        }
    }

    private fun viewPager2StartFinish() {
        val sharedPref = requireActivity().getSharedPreferences("viewPager2Start", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished", true)
        editor.apply()
    }
}