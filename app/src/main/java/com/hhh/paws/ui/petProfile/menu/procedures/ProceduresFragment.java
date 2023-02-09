package com.hhh.paws.ui.petProfile.menu.procedures;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.model.SurgicalProcedure;
import com.hhh.paws.database.viewModel.ProcedureViewModel;
import com.hhh.paws.databinding.FragmentProceduresBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;

import java.util.List;

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

    private LiveData<List<SurgicalProcedure>> listLiveData = new MutableLiveData<>();

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
        progressBarProcedures.setVisibility(View.VISIBLE);

        recyclerProcedures = getBinding().recyclerProcedures;
        initAdapter();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("procedure", (SurgicalProcedure) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_procedures_to_detailProceduresFragment, bundle);
            }

            @Override
            public void onItemLongClickListener(Object object, CardView cardView) {
                showPopUp((SurgicalProcedure) object, cardView);
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

        getNewList();
    }

    private void getNewList() {
        disposableGet = viewModelProcedure.getAllProcedures(petNameThis)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(procedures -> {
                    adapter.setDiffer(procedures);
                    progressBarProcedures.setVisibility(View.INVISIBLE);
                    if (adapter.getItemCount() == 0) {
                        notElemProcedures.setVisibility(View.VISIBLE);
                        addArrow.setVisibility(View.VISIBLE);
                        addTextView.setVisibility(View.VISIBLE);
                    } else {
                        notElemProcedures.setVisibility(View.INVISIBLE);
                        addArrow.setVisibility(View.INVISIBLE);
                        addTextView.setVisibility(View.INVISIBLE);
                    }
                }, error -> {
                    Log.d("Procedure", error.getLocalizedMessage());
                });
    }

    private void initAdapter() {
        adapter = new ProceduresAdapter();
        recyclerProcedures.setAdapter(adapter);
        recyclerProcedures.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
    }

    private void showPopUp(SurgicalProcedure currentProcedure, CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this.getContext(), cardView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteMenu) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                    alertDialog.setIcon(R.mipmap.logo_paws);
                    alertDialog.setTitle("deleteQuestion");
                    alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModelProcedure.deleteProcedure(petNameThis, currentProcedure);
                            getNewList();
                        }
                    });
                    alertDialog.setNegativeButton("no", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                    return true;
                }
                return false;
            }
        });
        popupMenu.inflate(R.menu.long_click_menu);
        popupMenu.show();
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