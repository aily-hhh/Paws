package com.hhh.paws.ui.petProfile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import com.hhh.paws.MainActivity
import com.hhh.paws.R
import com.hhh.paws.database.model.Pet
import com.hhh.paws.database.viewModel.PetViewModel
import com.hhh.paws.databinding.ActivityPetProfileBinding
import com.hhh.paws.ui.main.MainFragment
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class PetProfileActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private var _binding: ActivityPetProfileBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var petSpecies: TextInputEditText
    private lateinit var petBreed: TextInputEditText
    private lateinit var petBirthday: TextInputEditText
    private lateinit var petHair: TextInputEditText
    private lateinit var spinnerSex: AppCompatSpinner
    private lateinit var buttonUpdate: Button
    private lateinit var buttonBack: Button
    private lateinit var buttonToMainFragment: Button
    private lateinit var buttonDelete: Button
    private lateinit var toolbarLayoutPet: CollapsingToolbarLayout

    private lateinit var petPhoto: ImageView
    private lateinit var changeImagePetFab: FloatingActionButton

    private val viewModelPet by viewModels<PetViewModel>()
    private val bundleArgs: PetProfileActivityArgs by navArgs()

    private lateinit var petNameThis: String
    private var photoUri: Uri? = null
    private var day = 0
    private var month = 0
    private var year = 0
    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val w: Window = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        _binding = ActivityPetProfileBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        petNameThis = bundleArgs.pet

        toolbarLayoutPet = mBinding.toolbarLayoutPet
        toolbarLayoutPet.title = petNameThis

        petSpecies = mBinding.root.findViewById(R.id.petSpecies)
        petBreed = mBinding.root.findViewById(R.id.petBreed)
        petHair = mBinding.root.findViewById(R.id.petHair)

        petBirthday = mBinding.root.findViewById(R.id.petBirthday)
        petBirthday.setOnClickListener {
            getDateCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }

        spinnerSex = mBinding.root.findViewById(R.id.spinnerSex)
        val adapterSex = ArrayAdapter.createFromResource(
            this,
            R.array.sexArray,
            android.R.layout.simple_spinner_item
        )
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSex.adapter = adapterSex

        buttonUpdate = mBinding.root.findViewById(R.id.buttonUpdate)
        buttonUpdate.setOnClickListener {
            val pet = Pet(
                petNameThis,
                petSpecies.text.toString(),
                petBreed.text.toString(),
                spinnerSex.selectedItem.toString(),
                petBirthday.text.toString(),
                petHair.text.toString(),
                photoUri.toString()
            )
            viewModelPet.updatePet(pet)
        }
        viewModelPet.update.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    toast(this, it.data)
                }
                is UiState.Failure -> {
                    Log.e("UI State", it.error.toString())
                }
            }
        }

        buttonBack = mBinding.root.findViewById(R.id.buttonBack)
        buttonBack.setOnClickListener {
            val intent = Intent(this, VetPassportActivity::class.java)
            intent.putExtra("pet", petNameThis)
            startActivity(intent)
        }

        buttonToMainFragment = mBinding.root.findViewById(R.id.buttonToMainFragment)
        buttonToMainFragment.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonDelete = mBinding.root.findViewById(R.id.buttonDelete)
        buttonDelete.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setIcon(R.mipmap.logo_paws)
            alertDialog.setTitle(R.string.delete_profile)
            alertDialog.setMessage(R.string.delete_profile_msg)
            alertDialog.setPositiveButton(R.string.cancel
            ) { dialogInterface, i -> dialogInterface.dismiss() }
            alertDialog.setNeutralButton(R.string.delete_yes
            ) { dialogInterface, i ->
                viewModelPet.deletePet(petNameThis)
            }
            alertDialog.show()

            val intent = Intent(this, MainFragment::class.java)
        }
        viewModelPet.delete.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    toast(this, it.data)
                }
                is UiState.Failure -> {
                    Log.e("UI State", it.error.toString())
                }
            }
        }

        petPhoto = mBinding.petPhoto
        changeImagePetFab = mBinding.changeImagePetFab
        changeImagePetFab.setOnClickListener {
            updatePetPhoto()
        }

        viewModelPet.getPet(petNameThis)
        viewModelPet.pet.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    Log.d("UI State", "Loading")
                }
                is UiState.Success -> {
                    Log.d("UI State", "$it")
                    petSpecies.setText(it.data.species)
                    petBreed.setText(it.data.breed)
                    petBirthday.setText(it.data.birthday)
                    petHair.setText(it.data.hair)
                    val pos = adapterSex.getPosition(it.data.sex)
                    spinnerSex.setSelection(pos)
                    FirebaseStorage.getInstance().reference
                        .child("images/" + it.data.photoUri).downloadUrl
                        .addOnSuccessListener { uri ->
                            Glide.with(applicationContext)
                                .load(uri)
                                .into(petPhoto)
                        }.addOnFailureListener {
                            Log.d("UI State", "Failure image: ${it.localizedMessage}")
                        }
                }
                is UiState.Failure -> {
                    Log.d("UI State", it.error.toString())
                }
            }
        }
    }

    private fun getDateCalendar() {
        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month + 1
        savedYear = year

        petBirthday.setText("$savedDay.$savedMonth.$savedYear")
    }

    private fun isPhotoPickerAvailable(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                SdkExtensions.getExtensionVersion(Build.VERSION_CODES.R) >= 2
            } else {
                TODO("VERSION.SDK_INT < TIRAMISU")
            }
        } else {
            false
        }
    }

    private fun updatePetPhoto() {
        if (isPhotoPickerAvailable()) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            mGetContent.launch("image/*")
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { setImage(it) }
    }

    private var mGetContent: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        uri?.let { setImage(it) }
    }

    private fun setImage(uri: Uri) {
        photoUri = uri
        petPhoto.setImageURI(uri)
    }
}