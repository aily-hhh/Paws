package com.hhh.paws.ui.petProfile.menu.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.paws.R
import com.hhh.paws.database.model.Notes
import com.hhh.paws.ui.petProfile.menu.ItemClickListener

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val notesContainer: CardView = view.findViewById(R.id.notesContainer)
        val title: TextView = view.findViewById(R.id.titleNotes)
        val description: TextView = view.findViewById(R.id.descriptionNotes)
        val date: TextView = view.findViewById(R.id.dateNotes)
        val pin: ImageView = view.findViewById(R.id.pinnedNotes)
    }

    private val callback = object: DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return when {
                oldItem.id != newItem.id -> false
                oldItem.title != newItem.title -> false
                oldItem.description != newItem.description -> false
                oldItem.date != newItem.date -> false
                oldItem.pinned != newItem.pinned -> false
                else -> true
            }
        }
    }

    private val differ = AsyncListDiffer(this, callback)

    fun setDiffer(notesList: List<Notes>) {
        this.differ.submitList(null)
        this.differ.submitList(notesList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        val currentNote = differ.currentList[position]
        holder.title.text = currentNote.title
        holder.description.text = currentNote.description
        holder.date.text = currentNote.date.toString()
        if (currentNote.pinned) {
            holder.pin.visibility = View.VISIBLE
        } else {
            holder.pin.visibility = View.INVISIBLE
        }

        holder.itemView.apply {
            setOnClickListener {
                clickListener!!.onItemClickListener(differ.currentList[holder.adapterPosition])
            }
            setOnLongClickListener {
                clickListener!!.onItemLongClickListener(
                    differ.currentList[holder.adapterPosition],
                    holder.notesContainer
                )
                false
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var clickListener: ItemClickListener? = null
    fun setClickListener(clickListener: ItemClickListener) {
        this.clickListener = clickListener
    }
}