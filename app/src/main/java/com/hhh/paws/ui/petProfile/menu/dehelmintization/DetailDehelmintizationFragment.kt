package com.hhh.paws.ui.petProfile.menu.dehelmintization

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.viewModel.DehelmintizationViewModel
import com.hhh.paws.databinding.FragmentDetailDehelmintizationBinding
import com.hhh.paws.util.UiState
import java.util.*


class DetailDehelmintizationFragment : Fragment() {

    private var _binding: FragmentDetailDehelmintizationBinding? = null
    private val mBinding get() = _binding!!

    private var nameDehelmintizationDetail: TextInputEditText? = null
    private var manufacturerDehelmintizationDetail: TextInputEditText? = null
    private var doseDehelmintizationDetail: TextInputEditText? = null
    private var dateDehelmintizationDetail: TextInputEditText? = null
    private var timeDehelmintizationDetail: TextInputEditText? = null
    private var veterinarianDehelmintizationDetail: TextInputEditText? = null
    private var descriptionDehelmintizationDetail: TextInputEditText? = null

    private var dehelmintizationThis: Dehelmintization? = null
    private lateinit var petName: String

    private val viewModelDehelmintization by viewModels<DehelmintizationViewModel>()
    private val bundleArgs by navArgs<DetailDehelmintizationFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailDehelmintizationBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petName = "Котик"
        dehelmintizationThis = bundleArgs.dehelmintization

        nameDehelmintizationDetail = mBinding.nameDehelmintizationDetail
        manufacturerDehelmintizationDetail = mBinding.manufacturerDehelmintizationDetail
        doseDehelmintizationDetail = mBinding.doseDehelmintizationDetail
        dateDehelmintizationDetail = mBinding.dateDehelmintizationDetail
        timeDehelmintizationDetail = mBinding.timeDehelmintizationDetail
        veterinarianDehelmintizationDetail = mBinding.veterinarianDehelmintizationDetail
        descriptionDehelmintizationDetail = mBinding.descriptionDehelmintizationDetail

        if (dehelmintizationThis != null) {
            nameDehelmintizationDetail!!.setText(dehelmintizationThis!!.name)
            manufacturerDehelmintizationDetail!!.setText(dehelmintizationThis!!.manufacturer)
            doseDehelmintizationDetail!!.setText(dehelmintizationThis!!.dose)
            dateDehelmintizationDetail!!.setText(dehelmintizationThis!!.date)
            timeDehelmintizationDetail!!.setText(dehelmintizationThis!!.time)
            veterinarianDehelmintizationDetail!!.setText(dehelmintizationThis!!.veterinarian)
            descriptionDehelmintizationDetail!!.setText(dehelmintizationThis!!.description)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newDehelmintization = Dehelmintization()
                if (dehelmintizationThis != null) {
                    newDehelmintization.id = dehelmintizationThis!!.id
                } else {
                    newDehelmintization.id = UUID.randomUUID().toString()
                }



                viewModelDehelmintization.update.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Loading -> {

                        }
                        is UiState.Success -> {

                        }
                        is UiState.Failure -> {

                        }
                        else -> {

                        }
                    }
                }
                viewModelDehelmintization.updateDehelmintization(newDehelmintization, petName)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}