package com.hhh.paws.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.FragmentMainBinding
import com.hhh.paws.util.UiState
import com.hhh.paws.util.alertDialogForMainFragment
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint


private var namesPetList: MutableList<String> = mutableListOf()

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!

    private var myPetsButton: Button? = null
    private var signOutButton: Button? = null
    private var settingsButton: ImageButton? = null
    private var progressBar: ProgressBar? = null
    private var adapterDialog: ArrayAdapter<String>? = null

    private val viewModelPet by viewModels<PetViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = mBinding.progressBar

        myPetsButton = mBinding.myPetsButton
        myPetsButton?.setOnClickListener{
            initDialogFragment()
        }

        signOutButton = mBinding.signOutButton
        signOutButton?.setOnClickListener{
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    toast("${R.string.signed_out_toast}")
                    Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                        .navigate(R.id.action_mainFragment_to_authFragment)
                }
        }

        settingsButton = mBinding.settingsButton
        settingsButton?.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        viewModelPet.namesPet.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBar?.visibility = View.VISIBLE
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    progressBar?.visibility = View.INVISIBLE
                    namesPetList = it.data.toMutableList()
                    adapterDialog = ArrayAdapter(
                        requireContext(),
                        android.R.layout.select_dialog_item,
                        namesPetList
                    )
                    Log.d("UI State", "$it")
                }
                is UiState.Failure -> {
                    progressBar?.visibility = View.INVISIBLE
                    Log.e("UI State", it.error.toString())
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModelPet.getNamesPet()
    }

    private fun initDialogFragment() {
        alertDialogForMainFragment(adapterDialog!!, namesPetList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}