package com.hhh.paws.ui.petProfile

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.ActivityVetPassportBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VetPassportActivity : AppCompatActivity() {

    private var _binding: ActivityVetPassportBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var iconPetProfile: ImageView

    private val bundleArgs: VetPassportActivityArgs by navArgs()
    private val viewModelPet by viewModels<PetViewModel>()

    private lateinit var petNameThis: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        _binding = ActivityVetPassportBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        petNameThis = bundleArgs.pet
        if (petNameThis.isEmpty()) {
            petNameThis = intent.getStringExtra("pet").toString()
        }

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

        val header: View = navView.getHeaderView(0)
        val namePetProfile = header.findViewById<TextView>(R.id.namePetProfile)
        val agePetProfile = header.findViewById<TextView>(R.id.agePetProfile)
        iconPetProfile = header.findViewById<ImageView>(R.id.iconPetProfile)
        iconPetProfile.setOnClickListener {
            val bundle = bundleOf("pet" to petNameThis)
            Navigation.findNavController(this@VetPassportActivity, R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_notes_to_petProfileActivity, bundle)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_vet_passport)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}