package com.hhh.paws.ui.settings

import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.UserViewModel
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}