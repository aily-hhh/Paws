package com.hhh.paws.ui.settings

import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.hhh.paws.R
import com.jakewharton.processphoenix.ProcessPhoenix
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private var listener: OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        listener =
            OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "language" || key == "themeApp") {
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(150)
                        ProcessPhoenix.triggerRebirth(requireContext().applicationContext);
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