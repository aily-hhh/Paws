package com.hhh.paws.database.dao

import com.hhh.paws.database.model.GalleryImage
import com.hhh.paws.util.UiState

interface GalleryDao {
    fun getAllImages(petName: String, result: (UiState<List<String>>) -> Unit)
    fun getOneImage(petName: String, image: String, result: (UiState<String>) -> Unit)
    suspend fun setImage(petName: String, image: GalleryImage, result: (UiState<String>) -> Unit)
    suspend fun deleteImage(petName: String, imageID: String, result: (UiState<String>) -> Unit)
}