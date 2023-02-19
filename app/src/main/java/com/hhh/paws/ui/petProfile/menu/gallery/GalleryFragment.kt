package com.hhh.paws.ui.petProfile.menu.gallery

import android.app.AlertDialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ext.SdkExtensions
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.paws.R
import com.hhh.paws.database.model.Gallery
import com.hhh.paws.database.viewModel.GalleryViewModel
import com.hhh.paws.databinding.FragmentGalleryBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.PetName
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val mBinding get() = _binding!!

    private var recyclerGallery: RecyclerView? = null;
    private var addGalleryButton: FloatingActionButton? = null;
    private var notElemGallery: TextView? = null;
    private var addTextView: TextView? = null;
    private var addArrow: ImageView? = null;
    private var progressBarGallery: ProgressBar? = null;

    private var petName: String? = null
    private var adapter: GalleryAdapter? = null
    private var galleryModel: Gallery = Gallery()

    private val viewModelGallery by viewModels<GalleryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petName = PetName.name

        recyclerGallery = mBinding.recyclerGallery
        initAdapter()
        adapter?.setClickListener(object: GalleryClickListener {
            override fun onClickListener(position: Int) {
                galleryModel.position = position
                val bundle = bundleOf("gallery" to galleryModel)
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(R.id.action_nav_gallery_to_viewPager2GalleryFragment, bundle)
            }

            override fun onLongClickListener(uri: String, view: View) {
                showPopUp(uri, view)
            }

        })

        notElemGallery = mBinding.notElemGallery
        addTextView = mBinding.addTextView
        addArrow = mBinding.addArrow
        progressBarGallery = mBinding.progressBarGallery

        addGalleryButton = mBinding.addGalleryButton
        addGalleryButton?.setOnClickListener {
            choosePhoto()
        }

        viewModelGallery.allImages.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarGallery?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarGallery?.visibility = View.INVISIBLE
                    adapter?.setDiffer(it.data)
                    galleryModel.galleryList.addAll(it.data)
                    if (adapter?.itemCount == 0) {
                        notElemGallery?.visibility = View.VISIBLE
                        addArrow?.visibility = View.VISIBLE
                        addTextView?.visibility = View.VISIBLE
                    } else {
                        notElemGallery?.visibility = View.INVISIBLE
                        addArrow?.visibility = View.INVISIBLE
                        addTextView?.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarGallery?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
                else -> {
                    progressBarGallery?.visibility = View.INVISIBLE
                }
            }
        }

        viewModelGallery.oneImage.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarGallery?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarGallery?.visibility = View.INVISIBLE
                }
                is UiState.Failure -> {
                    progressBarGallery?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
                else -> {
                    progressBarGallery?.visibility = View.INVISIBLE
                }
            }
        }

        viewModelGallery.getAllImages(petName!!)
    }

    private fun initAdapter() {
        adapter = GalleryAdapter()
        recyclerGallery?.adapter = adapter
        recyclerGallery?.apply {
            adapter = adapter
            layoutManager = StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL)
        }
    }

    private fun showPopUp(currentImage: String, view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.deleteMenu) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setIcon(R.mipmap.logo_paws)
                alertDialog.setTitle(R.string.delete_note)
                alertDialog.setPositiveButton(
                    R.string.yes
                ) { dialogInterface, i ->
                    // delete
                }
                alertDialog.setNegativeButton(
                    R.string.no
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                alertDialog.show()
            }
            false
        })
        popupMenu.inflate(R.menu.long_click_menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
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

    private fun choosePhoto() {
        if (isPhotoPickerAvailable()) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            mGetContent.launch("image/*")
        }
    }

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { uploadImage(it) }
    }

    private var mGetContent: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.GetContent()) { uri ->
        uri?.let { uploadImage(it) }
    }

    private fun uploadImage(uri: Uri) {
        // add image in firebase and refresh recyclerView
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}