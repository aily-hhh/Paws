package com.hhh.paws.ui.petProfile.menu.vaccines

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Treatment
import com.hhh.paws.database.model.Vaccine
import com.hhh.paws.database.viewModel.VaccinesViewModel
import com.hhh.paws.databinding.FragmentDetailVaccineBinding
import com.hhh.paws.util.PetName
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailVaccineFragment : Fragment() {

    private var _binding: FragmentDetailVaccineBinding? = null
    private val mBinding get() = _binding!!

    private var typeVaccine: TextInputEditText? = null
    private var nameVaccine: TextInputEditText? = null
    private var manufacturerVaccine: TextInputEditText? = null
    private var dateVaccine: TextInputEditText? = null
    private var validUntilVaccine: TextInputEditText? = null
    private var veterinarianVaccine: TextInputEditText? = null
    private var progressBarVaccineDetail: ProgressBar? = null

    private var year: Int = 0;
    private var month: Int = 0;
    private var day: Int = 0;
    private var savedYear: Int = 0;
    private var savedMonth: Int = 0;
    private var savedDay: Int = 0;

    private val viewModelVaccines by viewModels<VaccinesViewModel>()
    private val bundleArgs by navArgs<DetailVaccineFragmentArgs>()

    private var petName: String? = null
    private var vaccineThis: Vaccine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailVaccineBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petName = PetName.name;
        vaccineThis = bundleArgs.vaccine

        typeVaccine = mBinding.typeVaccine
        nameVaccine = mBinding.nameVaccine
        manufacturerVaccine = mBinding.manufacturerVaccine
        veterinarianVaccine = mBinding.veterinarianVaccine

        dateVaccine = mBinding.dateVaccine
        dateVaccine?.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), dateVaccineListener, year, month, day).show()
        }

        validUntilVaccine = mBinding.validUntilVaccine
        validUntilVaccine?.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(),validUntilVaccineListener, year, month, day).show()
        }

        if (vaccineThis != null) {
            typeVaccine?.setText(vaccineThis?.type)
            nameVaccine?.setText(vaccineThis?.name)
            manufacturerVaccine?.setText(vaccineThis?.manufacturer)
            veterinarianVaccine?.setText(vaccineThis?.veterinarian)
            dateVaccine?.setText(vaccineThis?.dateOfVaccination)
            validUntilVaccine?.setText(vaccineThis?.validUntil)
        }

        viewModelVaccines.addVaccine.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarVaccineDetail?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarVaccineDetail?.visibility = View.INVISIBLE
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .popBackStack()
                }
                is UiState.Failure -> {
                    progressBarVaccineDetail?.visibility = View.INVISIBLE
                }
                else -> {
                    progressBarVaccineDetail?.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    private fun getDateCalendar() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    @SuppressLint("SetTextI18n")
    private var dateVaccineListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        dateVaccine!!.setText("$savedDay.$savedMonth.$savedYear")
    }
    @SuppressLint("SetTextI18n")
    private var validUntilVaccineListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        validUntilVaccine!!.setText("$savedDay.$savedMonth.$savedYear")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newVaccine = Vaccine()
                if (vaccineThis != null) {
                    newVaccine.id = vaccineThis!!.id
                } else {
                    newVaccine.id = UUID.randomUUID().toString()
                }

                newVaccine.dateOfVaccination = dateVaccine?.text.toString()
                newVaccine.type = typeVaccine?.text.toString()
                newVaccine.name = nameVaccine?.text.toString()
                newVaccine.manufacturer = manufacturerVaccine?.text.toString()
                newVaccine.validUntil = validUntilVaccine?.text.toString()
                newVaccine.veterinarian = veterinarianVaccine?.text.toString()

                viewModelVaccines.setVaccine(petName!!, newVaccine)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}