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

class ReproductionAdapter: RecyclerView.Adapter<ReproductionAdapter.ReproductionViewHolder>() {

    inner class ReproductionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val reproductionContainer: CardView = view.findViewById(R.id.reproductionContainer)
        val dateOfHeatItem: TextView = view.findViewById(R.id.dateOfHeatItem)
        val dateOfMatingItem: TextView = view.findViewById(R.id.dateOfMatingItem)
        val numberOfTheLitterItem: TextView = view.findViewById(R.id.numberOfTheLitterItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReproductionViewHolder {
        return ReproductionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.reproduction_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReproductionViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }
}