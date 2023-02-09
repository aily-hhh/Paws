package com.hhh.paws.ui.petProfile.menu.notes

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.paws.R
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.viewModel.NotesViewModel
import com.hhh.paws.databinding.FragmentNotesBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.UiState
import com.hhh.paws.util.toast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotesFragment: Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val mBinding get() = _binding!!

    private var recyclerNotes: RecyclerView? = null
    private var addNotesButton: FloatingActionButton? = null
    private var notElemNotes: TextView? = null
    private var progressBarNotes: ProgressBar? = null
    private var addArrow: ImageView? = null
    private var addTextView: TextView? = null

    private val viewModelNotes by viewModels<NotesViewModel>()
    private var petNameThis: String? = null
    private var notesAdapter: NotesAdapter? = null

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
        notesAdapter?.setClickListener(object : ItemClickListener {
            override fun onItemClickListener(it: Any) {
                val bundle = bundleOf("note" to it)
                findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(R.id.action_nav_notes_to_detailNoteFragment, bundle)
            }

            override fun onItemLongClickListener(it: Any, cardView: CardView) {
                showPopUp(it as Notes, cardView)
            }
        })

        viewModelNotes.update.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarNotes?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    viewModelNotes.getAllNotes(petNameThis!!)
                }
                is UiState.Failure -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                }
            }
        }

        addNotesButton = mBinding.addNotesButton
        addNotesButton?.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_notes_to_detailNoteFragment)
        }

        viewModelNotes.getAllNotes(petNameThis!!)
        viewModelNotes.allNotes.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarNotes?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    notesAdapter?.setDiffer(it.data)
                    if (notesAdapter?.itemCount == 0) {
                        notElemNotes?.visibility = View.VISIBLE
                        addArrow?.visibility = View.VISIBLE
                        addTextView?.visibility = View.VISIBLE
                    } else {
                        notElemNotes?.visibility = View.INVISIBLE
                        addArrow?.visibility = View.INVISIBLE
                        addTextView?.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
            }
        }

        viewModelNotes.update.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarNotes?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    viewModelNotes.getAllNotes(petNameThis!!)
                }
                is UiState.Failure -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
                else -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                }
            }
        }

        viewModelNotes.delete.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarNotes?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    viewModelNotes.getAllNotes(petNameThis!!)
                }
                is UiState.Failure -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
                else -> {
                    progressBarNotes?.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun initAdapter() {
        notesAdapter = NotesAdapter()
        recyclerNotes?.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        }
    }

    private fun showPopUp(noteForMenu: Notes, cardView: CardView) {
        val popup = PopupMenu(requireContext(), cardView)
        popup.inflate(R.menu.long_click_menu_note)
        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.pinMenuNote -> {
                    noteForMenu.pinned = true
                    viewModelNotes.updateNote(
                        noteForMenu,
                        petNameThis!!
                    )
                }
                R.id.deleteMenuNote -> {
                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setIcon(R.mipmap.logo_paws)
                    alertDialog.setTitle("")
                    alertDialog.setPositiveButton(R.string.delete_yes,
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            val alertDialog = AlertDialog.Builder(requireContext())
                            alertDialog.setIcon(R.mipmap.logo_paws)
                            alertDialog.setTitle("")
                            alertDialog.setPositiveButton("yes",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    viewModelNotes.deleteNote(
                                        noteForMenu.id,
                                        petNameThis!!
                                    )
                                })
                            alertDialog.setNeutralButton("no",
                                DialogInterface.OnClickListener { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                })
                            alertDialog.show()
                        })
                    alertDialog.setNeutralButton("no",
                        DialogInterface.OnClickListener { dialogInterface, i ->
                            dialogInterface.dismiss()
                        })
                    alertDialog.show()
                }
            }
            false
        }
        popup.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}