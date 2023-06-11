package com.hhh.paws.ui.petProfile.menu.procedures

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.SurgicalProcedure
import com.hhh.paws.database.viewModel.ProcedureViewModel
import com.hhh.paws.databinding.FragmentDetailProceduresBinding
import com.hhh.paws.ui.petProfile.menu.notes.DetailNoteFragmentArgs
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailProceduresFragment : Fragment(), OnDateSetListener {

    private var binding: FragmentDetailProceduresBinding? = null
    private var spinnerTypeSurgicalProcedure: MaterialAutoCompleteTextView? = null
    private var nameSurgicalProcedure: TextInputEditText? = null
    private var anesthesiaSurgicalProcedure: TextInputEditText? = null
    private var dateSurgicalProcedure: TextInputEditText? = null
    private var veterinarianSurgicalProcedure: TextInputEditText? = null
    private var descriptionSurgicalProcedure: TextInputEditText? = null
    private var progressBarProcedureDetail: ProgressBar? = null
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private val viewModelProcedures by viewModels<ProcedureViewModel>()
    private val bundleArgs: DetailProceduresFragmentArgs by navArgs()
    private var petNameThis: String? = null
    private var procedure: SurgicalProcedure? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailProceduresBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petNameThis = name
        procedure = bundleArgs.procedure

        progressBarProcedureDetail = binding!!.progressBarProcedureDetail
        spinnerTypeSurgicalProcedure = binding!!.spinnerTypeSurgicalProcedure
        nameSurgicalProcedure = binding!!.nameSurgicalProcedure
        anesthesiaSurgicalProcedure = binding!!.anesthesiaSurgicalProcedure
        veterinarianSurgicalProcedure = binding!!.veterinarianSurgicalProcedure
        descriptionSurgicalProcedure = binding!!.descriptionSurgicalProcedure
        dateSurgicalProcedure = binding!!.dateSurgicalProcedure

        dateSurgicalProcedure!!.setOnClickListener { v: View? ->
            dateSet
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        if (procedure != null) {
            spinnerTypeSurgicalProcedure!!.setText(procedure!!.type)
            nameSurgicalProcedure!!.setText(procedure!!.name)
            anesthesiaSurgicalProcedure!!.setText(procedure!!.anesthesia)
            dateSurgicalProcedure!!.setText(procedure!!.date)
            veterinarianSurgicalProcedure!!.setText(procedure!!.veterinarian)
            descriptionSurgicalProcedure!!.setText(procedure!!.description)
        }

        viewModelProcedures.addProcedure.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarProcedureDetail?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarProcedureDetail?.visibility = View.INVISIBLE
                    findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .popBackStack()
                }
                is UiState.Failure -> {
                    progressBarProcedureDetail?.visibility = View.INVISIBLE
                }
                else -> {
                    progressBarProcedureDetail?.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_save) {
            progressBarProcedureDetail!!.visibility = View.VISIBLE
            val newProcedure = SurgicalProcedure()
            if (procedure != null) {
                newProcedure.id = procedure!!.id
            } else {
                newProcedure.id = UUID.randomUUID().toString()
            }
            newProcedure.anesthesia = anesthesiaSurgicalProcedure!!.text.toString()
            newProcedure.date = dateSurgicalProcedure!!.text.toString()
            newProcedure.description = descriptionSurgicalProcedure!!.text.toString()
            newProcedure.veterinarian = veterinarianSurgicalProcedure!!.text.toString()
            newProcedure.type = spinnerTypeSurgicalProcedure!!.text.toString()
            newProcedure.name = nameSurgicalProcedure!!.text.toString()
            progressBarProcedureDetail!!.visibility = View.INVISIBLE
            viewModelProcedures.setProcedure(petNameThis!!, newProcedure)
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .popBackStack()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private val dateSet: Unit
        get() {
            val calendar = Calendar.getInstance()
            year = calendar[Calendar.YEAR]
            month = calendar[Calendar.MONTH]
            day = calendar[Calendar.DAY_OF_MONTH]
        }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year
        dateSurgicalProcedure!!.setText("$savedDay.$savedMonth.$savedYear")
    }
}