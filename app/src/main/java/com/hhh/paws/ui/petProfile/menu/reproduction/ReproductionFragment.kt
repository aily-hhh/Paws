package com.hhh.paws.ui.petProfile.menu.reproduction

import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hhh.paws.R
import com.hhh.paws.database.model.Reproduction
import com.hhh.paws.database.viewModel.ReproductionViewModel
import com.hhh.paws.databinding.FragmentReproductionBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReproductionFragment : Fragment() {

    private var binding: FragmentReproductionBinding? = null
    private var recyclerReproduction: RecyclerView? = null
    private var addReproductionButton: FloatingActionButton? = null
    private var notElemReproduction: TextView? = null
    private var addTextView: TextView? = null
    private var addArrow: ImageView? = null
    private var progressBarReproduction: ProgressBar? = null
    private var reproductionAdapter: ReproductionAdapter? = null
    private var petName: String? = null
    private val viewModelReproduction by viewModels<ReproductionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReproductionBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petName = name
        recyclerReproduction = binding!!.recyclerReproduction
        initAdapter()
        reproductionAdapter!!.setClickListener(object : ItemClickListener {
            override fun onItemClickListener(`object`: Any) {
                val bundle = Bundle()
                bundle.putParcelable("reproduction", `object` as Parcelable)
                findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(R.id.action_nav_reproduction_to_detailReproductionFragment, bundle)
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            override fun onItemLongClickListener(`object`: Any, cardView: CardView) {
                showPopUp(`object` as Reproduction, cardView)
            }
        })

        progressBarReproduction = binding!!.progressBarReproduction
        notElemReproduction = binding!!.notElemReproduction
        addTextView = binding!!.addTextView
        addArrow = binding!!.addArrow
        addReproductionButton = binding!!.addReproductionButton

        addReproductionButton!!.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_reproduction_to_detailReproductionFragment)
        }

        viewModelReproduction.getAllReproduction(petName!!)
        viewModelReproduction.allReproduction.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarReproduction!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarReproduction!!.visibility = View.INVISIBLE
                    reproductionAdapter!!.setDiffer(it.data)
                    if (reproductionAdapter!!.itemCount == 0) {
                        notElemReproduction!!.visibility = View.VISIBLE
                        addArrow!!.visibility = View.VISIBLE
                        addTextView!!.visibility = View.VISIBLE
                    } else {
                        notElemReproduction!!.visibility = View.INVISIBLE
                        addArrow!!.visibility = View.INVISIBLE
                        addTextView!!.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarReproduction!!.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModelReproduction.removeReproduction.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarReproduction!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarReproduction!!.visibility = View.INVISIBLE
                    viewModelReproduction.getAllReproduction(petName!!)
                }
                is UiState.Failure -> {
                    progressBarReproduction!!.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initAdapter() {
        reproductionAdapter = ReproductionAdapter()
        recyclerReproduction?.apply {
            adapter = reproductionAdapter
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun showPopUp(currentReproduction: Reproduction, cardView: CardView) {
        val popupMenu = PopupMenu(this.context, cardView)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.deleteMenu) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setIcon(R.mipmap.logo_paws)
                alertDialog.setTitle(R.string.delete_note)
                alertDialog.setPositiveButton(
                    R.string.yes
                ) { dialogInterface, i ->
                    viewModelReproduction!!.deleteReproduction(
                        petName!!,
                        currentReproduction.id
                    )
                }
                alertDialog.setNegativeButton(
                    R.string.no
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                alertDialog.show()
                return@OnMenuItemClickListener true
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