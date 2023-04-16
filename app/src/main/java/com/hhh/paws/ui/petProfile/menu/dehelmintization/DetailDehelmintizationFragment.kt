package com.hhh.paws.ui.petProfile.menu.dehelmintization

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.viewModel.DehelmintizationViewModel
import com.hhh.paws.databinding.FragmentDetailDehelmintizationBinding
import com.hhh.paws.util.PetName
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailDehelmintizationFragment : Fragment(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentDetailDehelmintizationBinding? = null
    private val mBinding get() = _binding!!

    private var nameDehelmintizationDetail: TextInputEditText? = null
    private var manufacturerDehelmintizationDetail: TextInputEditText? = null
    private var doseDehelmintizationDetail: TextInputEditText? = null
    private var dateDehelmintizationDetail: TextInputEditText? = null
    private var timeDehelmintizationDetail: TextInputEditText? = null
    private var veterinarianDehelmintizationDetail: TextInputEditText? = null
    private var descriptionDehelmintizationDetail: TextInputEditText? = null
    private var progressBarDehelmintizationDetail: ProgressBar? = null

    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0
    private var min = 0
    private var hour = 0
    private var savedMin = 0
    private var savedHour = 0

    private var dehelmintizationThis: Dehelmintization? = null
    private var petName: String? = null

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

        petName = PetName.name
        dehelmintizationThis = bundleArgs.dehelmintization

        nameDehelmintizationDetail = mBinding.nameDehelmintizationDetail
        manufacturerDehelmintizationDetail = mBinding.manufacturerDehelmintizationDetail
        doseDehelmintizationDetail = mBinding.doseDehelmintizationDetail
        veterinarianDehelmintizationDetail = mBinding.veterinarianDehelmintizationDetail
        descriptionDehelmintizationDetail = mBinding.descriptionDehelmintizationDetail
        progressBarDehelmintizationDetail = mBinding.progressBarDehelmintizationDetail

        dateDehelmintizationDetail = mBinding.dateDehelmintizationDetail
        dateDehelmintizationDetail!!.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        timeDehelmintizationDetail = mBinding.timeDehelmintizationDetail
        timeDehelmintizationDetail!!.setOnClickListener {
            getTimeCalendar()
            TimePickerDialog(requireActivity(), this, hour, min, true).show()
        }

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

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newDehelmintization = Dehelmintization()
                if (dehelmintizationThis != null) {
                    newDehelmintization.id = dehelmintizationThis!!.id
                } else {
                    newDehelmintization.id = UUID.randomUUID().toString()
                }

                newDehelmintization.description = descriptionDehelmintizationDetail?.text.toString()
                newDehelmintization.name = nameDehelmintizationDetail?.text.toString()
                newDehelmintization.veterinarian = veterinarianDehelmintizationDetail?.text.toString()
                newDehelmintization.dose = doseDehelmintizationDetail?.text.toString()
                newDehelmintization.manufacturer = manufacturerDehelmintizationDetail?.text.toString()
                newDehelmintization.time = timeDehelmintizationDetail?.text.toString()
                newDehelmintization.date = dateDehelmintizationDetail?.text.toString()

                viewModelDehelmintization.addDehelmintization.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Loading -> {
                            progressBarDehelmintizationDetail?.visibility = View.VISIBLE
                        }
                        is UiState.Success -> {
                            progressBarDehelmintizationDetail?.visibility = View.INVISIBLE
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                                .popBackStack()
                        }
                        is UiState.Failure -> {
                            progressBarDehelmintizationDetail?.visibility = View.INVISIBLE
                            toast(getString(R.string.error))
                        }
                        else -> {
                            progressBarDehelmintizationDetail?.visibility = View.INVISIBLE
                        }
                    }
                }
                viewModelDehelmintization.setDehelmintization(newDehelmintization, petName!!)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDateCalendar() {
        val calendar = Calendar.getInstance()
        year = calendar[Calendar.YEAR]
        month = calendar[Calendar.MONTH]
        day = calendar[Calendar.DAY_OF_MONTH]
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        dateDehelmintizationDetail!!.setText("$savedDay.$savedMonth.$savedYear")
    }

    private fun getTimeCalendar() {
        val calendar = Calendar.getInstance()
        min = calendar.get(Calendar.MINUTE)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
    }

    @SuppressLint("SetTextI18n")
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMin = minute

        timeDehelmintizationDetail!!.setText("$savedHour:$savedMin")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}