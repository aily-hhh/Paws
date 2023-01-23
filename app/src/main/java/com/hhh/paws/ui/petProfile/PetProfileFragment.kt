package com.hhh.paws.ui.petProfile

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Pet
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.FragmentPetProfileBinding
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class PetProfileFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentPetProfileBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var petSpecies: TextInputEditText
    private lateinit var petBreed: TextInputEditText
    private lateinit var petBirthday: TextInputEditText
    private lateinit var petHair: TextInputEditText
    private lateinit var spinnerSex: AppCompatSpinner
    private lateinit var buttonUpdate: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonDelete: Button
    private lateinit var toolbarLayoutPet: CollapsingToolbarLayout

    private lateinit var petPhoto: ImageView
    private lateinit var changeImagePetFab: FloatingActionButton

    private val viewModelPet by viewModels<PetViewModel>()
    private val bundleArgs: PetProfileFragmentArgs by navArgs()

    private lateinit var petNameThis: String
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPetProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petNameThis = bundleArgs.pet

        toolbarLayoutPet = mBinding.toolbarLayoutPet
        toolbarLayoutPet.title = petNameThis

        petSpecies = mBinding.root.findViewById(R.id.petSpecies)
        petBreed = mBinding.root.findViewById(R.id.petBreed)
        petHair = mBinding.root.findViewById(R.id.petHair)

        petBirthday = mBinding.root.findViewById(R.id.petBirthday)
        petBirthday.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        spinnerSex = mBinding.root.findViewById(R.id.spinnerSex)
        val adapterSex = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sexArray,
            android.R.layout.simple_spinner_item
        )
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSex.adapter = adapterSex

        buttonUpdate = mBinding.root.findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val pet = Pet(
                petNameThis,
                petSpecies.text.toString(),
                petBreed.text.toString(),
                spinnerSex.selectedItem.toString(),
                petBirthday.text.toString(),
                petHair.text.toString()
            )
            viewModelPet.updatePet(pet)
        }
        viewModelPet.update.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    toast(it.data)
                }
                is UiState.Failure -> {
                    Log.e("UI State", it.error.toString())
                }
            }
        }

        buttonBack = mBinding.root.findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        buttonDelete = mBinding.root.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            // диалог с уточнением
            viewModelPet.deletePet(petNameThis)
            Navigation.findNavController(requireActivity(), R.id.action_petProfileFragment_to_mainFragment)
        }
        viewModelPet.delete.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    toast(it.data)
                }
                is UiState.Failure -> {
                    Log.e("UI State", it.error.toString())
                }
            }
        }

        petPhoto = mBinding.petPhoto
        changeImagePetFab = mBinding.changeImagePetFab

        viewModelPet.getPet(petNameThis)
        viewModelPet.pet.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    Log.d("UI State", "$it")
                    petSpecies.setText(it.data.species)
                    petBreed.setText(it.data.breed)
                    petBirthday.setText(it.data.birthday)
                    petHair.setText(it.data.hair)
                    val pos = adapterSex.getPosition(it.data.sex)
                    spinnerSex.setSelection(pos)
                }
                is UiState.Failure -> {
                    Log.d("UI State", it.error.toString())
                }
            }
        }
    }

    private fun getDateCalendar() {
        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        petBirthday.setText("$savedDay.$savedMonth.$savedYear")
    }
}