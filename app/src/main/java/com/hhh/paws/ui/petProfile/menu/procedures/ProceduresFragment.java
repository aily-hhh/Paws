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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.database.viewModel.ProcedureViewModel;
import com.hhh.paws.databinding.FragmentProceduresBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


@AndroidEntryPoint
public class ProceduresFragment extends Fragment {

    private FragmentProceduresBinding _binding = null;
    private FragmentProceduresBinding getBinding() {
        return _binding;
    }

    private RecyclerView recyclerProcedures;
    private FloatingActionButton addProceduresButton;
    private TextView notElemProcedures,addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarProcedures;

    private Disposable disposableGet;
    private ProcedureViewModel viewModelProcedure;
    private ProceduresAdapter adapter;
    private String petNameThis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentProceduresBinding.inflate(inflater, container, false);
        viewModelProcedure = new ViewModelProvider(ProceduresFragment.this).get(ProcedureViewModel.class);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petNameThis = "Котик";

        notElemProcedures = getBinding().notElemProcedures;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;
        progressBarProcedures = getBinding().progressBarProcedures;

        recyclerProcedures = getBinding().recyclerProcedures;
        initAdapter();
        adapter.differ.getCurrentList().clear();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("procedure", (SurgicalProcedure) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_procedures_to_detailProceduresFragment, bundle);
            }

            @Override
            public void onItemLongClickListener(Object object) {
                // popup menu
            }
        });

        addProceduresButton = getBinding().addProceduresButton;
        addProceduresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_procedures_to_detailProceduresFragment);
            }
        });

        disposableGet = viewModelProcedure.getAllProcedures(petNameThis)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(procedure -> {
                    adapter.differ.getCurrentList().add(procedure);
                }, error -> {
                    Log.d("Procedure", error.getLocalizedMessage());
                });
    }

    private void initAdapter() {
        adapter = new ProceduresAdapter();
        recyclerProcedures.setAdapter(adapter);
        recyclerProcedures.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposableGet.dispose();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }
}