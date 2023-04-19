package com.hhh.paws.ui.newPet

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Pet
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.FragmentNewPetBinding
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewPetFragment : Fragment() {

    private var _binding: FragmentNewPetBinding? = null
    private val mBinding get() = _binding!!

    private var petNameNew: TextInputEditText? = null
    private var petSpeciesNew: TextInputEditText? = null
    private var spinnerSexNew: TextInputEditText? = null
    private var buttonCreate: Button? = null
    private var buttonCancel: Button? = null

    private var pet: Pet? = null

    private val viewModelPet by viewModels<PetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPetBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelPet.addNewPet.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    toast(getString(it.data))
                    val bundle = bundleOf("pet" to pet?.name)
                    Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                        .navigate(R.id.action_newPetFragment_to_petProfileActivity, bundle)
                }
                is UiState.Failure -> {
                    Log.e("UI State", it.error.toString())
                }
            }
        }

        petNameNew = mBinding.petNameNew
        petSpeciesNew = mBinding.petSpeciesNew
        spinnerSexNew = mBinding.spinnerSexNew

        buttonCreate = mBinding.buttonCreate
        buttonCreate?.setOnClickListener{
            if (petNameNew?.text.toString().trim().isNullOrEmpty()) {
                petNameNew?.requestFocus()
                petNameNew?.error = getString(R.string.errorNewPet)
            } else {
                pet = Pet(
                    petNameNew?.text.toString().trim(),
                    petSpeciesNew?.text.toString().trim(),
                    "",
                    spinnerSexNew?.text.toString().trim(),
                    "",
                    "",
                    ""
                )
                viewModelPet.newPet(pet!!)
            }
        }

        buttonCancel = mBinding.buttonCancel
        buttonCancel?.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_newPetFragment_to_mainFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}