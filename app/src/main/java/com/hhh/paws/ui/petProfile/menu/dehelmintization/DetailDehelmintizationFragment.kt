package com.hhh.paws.ui.petProfile.menu.dehelmintization

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hhh.paws.R
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.viewModel.DehelmintizationViewModel
import com.hhh.paws.databinding.FragmentDetailDehelmintizationBinding
import com.hhh.paws.util.UiState
import java.util.*


class DetailDehelmintizationFragment : Fragment() {

    private var _binding: FragmentDetailDehelmintizationBinding? = null
    private val mBinding get() = _binding!!

    private var dehelmintizationThis: Dehelmintization? = null
    private lateinit var petName: String

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

        petName = "Котик"
        dehelmintizationThis = bundleArgs.dehelmintization

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newDehelmintization = Dehelmintization()
                if (dehelmintizationThis != null) {
                    newDehelmintization.id = dehelmintizationThis!!.id
                } else {
                    newDehelmintization.id = UUID.randomUUID().toString()
                }



                viewModelDehelmintization.update.observe(viewLifecycleOwner) {
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
                viewModelDehelmintization.updateDehelmintization(newDehelmintization, petName)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}