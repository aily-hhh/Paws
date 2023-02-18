package com.hhh.paws.ui.viewPager2Gallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentViewPager2GalleryBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewPager2GalleryFragment : Fragment() {

    private var _binding: FragmentViewPager2GalleryBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentViewPager2GalleryBinding.inflate(inflater, container, false)
        return mBinding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}