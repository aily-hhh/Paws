package com.hhh.paws.ui.petProfile.menu.treatment

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
import com.hhh.paws.database.model.Treatment
import com.hhh.paws.database.viewModel.TreatmentViewModel
import com.hhh.paws.databinding.FragmentTreatmentBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TreatmentFragment : Fragment() {

    private var binding: FragmentTreatmentBinding? = null
    private var recyclerTreatment: RecyclerView? = null
    private var addTreatmentButton: FloatingActionButton? = null
    private var notElemTreatment: TextView? = null
    private var addTextView: TextView? = null
    private var addArrow: ImageView? = null
    private var progressBarTreatment: ProgressBar? = null
    private var treatmentAdapter: TreatmentAdapter? = null
    private var petName: String? = null
    private val viewModelTreatment by viewModels<TreatmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTreatmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petName = name
        recyclerTreatment = binding!!.recyclerTreatment

        initAdapter()
        treatmentAdapter!!.setClickListener(object : ItemClickListener {
            override fun onItemClickListener(`object`: Any) {
                val bundle = Bundle()
                bundle.putParcelable("treatment", `object` as Parcelable)
                findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(R.id.action_nav_treatment_to_detailTreatmentFragment, bundle)
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            override fun onItemLongClickListener(`object`: Any, cardView: CardView) {
                showPopUp(`object` as Treatment, cardView)
            }
        })

        notElemTreatment = binding!!.notElemTreatment
        addTextView = binding!!.addTextView
        addArrow = binding!!.addArrow
        progressBarTreatment = binding!!.progressBarTreatment
        addTreatmentButton = binding!!.addTreatmentButton

        addTreatmentButton!!.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_treatment_to_detailTreatmentFragment)
        }

        viewModelTreatment.getAllTreatment(petName!!)
        viewModelTreatment.allTreatment.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarTreatment!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarTreatment!!.visibility = View.INVISIBLE
                    treatmentAdapter!!.setDiffer(it.data)
                    if (treatmentAdapter!!.itemCount == 0) {
                        notElemTreatment!!.visibility = View.VISIBLE
                        addArrow!!.visibility = View.VISIBLE
                        addTextView!!.visibility = View.VISIBLE
                    } else {
                        notElemTreatment!!.visibility = View.INVISIBLE
                        addArrow!!.visibility = View.INVISIBLE
                        addTextView!!.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarTreatment!!.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModelTreatment.removeTreatment.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarTreatment!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarTreatment!!.visibility = View.INVISIBLE
                    viewModelTreatment.getAllTreatment(petName!!)
                }
                is UiState.Failure -> {
                    progressBarTreatment!!.visibility = View.INVISIBLE
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
        treatmentAdapter = TreatmentAdapter()
        recyclerTreatment!!.adapter = treatmentAdapter
        recyclerTreatment!!.layoutManager = StaggeredGridLayoutManager(
            1, LinearLayoutManager.VERTICAL
        )
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun showPopUp(currentTreatment: Treatment, cardView: CardView) {
        val popupMenu = PopupMenu(this.context, cardView)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.deleteMenu) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setIcon(R.mipmap.logo_paws)
                alertDialog.setTitle(R.string.delete_note)
                alertDialog.setPositiveButton(
                    R.string.yes
                ) { dialogInterface, i ->
                    viewModelTreatment!!.deleteTreatment(
                        petName!!,
                        currentTreatment.id!!
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