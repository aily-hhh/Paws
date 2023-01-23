package com.hhh.paws.ui.petProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.hhh.paws.R
import com.hhh.paws.databinding.ActivityVetPassportBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VetPassportActivity : AppCompatActivity() {

    private var _binding: ActivityVetPassportBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityVetPassportBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setSupportActionBar(mBinding.appBarVetPassport.toolbarVetPassport)

        val drawerLayout: DrawerLayout = mBinding.drawerLayout
        val navView: NavigationView = mBinding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_vet_passport)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_notes,
                R.id.nav_gallery,
                R.id.nav_vaccines,
                R.id.nav_procedures,
                R.id.nav_treatment,
                R.id.nav_dehelmintization,
                R.id.nav_reproduction,
                R.id.nav_identification,
                R.id.nav_settings,
                R.id.nav_signOut
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_vet_passport)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}