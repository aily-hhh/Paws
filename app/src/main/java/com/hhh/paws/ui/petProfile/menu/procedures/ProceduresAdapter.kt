package com.hhh.paws.ui.petProfile.menu.procedures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.paws.R
import com.hhh.paws.database.model.SurgicalProcedure
import com.hhh.paws.ui.petProfile.menu.ItemClickListener

class ProceduresAdapter: RecyclerView.Adapter<ProceduresAdapter.ProceduresViewHolder>() {

    inner class ProceduresViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val surgicalProceduresContainer: CardView = itemView.findViewById(R.id.surgicalProceduresContainer)
        val typeSurgicalProcedureItem: TextView = itemView.findViewById(R.id.typeSurgicalProcedureItem)
        val nameSurgicalProcedureItem: TextView = itemView.findViewById(R.id.nameSurgicalProcedureItem)
        val dateSurgicalProcedureItem: TextView = itemView.findViewById(R.id.dateSurgicalProcedureItem)
        val anesthesiaSurgicalProcedureItem: TextView = itemView.findViewById(R.id.anesthesiaSurgicalProcedureItem)
        val veterinarianSurgicalProcedureItem: TextView = itemView.findViewById(R.id.veterinarianSurgicalProcedureItem)
    }

    private val callback: DiffUtil.ItemCallback<SurgicalProcedure> = object : DiffUtil.ItemCallback<SurgicalProcedure>() {
            override fun areItemsTheSame(oldItem: SurgicalProcedure, newItem: SurgicalProcedure): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SurgicalProcedure, newItem: SurgicalProcedure): Boolean {
                return if (oldItem.id != newItem.id) {
                    false
                } else if (oldItem.anesthesia != newItem.anesthesia) {
                    false
                } else if (oldItem.name != newItem.name) {
                    false
                } else if (oldItem.date != newItem.date) {
                    false
                } else if (oldItem.description != newItem.description) {
                    false
                } else if (oldItem.type != newItem.type) {
                    false
                } else {
                    oldItem.veterinarian == newItem.veterinarian
                }
            }
        }

    private val differ = AsyncListDiffer(this, callback)

    fun setDiffer(procedureList: List<SurgicalProcedure>) {
        differ.submitList(procedureList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProceduresViewHolder {
        return ProceduresViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.procedure_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProceduresViewHolder, position: Int) {
        val procedure = differ.currentList[position]
        holder.typeSurgicalProcedureItem.text = procedure.type
        holder.anesthesiaSurgicalProcedureItem.text = procedure.anesthesia
        holder.nameSurgicalProcedureItem.text = procedure.name
        holder.dateSurgicalProcedureItem.text = procedure.date
        holder.veterinarianSurgicalProcedureItem.text = procedure.veterinarian
        holder.itemView.setOnClickListener { clickListener!!.onItemClickListener(differ.currentList[holder.adapterPosition]) }
        holder.itemView.setOnLongClickListener {
            clickListener!!.onItemLongClickListener(
                differ.currentList[holder.adapterPosition],
                holder.surgicalProceduresContainer
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