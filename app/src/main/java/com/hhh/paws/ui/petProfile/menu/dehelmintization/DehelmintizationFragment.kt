package com.hhh.paws.ui.petProfile.menu.dehelmintization

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
import com.hhh.paws.database.model.Dehelmintization
import com.hhh.paws.database.viewModel.DehelmintizationViewModel
import com.hhh.paws.databinding.FragmentDehelmintizationBinding
import com.hhh.paws.ui.petProfile.menu.ItemClickListener
import com.hhh.paws.util.PetName.name
import com.hhh.paws.util.UiState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DehelmintizationFragment : Fragment() {

    private var binding: FragmentDehelmintizationBinding? = null
    private var recyclerDehelmintization: RecyclerView? = null
    private var addDehelmintizationButton: FloatingActionButton? = null
    private var notElemDehelmintization: TextView? = null
    private var addTextView: TextView? = null
    private var addArrow: ImageView? = null
    private var progressBarDehelmintization: ProgressBar? = null
    private var dehelmintizationAdapter: DehelmintizationAdapter? = null
    private var petNameThis: String? = null
    private val viewModelDehelmintization by viewModels<DehelmintizationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDehelmintizationBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        petNameThis = name
        recyclerDehelmintization = binding!!.recyclerDehelmintization

        initAdapter()
        dehelmintizationAdapter!!.setClickListener(object : ItemClickListener {
            override fun onItemClickListener(`object`: Any) {
                val bundle = Bundle()
                bundle.putParcelable("dehelmintization", `object` as Parcelable)
                findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                    .navigate(
                        R.id.action_nav_dehelmintization_to_detailDehelmintizationFragment,
                        bundle
                    )
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            override fun onItemLongClickListener(`object`: Any, cardView: CardView) {
                showPopUp(`object` as Dehelmintization, cardView)
            }
        })

        addDehelmintizationButton = binding!!.addDehelmintizationButton
        addDehelmintizationButton!!.setOnClickListener {
            findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                .navigate(R.id.action_nav_dehelmintization_to_detailDehelmintizationFragment)
        }

        notElemDehelmintization = binding!!.notElemDehelmintization
        addTextView = binding!!.addTextView
        addArrow = binding!!.addArrow
        progressBarDehelmintization = binding!!.progressBarDehelmintization

        viewModelDehelmintization.getAllDehelmintization(petNameThis!!)
        viewModelDehelmintization.dehelmintizationList.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarDehelmintization!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    progressBarDehelmintization!!.visibility = View.INVISIBLE
                    dehelmintizationAdapter!!.setDiffer(it.data)
                    if (dehelmintizationAdapter!!.itemCount == 0) {
                        notElemDehelmintization!!.visibility = View.VISIBLE
                        addArrow!!.visibility = View.VISIBLE
                        addTextView!!.visibility = View.VISIBLE
                    } else {
                        notElemDehelmintization!!.visibility = View.INVISIBLE
                        addArrow!!.visibility = View.INVISIBLE
                        addTextView!!.visibility = View.INVISIBLE
                    }
                }
                is UiState.Failure -> {
                    progressBarDehelmintization!!.visibility = View.INVISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModelDehelmintization.delete.observe(viewLifecycleOwner) {
            when(it) {
                is UiState.Loading -> {
                    progressBarDehelmintization!!.visibility = View.VISIBLE
                }
                is UiState.Success -> {
                    viewModelDehelmintization.getAllDehelmintization(petNameThis!!)
                }
                is UiState.Failure -> {
                    progressBarDehelmintization!!.visibility = View.INVISIBLE
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
        dehelmintizationAdapter = DehelmintizationAdapter()
        recyclerDehelmintization?.apply {
            adapter = dehelmintizationAdapter
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private fun showPopUp(currentDehelmintization: Dehelmintization, cardView: CardView) {
        val popupMenu = PopupMenu(this.context, cardView)
        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.deleteMenu) {
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.setIcon(R.mipmap.logo_paws)
                alertDialog.setTitle(R.string.delete_note)
                alertDialog.setPositiveButton(
                    R.string.yes
                ) { dialogInterface, i ->
                    viewModelDehelmintization!!.deleteDehelmintization(
                        currentDehelmintization.id!!,
                        petNameThis!!
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