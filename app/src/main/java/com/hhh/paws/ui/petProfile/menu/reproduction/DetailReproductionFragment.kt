package com.hhh.paws.ui.petProfile.menu.reproduction

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.database.viewModel.ReproductionViewModel
import com.hhh.paws.databinding.FragmentDetailReproductionBinding
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class DetailReproductionFragment : Fragment() {

    private var _binding: FragmentDetailReproductionBinding? = null
    private val mBinding get() = _binding!!

    private var dateOfHeatDetail: TextInputEditText? = null
    private var dateOfMatingDetail: TextInputEditText? = null
    private var dateOfBirthDetail: TextInputEditText? = null
    private var numberOfTheLitterDetail: TextInputEditText? = null

    private var year: Int = 0;
    private var month: Int = 0;
    private var day: Int = 0;
    private var savedYear: Int = 0;
    private var savedMonth: Int = 0;
    private var savedDay: Int = 0;

    private var petName: String? = null
    private var reproductionThis: Reproduction? = null

    private val viewModelReproduction by viewModels<ReproductionViewModel>()
    private val bundleArgs by navArgs<DetailReproductionFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailReproductionBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petName = "Котик"
        reproductionThis = bundleArgs.reproduction

        numberOfTheLitterDetail = mBinding.numberOfTheLitterDetail

        dateOfHeatDetail = mBinding.dateOfHeatDetail
        dateOfHeatDetail!!.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), dateOfHeatListener, year, month, day).show()
        }

        dateOfMatingDetail = mBinding.dateOfMatingDetail
        dateOfMatingDetail!!.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), dateOfMatingListener, year, month, day).show()
        }

        dateOfBirthDetail = mBinding.dateOfBirthDetail
        dateOfBirthDetail!!.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(requireContext(), dateOfBirthListener, year, month, day).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newReproduction = Reproduction()
                if (reproductionThis != null) {
                    newReproduction.id = reproductionThis!!.id
                } else {
                    newReproduction.id = UUID.randomUUID().toString()
                }

                newReproduction.dateOfMating = dateOfMatingDetail?.text.toString()
                newReproduction.dateOfHeat = dateOfHeatDetail?.text.toString()
                newReproduction.dateOfBirth = dateOfBirthDetail?.text.toString()
                newReproduction.numberOfTheLitter = numberOfTheLitterDetail?.text.toString()

                viewModelReproduction.addReproduction.observe(viewLifecycleOwner) {
                    when (it) {
                        is UiState.Loading -> {

                        }
                        is UiState.Success -> {
                            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                                .popBackStack()
                        }
                        is UiState.Failure -> {

                        }
                        else -> {

                        }
                    }
                }
                viewModelReproduction.setReproduction(petName!!, newReproduction)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getDateCalendar() {
        year = Calendar.YEAR
        month = Calendar.MONTH
        day = Calendar.DAY_OF_MONTH
    }

    @SuppressLint("SetTextI18n")
    private var dateOfHeatListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        dateOfHeatDetail!!.setText("$savedDay.$savedMonth.$savedYear")
    }
    @SuppressLint("SetTextI18n")
    private var dateOfMatingListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        dateOfMatingDetail!!.setText("$savedDay.$savedMonth.$savedYear")
    }
    @SuppressLint("SetTextI18n")
    private var dateOfBirthListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        dateOfBirthDetail!!.setText("$savedDay.$savedMonth.$savedYear")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}