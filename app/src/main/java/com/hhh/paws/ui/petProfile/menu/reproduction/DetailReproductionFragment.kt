package com.hhh.paws.ui.petProfile.menu.reproduction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.ReproductionViewModel
import com.hhh.paws.databinding.FragmentDetailReproductionBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailReproductionFragment : Fragment() {

    private var _binding: FragmentDetailReproductionBinding? = null
    private val mBinding get() = _binding!!

    private var dateOfHeatDetail: TextInputEditText? = null
    private var dateOfMatingDetail: TextInputEditText? = null
    private var dateOfBirthDetail: TextInputEditText? = null
    private var numberOfTheLitterDetail: TextInputEditText? = null

    private val viewModelReproduction by viewModels<ReproductionViewModel>()
    private val bundleArgs by navArgs<DetailReproductionFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailReproductionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateOfHeatDetail = mBinding.dateOfHeatDetail
        dateOfMatingDetail = mBinding.dateOfMatingDetail
        dateOfBirthDetail = mBinding.dateOfBirthDetail
        numberOfTheLitterDetail = mBinding.numberOfTheLitterDetail

    }
}