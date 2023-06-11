package com.hhh.paws.ui.petProfile.menu.procedures

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.paws.R
import com.hhh.paws.database.model.SurgicalProcedure
import com.hhh.paws.database.viewModel.ProcedureViewModel
import com.hhh.paws.databinding.FragmentProceduresBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProceduresFragment : Fragment() {

    private var binding: FragmentProceduresBinding? = null
    private var recyclerProcedures: RecyclerView? = null
    private var addProceduresButton: FloatingActionButton? = null
    private var notElemProcedures: TextView? = null
    private var addTextView: TextView? = null
    private var addArrow: ImageView? = null
    private var progressBarProcedures: ProgressBar? = null
    private val viewModelProcedure by viewModels<ProcedureViewModel>()
    private var procedureAdapter: ProceduresAdapter? = null
    private var petNameThis: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProceduresBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petNameThis = name
        notElemProcedures = binding!!.notElemProcedures
        addTextView = binding!!.addTextView
        addArrow = binding!!.addArrow
        progressBarProcedures = binding!!.progressBarProcedures

        progressBarProcedures!!.visibility = View.VISIBLE
        recyclerProcedures = binding!!.recyclerProcedures

        initAdapter()
        procedureAdapter!!.setClickListener(object : ItemClickListener {
            override fun onItemClickListener(it: Any) {
                val bundle = Bundle()
                bundle.putParcelable("procedure", it as SurgicalProcedure)
                findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(R.id.action_nav_procedures_to_detailProceduresFragment, bundle)
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            override fun onItemLongClickListener(it: Any, cardView: CardView) {
                showPopUp(it as SurgicalProcedure, cardView)
            }
        })

        addProceduresButton = binding!!.addProceduresButton
        addProceduresButton!!.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_procedures_to_detailProceduresFragment)
        }

        viewModelProcedure.getAllProcedures(petNameThis!!)
        viewModelProcedure.allProcedures.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarProcedures?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarProcedures?.visibility = View.INVISIBLE
                    procedureAdapter?.setDiffer(it.data)
                    if (procedureAdapter?.itemCount == 0) {
                        notElemProcedures?.visibility = View.VISIBLE
                        addArrow?.visibility = View.VISIBLE
                        addTextView?.visibility = View.VISIBLE
                    } else {
                        notElemProcedures?.visibility = View.INVISIBLE
                        addArrow?.visibility = View.INVISIBLE
                        addTextView?.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarProcedures?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
            }
        }

        viewModelProcedure.removeProcedure.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarProcedures?.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarProcedures?.visibility = View.INVISIBLE
                    viewModelProcedure.getAllProcedures(petNameThis!!)
                }
                is UiState.Failure -> {
                    progressBarProcedures?.visibility = View.INVISIBLE
                    Log.d("UI State", it.error.toString())
                }
                else -> {
                    progressBarProcedures?.visibility = View.INVISIBLE
                }
            }
        }

    }

    private fun initAdapter() {
        procedureAdapter = ProceduresAdapter()
        recyclerProcedures?.apply {
            adapter = procedureAdapter
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun showPopUp(currentProcedure: SurgicalProcedure, cardView: CardView) {
        val popupMenu = PopupMenu(this.context, cardView)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.deleteMenu) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setIcon(R.mipmap.logo_paws)
                alertDialog.setTitle(R.string.delete_note)
                alertDialog.setPositiveButton(
                    R.string.yes
                ) { _, _ ->
                    viewModelProcedure.deleteProcedure(petNameThis!!, currentProcedure)
                    progressBarProcedures!!.visibility = View.VISIBLE
                }
                alertDialog.setNegativeButton(
                    R.string.no
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                alertDialog.show()
            }
            false
        })
        popupMenu.inflate(R.menu.long_click_menu)
        popupMenu.setForceShowIcon(true)
        popupMenu.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}