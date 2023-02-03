package com.hhh.paws.ui.petProfile.menu.notes

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.paws.MainActivity
import com.hhh.paws.R
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.viewModel.NotesViewModel
import com.hhh.paws.databinding.FragmentNotesBinding
import com.hhh.paws.ui.petProfile.VetPassportActivity
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotesFragment: Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var recyclerNotes: RecyclerView
    private lateinit var addNotesButton: FloatingActionButton
    private lateinit var notElemNotes: TextView
    private lateinit var progressBarNotes: ProgressBar
    private lateinit var addArrow: ImageView
    private lateinit var addTextView: TextView

    private val viewModelNotes by viewModels<NotesViewModel>()
    private lateinit var petNameThis: String
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petNameThis = "Котик"

        notElemNotes = mBinding.notElemNotes
        progressBarNotes = mBinding.progressBarNotes
        addArrow = mBinding.addArrow
        addTextView = mBinding.addTextView

        recyclerNotes = mBinding.recyclerNotes
        initAdapter()
        notesAdapter.setOnItemClickListener {
            val bundle = bundleOf("note" to it)
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_notes_to_detailNoteFragment, bundle)
        }
        notesAdapter.setOnItemLongClickListener {
            showPopUpMenu(it)
        }

        viewModelNotes.update.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {

                }
                is UiState.Success -> {
                    toast("Long click")
                }
                is UiState.Failure -> {

                }
            }
        }

        addNotesButton = mBinding.addNotesButton
        addNotesButton.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_notes_to_detailNoteFragment)
        }

        viewModelNotes.getAllNotes(petNameThis)
        viewModelNotes.allNotes.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarNotes.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarNotes.visibility = View.INVISIBLE
                    notesAdapter.differ.submitList(it.data)
                    if (notesAdapter.differ.currentList.isEmpty()) {
                        notElemNotes.visibility = View.VISIBLE
                        addArrow.visibility = View.VISIBLE
                        addTextView.visibility = View.VISIBLE
                    } else {
                        notElemNotes.visibility = View.INVISIBLE
                        addArrow.visibility = View.INVISIBLE
                        addTextView.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarNotes.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
            }
        }
    }

    private fun initAdapter() {
        notesAdapter = NotesAdapter()
        recyclerNotes.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    private fun showPopUpMenu(noteForMenu: Notes) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.long_click_menu)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.pinMenu -> {
                    noteForMenu.pinned = true
                    viewModelNotes.updateNote(
                        noteForMenu,
                        petNameThis
                    )
                }
                R.id.deleteMenu -> {
                    viewModelNotes.deleteNote(
                        noteForMenu.id,
                        petNameThis
                    )
                }
            }
            false
        }
        popup.show()
    }

}