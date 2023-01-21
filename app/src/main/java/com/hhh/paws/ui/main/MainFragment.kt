package com.hhh.paws.ui.main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.firebase.ui.auth.AuthUI
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.database.viewModel.UserViewModel
import com.hhh.paws.databinding.FragmentMainBinding
import com.hhh.paws.util.UiState
import com.hhh.paws.util.alertDialogForMainFragment
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var myPetsButton: Button
    private lateinit var signOutButton: Button
    private lateinit var settingsButton: ImageButton
    private lateinit var adapterDialog: ArrayAdapter<String>
    private var namesPetList: MutableList<String> = mutableListOf()

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

        myPetsButton = mBinding.myPetsButton
        myPetsButton.setOnClickListener{
            initDialogFragment()
        }

        signOutButton = mBinding.signOutButton
        signOutButton.setOnClickListener{
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    toast("Вы успешно вышли из аккаунта")
                    Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                        .navigate(R.id.action_mainFragment_to_authFragment)
                }
        }

        settingsButton = mBinding.settingsButton
        settingsButton.setOnClickListener{
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_mainFragment_to_settingsFragment)
        }

        viewModelPet.namesPet.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    namesPetList = it.data.toMutableList()
                    adapterDialog.clear()
                    adapterDialog = ArrayAdapter(
                        requireContext(),
                        android.R.layout.select_dialog_item,
                        namesPetList
                    )
                    Log.d("UI State", "$it")
                }
                is UiState.Failure -> {
                    Log.e("UI State", it.error.toString())
                }
            }
        }
    }

    private fun initDialogFragment() {
        viewModelPet.getNamesPet()
        alertDialogForMainFragment(adapterDialog)
    }
}