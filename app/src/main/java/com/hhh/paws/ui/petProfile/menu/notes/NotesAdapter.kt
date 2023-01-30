package com.hhh.paws.ui.petProfile.menu.notes

import android.annotation.SuppressLint
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
        val title = view.findViewById<TextView>(R.id.titleNotes)
        val description = view.findViewById<TextView>(R.id.descriptionNotes)
        val date = view.findViewById<TextView>(R.id.dateNotes)
        val pin = view.findViewById<ImageView>(R.id.pinnedNotes)
    }

    private val callback = object: DiffUtil.ItemCallback<Notes>() {
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean {
            return oldItem == newItem
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