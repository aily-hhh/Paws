package com.hhh.paws.ui.petProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.storage.FirebaseStorage
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.ActivityVetPassportBinding
import com.hhh.paws.util.PetName
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.time.LocalDate


@AndroidEntryPoint
class VetPassportActivity : AppCompatActivity() {

    private var _binding: ActivityVetPassportBinding? = null
    private val mBinding get() = _binding!!

    private var appBarConfiguration: AppBarConfiguration? = null
    private var iconPetProfile: ImageView? = null
    private var namePetProfile: TextView? = null
    private var agePetProfile: TextView? = null

    private val viewModelPet by viewModels<PetViewModel>()
    private val bundleArgs: VetPassportActivityArgs by navArgs()

    private var petNameThis: String? = null

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
        PetName.name = petNameThis

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
                R.id.nav_settings
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration!!)
        navView.setupWithNavController(navController)

        val header: View = navView.getHeaderView(0)
        namePetProfile = header.findViewById<TextView>(R.id.namePetProfile)
        agePetProfile = header.findViewById<TextView>(R.id.agePetProfile)
        iconPetProfile = header.findViewById<ImageView>(R.id.iconPetProfile)
        iconPetProfile?.setOnClickListener {
            val intent = Intent(this, PetProfileActivity::class.java)
            intent.putExtra("pet", petNameThis)
            startActivity(intent)
        }

        viewModelPet.pet.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    Log.d("UI State", "$it")
                    namePetProfile?.text = it.data.name
                    agePetProfile?.text = it.data.birthday
                    FirebaseStorage.getInstance().reference
                        .child("images/" + it.data.photoUri).downloadUrl
                        .addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                .load(uri)
                                .into(iconPetProfile!!)
                        }.addOnFailureListener {
                            Log.d("UI State", "Failure image: ${it.localizedMessage}")
                        }

                    if (it.data.birthday != null && it.data.birthday != "" && it.data.birthday != "null") {
                        calculateAge(it.data.birthday)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { age ->
                                agePetProfile?.text = age
                            }
                    }
                }
                is UiState.Failure -> {
                    Log.d("UI State", it.error.toString())
                }
                else -> {

                }
            }
        }

        viewModelPet.getPet(petNameThis!!)
    }

    private fun calculateAge(birthday: String): Observable<String> {
        return Observable.fromSingle { source ->
            val date = birthday.split(".").toTypedArray()
            val birthdayDay = date[0].toInt()
            val birthdayMonth = date[1].toInt()
            val birthdayYear = date[2].toInt()
            val dateNow = LocalDate.now()
            val currentDay = dateNow.dayOfMonth
            val currentMonth = dateNow.monthValue + 1
            val currentYear = dateNow.year
            val difDay = currentDay - birthdayDay
            val difMonth = currentMonth - birthdayMonth
            var difYear = currentYear - birthdayYear
            if (difMonth == 0 && difDay < 0) {
                difYear -= 1
                val age = "$difYear y.o"
                source.onSuccess(age)
            } else if (difMonth == 0) {
                val age = "$difYear y.o"
                source.onSuccess(age)
            } else if (difMonth > 0) {
                val age = "$difYear y.o"
                source.onSuccess(age)
            } else {
                difYear -= 1
                val age = "$difYear y.o"
                source.onSuccess(age)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_vet_passport)
        return navController.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        PetName.name = null
    }
}