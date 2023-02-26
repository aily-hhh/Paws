package com.hhh.paws.ui.settings

import android.app.AlertDialog
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.UserViewModel
import com.hhh.paws.util.UiState
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private var listener: OnSharedPreferenceChangeListener? = null
    var deleteUserButtonFragment: DeleteUserButtonFragment? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        deleteUserButtonFragment = findPreference("deleteUser")

        val viewModelUser by viewModels<UserViewModel>()
        viewModelUser.delete.observe(viewLifecycleOwner) {
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

        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        listener =
            OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "language" || key == "themeApp") {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(150)
                        ProcessPhoenix.triggerRebirth(requireContext().applicationContext);
                    }
                } else {
                    deleteUserButtonFragment?.setClickListener {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setIcon(R.mipmap.logo_paws)
                        alertDialog.setTitle(R.string.delete_profile)
                        alertDialog.setPositiveButton(
                            R.string.yes
                        ) { dialogInterface, i ->
                            viewModelUser.deleteUser()
                        }
                        alertDialog.setNegativeButton(
                            R.string.no
                        ) { dialogInterface, i -> dialogInterface.dismiss() }
                        alertDialog.show()
                    }
                }
            }

    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences!!.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        preferenceScreen.sharedPreferences!!.unregisterOnSharedPreferenceChangeListener(listener)
        super.onPause()
    }
}