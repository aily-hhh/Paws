package com.hhh.paws.ui.petProfile.menu.vaccines

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.paws.R
import com.hhh.paws.database.model.Vaccine
import com.hhh.paws.ui.petProfile.menu.ItemClickListener

class VaccinesAdapter: RecyclerView.Adapter<VaccinesAdapter.VaccinesViewHolder>() {

    inner class VaccinesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val vaccinesContainer: CardView = view.findViewById(R.id.vaccinesContainer)
        val typeVaccineItem: TextView = view.findViewById(R.id.typeVaccineItem)
        val nameVaccineItem: TextView = view.findViewById(R.id.nameVaccineItem)
        val manufacturerVaccineItem: TextView = view.findViewById(R.id.manufacturerVaccineItem)
        val dateVaccineItem: TextView = view.findViewById(R.id.dateVaccineItem)
        val veterinarianVaccineItem: TextView = view.findViewById(R.id.veterinarianVaccineItem)
    }

    private val callback = object: DiffUtil.ItemCallback<Vaccine>() {
        override fun areItemsTheSame(oldItem: Vaccine, newItem: Vaccine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Vaccine, newItem: Vaccine): Boolean {
            return if (oldItem.id != newItem.id) {
                false
            } else if (oldItem.name != newItem.name) {
                false
            } else if (oldItem.manufacturer != newItem.manufacturer) {
                false
            } else if (oldItem.dateOfVaccination != newItem.dateOfVaccination) {
                false
            } else if (oldItem.type != newItem.type) {
                false
            } else if (oldItem.validUntil != newItem.validUntil) {
                false
            } else {
                oldItem.veterinarian == newItem.veterinarian
            }
        }
    }

    private val differ = AsyncListDiffer<Vaccine>(this, callback)

    fun setDiffer(vaccineList: List<Vaccine>) {
        this.differ.submitList(null)
        this.differ.submitList(vaccineList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinesViewHolder {
        return VaccinesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.vaccine_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: VaccinesViewHolder, position: Int) {
        val currentVaccine = this.differ.currentList[position]

        holder.dateVaccineItem.text = currentVaccine.dateOfVaccination
        holder.nameVaccineItem.text = currentVaccine.name
        holder.typeVaccineItem.text = currentVaccine.type
        holder.manufacturerVaccineItem.text = currentVaccine.manufacturer
        holder.veterinarianVaccineItem.text = currentVaccine.veterinarian

        holder.itemView.apply {
            this.setOnClickListener {
                clickListener?.onItemClickListener(differ.currentList[holder.adapterPosition])
            }

            this.setOnLongClickListener {
                clickListener?.onItemLongClickListener(
                    differ.currentList[holder.adapterPosition],
                    holder.vaccinesContainer
                )
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return this.differ.currentList.size
    }

    private var clickListener: ItemClickListener? = null
    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }
}