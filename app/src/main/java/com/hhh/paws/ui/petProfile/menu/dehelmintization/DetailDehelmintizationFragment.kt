package com.hhh.paws.ui.petProfile.menu.dehelmintization

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentDetailDehelmintizationBinding


class DetailDehelmintizationFragment : Fragment() {

    private var _binding: FragmentDetailDehelmintizationBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDehelmintizationBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}