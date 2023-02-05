package com.hhh.paws.ui.petProfile.menu.procedures;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.viewModel.ProcedureViewModel;
import com.hhh.paws.databinding.FragmentProceduresBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;


@AndroidEntryPoint
public class ProceduresFragment extends Fragment {

    private FragmentProceduresBinding _binding = null;
    private FragmentProceduresBinding mBinding = _binding;

    private RecyclerView recyclerProcedures;
    private FloatingActionButton addProceduresButton;
    private TextView notElemProcedures,addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarProcedures;

    private ProcedureViewModel viewModelProcedure;
    private ProceduresAdapter adapter;
    private String petNameThis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentProceduresBinding.inflate(inflater, container, false);
        viewModelProcedure = new ViewModelProvider(ProceduresFragment.this).get(ProcedureViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petNameThis = "Котик";

        notElemProcedures = mBinding.notElemProcedures;
        addTextView = mBinding.addTextView;
        addArrow = mBinding.addArrow;
        progressBarProcedures = mBinding.progressBarProcedures;

        recyclerProcedures = mBinding.recyclerProcedures;
        initAdapter();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("procedure", (Parcelable) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_procedures_to_detailProceduresFragment, bundle);
            }

            @Override
            public void onItemLongClickListener(Object object) {
                // popup menu
            }
        });

        addProceduresButton = mBinding.addProceduresButton;
        addProceduresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_procedures_to_detailProceduresFragment);
            }
        });

        viewModelProcedure.getAllProcedures(petNameThis);
    }

    private void initAdapter() {
        adapter = new ProceduresAdapter();
        recyclerProcedures.setAdapter(adapter);
        recyclerProcedures.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
    }
}