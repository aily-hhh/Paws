package com.hhh.paws.ui.petProfile.menu.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentDetailNoteBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailNoteFragment : Fragment() {

    private lateinit var saveNote: ImageView
    private lateinit var backNote: ImageView
    private lateinit var titleNotesDetail: EditText
    private lateinit var descriptionNotesDetail: EditText

    private var _binding: FragmentDetailNoteBinding? = null
    private val mBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailNoteBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        saveNote = mBinding.saveNote
        backNote = mBinding.backNote
        titleNotesDetail = mBinding.titleNotesDetail
        descriptionNotesDetail = mBinding.descriptionNotesDetail
    }
}