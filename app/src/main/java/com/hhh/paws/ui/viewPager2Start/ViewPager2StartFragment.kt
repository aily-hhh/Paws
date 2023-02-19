package com.hhh.paws.ui.viewPager2Start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentViewPager2StartBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewPager2StartFragment : Fragment() {

    private var _binding: FragmentViewPager2StartBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPager2StartBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager2Start = mBinding.viewPager2Start

        val fragmentList = arrayListOf<Fragment>(FirstFragment(), SecondFragment())
        val adapter = ViewPager2StartAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager2Start.adapter = adapter
    }
}