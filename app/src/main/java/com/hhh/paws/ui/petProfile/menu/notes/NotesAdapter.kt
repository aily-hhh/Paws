package com.hhh.paws.ui.petProfile.menu.notes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hhh.paws.R
import com.hhh.paws.database.model.Notes

class NotesAdapter: RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(view: View): RecyclerView.ViewHolder(view) {
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

    val differ = AsyncListDiffer(this, callback)

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
                onItemClickListener?.let {
                    it(currentNote)
                }
            }
            setOnLongClickListener {
                onItemLongClickListener?.let {
                    it(currentNote)
                }
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Notes) -> Unit)? = null
    fun setOnItemClickListener(listener: (Notes) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((Notes) -> Unit)? = null
    fun setOnItemLongClickListener(listener: (Notes) -> Unit) {
        onItemLongClickListener = listener
    }
}