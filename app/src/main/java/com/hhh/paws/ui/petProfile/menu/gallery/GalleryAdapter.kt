package com.hhh.paws.ui.petProfile.menu.gallery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.hhh.paws.R
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import kotlinx.coroutines.coroutineScope

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
        differ.submitList(list)
    }

    fun getDiffer(): MutableList<String> {
        return differ.currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.gallery_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val uriStr: String = differ.currentList[position]

        holder.itemView.apply {
            FirebaseStorage.getInstance().reference
                .child("images/$uriStr").downloadUrl
                .addOnSuccessListener { uri ->
                    Glide.with(holder.galleryImage.context)
                        .load(uri)
                        .into(holder.galleryImage)
                }

            setOnClickListener {
                clickListener!!.onClickListener(position)
            }
            setOnLongClickListener {
                clickListener!!.onLongClickListener(uriStr, holder.itemView)
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var clickListener: GalleryClickListener? = null
    fun setClickListener(clickListener: GalleryClickListener) {
        this.clickListener = clickListener
    }

}

interface GalleryClickListener {
    fun onClickListener(position: Int)
    fun onLongClickListener(uri: String, view: View)
}