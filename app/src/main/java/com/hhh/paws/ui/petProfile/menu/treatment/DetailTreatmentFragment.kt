package com.hhh.paws.ui.petProfile.menu.treatment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import dagger.hilt.android.AndroidEntryPoint

import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.database.model.Treatment
import com.hhh.paws.database.viewModel.TreatmentViewModel
import com.hhh.paws.databinding.FragmentDetailTreatmentBinding
import com.hhh.paws.util.UiState
import java.util.*


@AndroidEntryPoint
class DetailTreatmentFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentDetailTreatmentBinding? = null
    private val mBinding get() = _binding!!

    private var progressBarTreatmentDetail: ProgressBar? = null
    private var nameTreatmentDetail: TextInputEditText? = null
    private var manufacturerTreatmentDetail: TextInputEditText? = null
    private var dateTreatmentDetail: TextInputEditText? = null
    private var veterinarianTreatmentDetail: TextInputEditText? = null

    private var year: Int = 0;
    private var month: Int = 0;
    private var day: Int = 0;
    private var savedYear: Int = 0;
    private var savedMonth: Int = 0;
    private var savedDay: Int = 0;

    private val viewModelTreatment by viewModels<TreatmentViewModel>()
    private val bundleArgs by navArgs<DetailTreatmentFragmentArgs>()
    private var petName: String? = null
    private var treatmentThis: Treatment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailTreatmentBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petName = "Котик"
        treatmentThis = bundleArgs.treatment

        progressBarTreatmentDetail = mBinding.progressBarTreatmentDetail
        nameTreatmentDetail = mBinding.nameTreatmentDetail
        manufacturerTreatmentDetail = mBinding.dateTreatmentDetail
        veterinarianTreatmentDetail = mBinding.veterinarianTreatmentDetail

        dateTreatmentDetail = mBinding.dateTreatmentDetail
        dateTreatmentDetail?.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        if (treatmentThis != null) {
            nameTreatmentDetail?.setText(treatmentThis!!.name)
            manufacturerTreatmentDetail?.setText(treatmentThis!!.manufacturer)
            dateTreatmentDetail?.setText(treatmentThis!!.date)
            veterinarianTreatmentDetail?.setText(treatmentThis!!.veterinarian)
        }

        viewModelTreatment.addTreatment.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarTreatmentDetail?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarTreatmentDetail?.visibility = View.INVISIBLE
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .popBackStack()
                }
                is UiState.Failure -> {
                    progressBarTreatmentDetail?.visibility = View.INVISIBLE
                }
                else -> {
                    progressBarTreatmentDetail?.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newTreatment = Treatment()
                if (treatmentThis != null) {
                    newTreatment.id = treatmentThis!!.id
                } else {
                    newTreatment.id = UUID.randomUUID().toString()
                }

                newTreatment.name = nameTreatmentDetail?.text.toString()
                newTreatment.date = dateTreatmentDetail?.text.toString()
                newTreatment.manufacturer = manufacturerTreatmentDetail?.text.toString()
                newTreatment.veterinarian = veterinarianTreatmentDetail?.text.toString()

                viewModelTreatment.setTreatment(petName!!, newTreatment)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDateCalendar() {
        val calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        dateTreatmentDetail!!.setText("$savedDay.$savedMonth.$savedYear")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}