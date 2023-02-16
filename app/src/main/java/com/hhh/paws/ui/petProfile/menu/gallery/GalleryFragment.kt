package com.hhh.paws.ui.petProfile.menu.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.paws.R
import com.hhh.paws.database.viewModel.GalleryViewModel
import com.hhh.paws.databinding.FragmentGalleryBinding
import com.hhh.paws.util.PetName
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
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
        // adapter clickListener

        notElemGallery = mBinding.notElemGallery
        addTextView = mBinding.addTextView
        addArrow = mBinding.addArrow
        progressBarGallery = mBinding.progressBarGallery

        addGalleryButton = mBinding.addGalleryButton
        addGalleryButton?.setOnClickListener {
            // открытие галереи, выбор фото и загрузка в бд
        }

        viewModelGallery.allImages.observe(viewLifecycleOwner) {
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}