package com.hhh.paws.ui.petProfile.menu.identification

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Identification
import com.hhh.paws.database.viewModel.IdentificationViewModel
import com.hhh.paws.databinding.FragmentIdentificationBinding
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.Disposable
import java.util.*


@AndroidEntryPoint
class IdentificationFragment : Fragment(),
    OnDateSetListener {

    private var binding: FragmentIdentificationBinding? = null
    private var microchipNumber: TextInputEditText? = null
    private var dateOfMicrochipping: TextInputEditText? = null
    private var microchipLocation: TextInputEditText? = null
    private var tattooNumber: TextInputEditText? = null
    private var dateOfTattooing: TextInputEditText? = null
    private var progressBarIdentification: ProgressBar? = null
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var flag = 0
    private val viewModelIdentification by viewModels<IdentificationViewModel>()
    private var petNameThis: String? = null

    companion object {
        private const val DATE_TATTOO = 1
        private const val DATE_CHIP = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIdentificationBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petNameThis = name
        progressBarIdentification = binding!!.progressBarIdentification
        progressBarIdentification!!.visibility = View.VISIBLE
        microchipNumber = binding!!.microchipNumber
        microchipLocation = binding!!.microchipLocation
        tattooNumber = binding!!.tattooNumber
        dateOfTattooing = binding!!.dateOfTattooing

        dateOfTattooing!!.setOnClickListener { v: View? ->
            flag = DATE_TATTOO
            dateCalendar
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        dateOfMicrochipping = binding!!.dateOfMicrochipping
        dateOfMicrochipping!!.setOnClickListener { v: View? ->
            flag = DATE_CHIP
            dateCalendar
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        viewModelIdentification.getIdentificationInstance(petNameThis!!)
        viewModelIdentification.identificationInstance.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarIdentification!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarIdentification!!.visibility = View.INVISIBLE
                    microchipNumber?.setText(it.data.microchipNumber)
                    dateOfMicrochipping?.setText(it.data.dateOfMicrochipping)
                    microchipLocation?.setText(it.data.microchipLocation)
                    tattooNumber?.setText(it.data.tattooNumber)
                    dateOfTattooing?.setText(it.data.dateOfTattooing)
                }
                is UiState.Failure -> {
                    progressBarIdentification!!.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), getString(R.string.error), Toast.LENGTH_SHORT).show()
                }
                else -> { progressBarIdentification!!.visibility = View.INVISIBLE }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_save) {
            val newIdentification = Identification()
            newIdentification.tattooNumber = tattooNumber!!.text.toString().trim()
            newIdentification.microchipNumber = microchipNumber!!.text.toString().trim()
            newIdentification.microchipLocation = microchipLocation!!.text.toString().trim()
            newIdentification.dateOfMicrochipping = dateOfMicrochipping!!.text.toString().trim()
            newIdentification.dateOfTattooing = dateOfTattooing!!.text.toString().trim()

            viewModelIdentification.setIdentificationInstance(petNameThis!!, newIdentification)
            Toast.makeText(requireContext(), getString(R.string.saved), Toast.LENGTH_SHORT).show()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private val dateCalendar: Unit
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
        if (flag == DATE_TATTOO) {
            dateOfTattooing!!.setText("$savedDay.$savedMonth.$savedYear")
        } else if (flag == DATE_CHIP) {
            dateOfMicrochipping!!.setText("$savedDay.$savedMonth.$savedYear")
        }
        flag = 0
    }
}