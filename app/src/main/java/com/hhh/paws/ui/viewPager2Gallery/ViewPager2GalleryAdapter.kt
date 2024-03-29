package com.hhh.paws.ui.viewPager2Gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.google.firebase.storage.FirebaseStorage
import com.hhh.paws.R

class ViewPager2GalleryAdapter: RecyclerView.Adapter<ViewPager2GalleryAdapter.ViewHolder>() {

    private var list = mutableListOf<String>()
    fun setListViewPager(list: MutableList<String>) {
        if (this.list.isNotEmpty()) {
            this.list.clear()
        }
        this.list = list
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val galleryDetailImageView: PhotoView = itemView.findViewById<PhotoView>(R.id.galleryDetailImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_detail_viewpager2_gallery, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image: String = list[position]
        val attacher = PhotoViewAttacher(holder.galleryDetailImageView)
        attacher.maximumScale = 4.0f

        FirebaseStorage.getInstance().reference
            .child("images/$image").downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(holder.galleryDetailImageView.context)
                    .load(uri)
                    .into(holder.galleryDetailImageView)
                attacher.update()
            }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}