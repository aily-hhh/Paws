package com.hhh.paws.util

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.hhh.paws.R

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.alertDialogForMainFragment(
    adapterDialog: ArrayAdapter<String>,
    namesPetList: MutableList<String>
) {
    val alertDialog = AlertDialog.Builder(requireContext())
    alertDialog.setIcon(R.mipmap.logo_paws)
    alertDialog.setTitle(R.string.choose_pet)
    alertDialog.setAdapter(adapterDialog) { dialogInterface, i ->
        val bundle = bundleOf("pet" to namesPetList[i])
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_mainFragment_to_petProfileFragment, bundle)
    }
    alertDialog.setPositiveButton(R.string.cancel,
        DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.dismiss() })
    alertDialog.setNeutralButton(R.string.add_pet,
        DialogInterface.OnClickListener { dialogInterface, i ->
            Navigation.findNavController(requireActivity(), R.id.navHostFragment)
                .navigate(R.id.action_mainFragment_to_newPetFragment)
        })
    alertDialog.show()
}