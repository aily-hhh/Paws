package com.hhh.paws.ui.petProfile.menu.dehelmintization

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.paws.R
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.ui.petProfile.menu.ItemClickListener

class DehelmintizationAdapter : RecyclerView.Adapter<DehelmintizationAdapter.DehelmintizationViewHolder>() {

    inner class DehelmintizationViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val dehelmintizationContainer: CardView = itemView.findViewById(R.id.dehelmintizationContainer)
        val nameDehelmintizationItem: TextView = itemView.findViewById(R.id.nameDehelmintizationItem)
        val timeDehelmintizationItem: TextView = itemView.findViewById(R.id.timeDehelmintizationItem)
        val manufacturerDehelmintizationItem: TextView = itemView.findViewById(R.id.manufacturerDehelmintizationItem)
        val doseDehelmintizationItem: TextView = itemView.findViewById(R.id.doseDehelmintizationItem)
        val veterinarianDehelmintizationItem: TextView = itemView.findViewById(R.id.veterinarianDehelmintizationItem)
        val dateDehelmintizationItem: TextView = itemView.findViewById(R.id.dateDehelmintizationItem)
    }

    private val callback: DiffUtil.ItemCallback<Dehelmintization> =
        object : DiffUtil.ItemCallback<Dehelmintization>() {
            override fun areItemsTheSame(
                oldItem: Dehelmintization,
                newItem: Dehelmintization
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Dehelmintization,
                newItem: Dehelmintization
            ): Boolean {
                return if (oldItem.id != newItem.id) false else if (oldItem.date != newItem.date) false else if (oldItem.description != newItem.description) false else if (oldItem.dose != newItem.dose) false else if (oldItem.manufacturer != newItem.manufacturer) false else if (oldItem.name != newItem.name) false else if (oldItem.time != newItem.time) false else oldItem.veterinarian == newItem.veterinarian
            }
        }

    private val differ = AsyncListDiffer(this, callback)
    fun setDiffer(dehelmintizationList: List<Dehelmintization>?) {
        differ.submitList(dehelmintizationList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DehelmintizationViewHolder {
        return DehelmintizationViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dehelmintization_item, parent, false)
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DehelmintizationViewHolder, position: Int) {
        val newDehelmintization = differ.currentList[position]
        if (newDehelmintization.date == "") {
            newDehelmintization.date = "-"
        }
        if (newDehelmintization.time == "") {
            newDehelmintization.time = "-"
        }
        holder.nameDehelmintizationItem.text = newDehelmintization.name
        holder.doseDehelmintizationItem.text = newDehelmintization.dose
        holder.timeDehelmintizationItem.text =
            newDehelmintization.date + ", " + newDehelmintization.time
        holder.manufacturerDehelmintizationItem.text = newDehelmintization.manufacturer
        holder.veterinarianDehelmintizationItem.text = newDehelmintization.veterinarian
        holder.dateDehelmintizationItem.text = newDehelmintization.date
        holder.itemView.setOnClickListener { clickListener!!.onItemClickListener(differ.currentList[holder.adapterPosition]) }
        holder.itemView.setOnLongClickListener {
            clickListener!!.onItemLongClickListener(
                differ.currentList[holder.adapterPosition],
                holder.dehelmintizationContainer
            )
            false
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var clickListener: ItemClickListener? = null
    fun setClickListener(clickListener: ItemClickListener?) {
        this.clickListener = clickListener
    }

}