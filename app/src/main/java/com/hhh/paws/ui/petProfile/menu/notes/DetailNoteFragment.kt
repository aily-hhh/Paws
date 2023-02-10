package com.hhh.paws.ui.petProfile.menu.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.hhh.paws.R
import com.hhh.paws.database.model.Notes
import com.hhh.paws.database.viewModel.NotesViewModel
import com.hhh.paws.databinding.FragmentDetailNoteBinding
import com.hhh.paws.util.PetName
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class DetailNoteFragment : Fragment() {

    private var titleNotesDetail: EditText? = null
    private var descriptionNotesDetail: EditText? = null
    private var progressBarNotesDetail: ProgressBar? = null

    private var _binding: FragmentDetailNoteBinding? = null
    private val mBinding get() = _binding!!

    private var noteThis: Notes? = null
    private var petName: String? = null

    private val viewModelNotes by viewModels<NotesViewModel>()
    private val bundleArgs: DetailNoteFragmentArgs by navArgs()

    private var calendar = Calendar.getInstance()
    @SuppressLint("SimpleDateFormat")
    private var dateFormat = SimpleDateFormat("dd.MM.yyyy ',' HH:mm")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailNoteBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        petName = PetName.name
        noteThis = bundleArgs.note

        titleNotesDetail = mBinding.titleNotesDetail
        descriptionNotesDetail = mBinding.descriptionNotesDetail
        progressBarNotesDetail = mBinding.progressBarNotesDetail

        if (noteThis != null) {
            titleNotesDetail?.setText(noteThis?.title.toString())
            descriptionNotesDetail?.setText(noteThis?.description.toString())
        }

        viewModelNotes.update.observe(viewLifecycleOwner) {
            when (it) {
                is UiState.Loading -> {
                    progressBarNotesDetail?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarNotesDetail?.visibility = View.INVISIBLE
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .popBackStack()
                }
                is UiState.Failure -> {
                    progressBarNotesDetail?.visibility = View.INVISIBLE
                }
                else -> {
                    progressBarNotesDetail?.visibility = View.INVISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_fragment_vetpassport, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val newNote = Notes()
                if (noteThis != null) {
                    newNote.id = noteThis!!.id
                } else {
                    newNote.id = UUID.randomUUID().toString()
                }
                newNote.pinned = false
                newNote.title = titleNotesDetail?.text.toString().trim()
                newNote.description = descriptionNotesDetail?.text.toString().trim()
                newNote.date = dateFormat.format(calendar.time)

                viewModelNotes.updateNote(newNote, petName!!)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}