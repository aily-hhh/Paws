package com.hhh.paws.ui.newPet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Pet
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.FragmentNewPetBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewPetFragment : Fragment() {

    private var _binding: FragmentNewPetBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var petNameNew: TextInputEditText
    private lateinit var petSpeciesNew: TextInputEditText
    private lateinit var petBreedNew: TextInputEditText
    private lateinit var petBirthdayNew: TextInputEditText
    private lateinit var petHairNew: TextInputEditText
    private lateinit var spinnerSexNew: Spinner
    private lateinit var buttonCreate: Button
    private lateinit var buttonCancel: Button

    private val viewModelPet by viewModels<PetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPetBinding.inflate(inflater, container, false)

        petNameNew = mBinding.petNameNew
        petSpeciesNew = mBinding.petSpeciesNew
        petBreedNew = mBinding.petBreedNew
        petBirthdayNew = mBinding.petBirthdayNew
        petHairNew = mBinding.petHairNew
        spinnerSexNew = mBinding.spinnerSexNew

        buttonCreate = mBinding.buttonCreate
        buttonCreate.setOnClickListener{
            val pet = Pet(
                petNameNew.text.toString().trim(),
                petSpeciesNew.text.toString().trim(),
                petBreedNew.text.toString().trim(),
                "girl",
                petBirthdayNew.text.toString().trim(),
                petHairNew.text.toString().trim()
            )
            viewModelPet.newPet(pet)
            val bundle = bundleOf("pet" to pet.name)
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_newPetFragment_to_petProfileFragment, bundle)
        }

        buttonCancel = mBinding.buttonCancel
        buttonCancel.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_newPetFragment_to_mainFragment)
        }

        return mBinding.root
    }
}