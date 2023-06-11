package com.hhh.paws.ui.petProfile.menu.vaccines

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
import com.hhh.paws.database.model.Vaccine
import com.hhh.paws.database.viewModel.VaccinesViewModel
import com.hhh.paws.databinding.FragmentVaccinesBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VaccinesFragment : Fragment() {
    private var binding: FragmentVaccinesBinding? = null
    private var recyclerVaccines: RecyclerView? = null
    private var addVaccinesButton: FloatingActionButton? = null
    private var notElemVaccines: TextView? = null
    private var addTextView: TextView? = null
    private var addArrow: ImageView? = null
    private var progressBarVaccines: ProgressBar? = null
    private var petName: String? = null
    private var vaccinesAdapter: VaccinesAdapter? = null
    private val viewModelVaccines by viewModels<VaccinesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVaccinesBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petName = name
        recyclerVaccines = binding!!.recyclerVaccines

        initAdapter()
        vaccinesAdapter!!.setClickListener(object : ItemClickListener {
            override fun onItemClickListener(`object`: Any) {
                val bundle = Bundle()
                bundle.putParcelable("vaccine", `object` as Parcelable)
                findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(R.id.action_nav_vaccines_to_detailVaccineFragment, bundle)
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            override fun onItemLongClickListener(`object`: Any, cardView: CardView) {
                showPopUp(`object` as Vaccine, cardView)
            }
        })

        addVaccinesButton = binding!!.addVaccinesButton
        addVaccinesButton!!.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_vaccines_to_detailVaccineFragment)
        }

        notElemVaccines = binding!!.notElemVaccines
        addTextView = binding!!.addTextView
        addArrow = binding!!.addArrow
        progressBarVaccines = binding!!.progressBarVaccines

        viewModelVaccines.getAllVaccines(petName!!)
        viewModelVaccines.allVaccines.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarVaccines!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarVaccines!!.visibility = View.INVISIBLE
                    vaccinesAdapter!!.setDiffer(it.data)
                    if (vaccinesAdapter!!.itemCount == 0) {
                        notElemVaccines!!.visibility = View.VISIBLE
                        addArrow!!.visibility = View.VISIBLE
                        addTextView!!.visibility = View.VISIBLE
                    } else {
                        notElemVaccines!!.visibility = View.INVISIBLE
                        addArrow!!.visibility = View.INVISIBLE
                        addTextView!!.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarVaccines!!.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModelVaccines.removeVaccine.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarVaccines!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarVaccines!!.visibility = View.INVISIBLE
                    viewModelVaccines.getAllVaccines(petName!!)
                }
                is UiState.Failure -> {
                    progressBarVaccines!!.visibility = View.INVISIBLE
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
        vaccinesAdapter = VaccinesAdapter()
        recyclerVaccines!!.adapter = vaccinesAdapter
        recyclerVaccines!!.layoutManager = StaggeredGridLayoutManager(
            1, LinearLayoutManager.VERTICAL
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun showPopUp(currentVaccine: Vaccine, cardView: CardView) {
        val popupMenu = PopupMenu(this.context, cardView)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.deleteMenu) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setIcon(R.mipmap.logo_paws)
                alertDialog.setTitle(R.string.delete_note)
                alertDialog.setPositiveButton(
                    R.string.yes
                ) { dialogInterface, i ->
                    viewModelVaccines!!.deleteVaccine(
                        petName!!,
                        currentVaccine.id!!
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