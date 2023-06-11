package com.hhh.paws.ui.petProfile.menu.treatment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.paws.R
import com.hhh.paws.database.model.Treatment
import com.hhh.paws.ui.petProfile.menu.ItemClickListener

class TreatmentAdapter : RecyclerView.Adapter<TreatmentAdapter.TreatmentViewHolder>() {

    inner class TreatmentViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val treatmentContainer: CardView = itemView.findViewById(R.id.treatmentContainer)
        val nameTreatmentItem: TextView = itemView.findViewById(R.id.nameTreatmentItem)
        val manufacturerTreatmentItem: TextView = itemView.findViewById(R.id.manufacturerTreatmentItem)
        val dateTreatmentItem: TextView = itemView.findViewById(R.id.dateTreatmentItem)
        val veterinarianTreatmentItem: TextView = itemView.findViewById(R.id.veterinarianTreatmentItem)
    }

    private val callback: DiffUtil.ItemCallback<Treatment> =
        object : DiffUtil.ItemCallback<Treatment>() {
            override fun areItemsTheSame(oldItem: Treatment, newItem: Treatment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Treatment, newItem: Treatment): Boolean {
                return if (oldItem.id != newItem.id) {
                    false
                } else if (oldItem.date != newItem.date) {
                    false
                } else if (oldItem.name != newItem.name) {
                    false
                } else if (oldItem.veterinarian != newItem.veterinarian) {
                    false
                } else {
                    oldItem.manufacturer == newItem.manufacturer
                }
            }
        }

    private val differ = AsyncListDiffer(this, callback)
    fun setDiffer(treatmentList: List<Treatment>?) {
        differ.submitList(treatmentList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        return TreatmentViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.treatment_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val currentTreatment = differ.currentList[position]
        holder.dateTreatmentItem.text = currentTreatment.date
        holder.nameTreatmentItem.text = currentTreatment.name
        holder.manufacturerTreatmentItem.text = currentTreatment.manufacturer
        holder.veterinarianTreatmentItem.text = currentTreatment.veterinarian
        holder.itemView.setOnClickListener { clickListener!!.onItemClickListener(differ.currentList[holder.adapterPosition]) }
        holder.itemView.setOnLongClickListener {
            clickListener!!.onItemLongClickListener(
                differ.currentList[holder.adapterPosition],
                holder.treatmentContainer
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