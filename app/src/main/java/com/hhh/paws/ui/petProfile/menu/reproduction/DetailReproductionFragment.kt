package com.hhh.paws.ui.petProfile.menu.reproduction

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.hhh.paws.R
import com.hhh.paws.database.model.Notes
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

        dateOfHeatDetail = mBinding.dateOfHeatDetail
        dateOfMatingDetail = mBinding.dateOfMatingDetail
        dateOfBirthDetail = mBinding.dateOfBirthDetail
        numberOfTheLitterDetail = mBinding.numberOfTheLitterDetail

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}