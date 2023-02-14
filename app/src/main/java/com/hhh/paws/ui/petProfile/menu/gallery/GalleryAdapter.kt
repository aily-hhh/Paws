package com.hhh.paws.ui.petProfile.menu.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hhh.paws.R

class GalleryAdapter: RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    inner class GalleryViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val galleryImage: ImageView = view.findViewById(R.id.galleryImage)
    }

    private val callback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    private val differ = AsyncListDiffer(this, callback)

    fun setDiffer(list: List<String>) {
        differ.submitList(null)
        differ.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val uriStr: String = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this.context)
                .load(uriStr)
                .into(holder.galleryImage)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}