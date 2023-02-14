package com.hhh.paws.database.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hhh.paws.database.repository.GalleryRepository
import com.hhh.paws.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class GalleryViewModel @Inject constructor(private val repository: GalleryRepository): ViewModel() {

    private var _allImages: MutableLiveData<UiState<List<String>>> = MutableLiveData()
    val allImages: LiveData<UiState<List<String>>> get() = _allImages
    fun getAllImages(petName: String) {
        _allImages.value = UiState.Loading
        repository.getAllImages(petName) {
            _allImages.value = it
        }
    }

    private var _oneImage: MutableLiveData<UiState<String>> = MutableLiveData()
    val oneImage: LiveData<UiState<String>> get() = _oneImage
    fun getOneImage(petName: String, image: String) {
        _oneImage.value = UiState.Loading
        repository.getOneImage(petName, image) {
            _oneImage.value = it
        }
    }
}