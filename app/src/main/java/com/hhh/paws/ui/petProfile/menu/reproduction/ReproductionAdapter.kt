package com.hhh.paws.ui.petProfile.menu.reproduction

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
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.ui.petProfile.menu.ItemClickListener

class ReproductionAdapter: RecyclerView.Adapter<ReproductionAdapter.ReproductionViewHolder>() {

    inner class ReproductionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reproductionContainer: CardView = view.findViewById(R.id.reproductionContainer)
        val dateOfHeatItem: TextView = view.findViewById(R.id.dateOfHeatItem)
        val dateOfMatingItem: TextView = view.findViewById(R.id.dateOfMatingItem)
        val dateOfBirthItem: TextView = view.findViewById(R.id.dateOfBirthItem)
        val numberOfTheLitterItem: TextView = view.findViewById(R.id.numberOfTheLitterItem)
    }

    private val callback = object: DiffUtil.ItemCallback<Reproduction>() {
        override fun areItemsTheSame(oldItem: Reproduction, newItem: Reproduction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reproduction, newItem: Reproduction): Boolean {
            return if (oldItem.id != newItem.id) {
                false
            } else if (oldItem.dateOfBirth != newItem.dateOfBirth) {
                false
            } else if (oldItem.dateOfHeat != newItem.dateOfHeat) {
                false
            } else if (oldItem.dateOfMating != newItem.dateOfMating) {
                false
            } else {
                oldItem.numberOfTheLitter == newItem.numberOfTheLitter
            }
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    fun setDiffer(reproductionList: List<Reproduction>) {
        this.differ.submitList(reproductionList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReproductionViewHolder {
        return ReproductionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.reproduction_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReproductionViewHolder, position: Int) {
        val currentReproduction: Reproduction = this.differ.currentList[position]

        holder.dateOfHeatItem.text = currentReproduction.dateOfHeat
        holder.dateOfMatingItem.text = currentReproduction.dateOfMating
        holder.dateOfBirthItem.text = currentReproduction.dateOfBirth
        holder.numberOfTheLitterItem.text = currentReproduction.numberOfTheLitter

        holder.itemView.apply {
            holder.itemView.setOnClickListener {
                clickListener!!.onItemClickListener(differ.currentList[holder.adapterPosition])
            }

            holder.itemView.setOnLongClickListener {
                clickListener!!.onItemLongClickListener(
                    differ.currentList[holder.adapterPosition],
                    holder.reproductionContainer
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