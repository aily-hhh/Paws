package com.hhh.paws.ui.petProfile.menu.treatment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.model.Reproduction;
import com.hhh.paws.database.model.Treatment;
import com.hhh.paws.database.viewModel.TreatmentViewModel;
import com.hhh.paws.databinding.FragmentTreatmentBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;
import com.hhh.paws.util.PetName;
import com.hhh.paws.util.UiState;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class TreatmentFragment extends Fragment {

    private FragmentTreatmentBinding _binding = null;
    private FragmentTreatmentBinding getBinding() {
        return _binding;
    }

    private RecyclerView recyclerTreatment;
    private FloatingActionButton addTreatmentButton;
    private TextView notElemTreatment, addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarTreatment;

    private TreatmentViewModel viewModelTreatment;
    private TreatmentAdapter adapter;
    private String petName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentTreatmentBinding.inflate(inflater, container, false);
        viewModelTreatment = new ViewModelProvider(TreatmentFragment.this).get(TreatmentViewModel.class);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petName = PetName.INSTANCE.getName();

        recyclerTreatment = getBinding().recyclerTreatment;
        initAdapter();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("treatment", (Parcelable) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_treatment_to_detailTreatmentFragment, bundle);
            }

            @Override
            public void onItemLongClickListener(Object object, CardView cardView) {
                showPopUp((Treatment) object, cardView);
            }
        });

        notElemTreatment = getBinding().notElemTreatment;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;
        progressBarTreatment = getBinding().progressBarTreatment;

        addTreatmentButton = getBinding().addTreatmentButton;
        addTreatmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_treatment_to_detailTreatmentFragment);
            }
        });

        viewModelTreatment.getAllTreatment().observe(getViewLifecycleOwner(), new Observer<UiState<List<Treatment>>>() {
            @Override
            public void onChanged(UiState<List<Treatment>> listUiState) {
                if (listUiState == UiState.Loading.INSTANCE) {
                    progressBarTreatment.setVisibility(View.VISIBLE);
                } else if (listUiState.getClass() == UiState.Success.class) {
                    progressBarTreatment.setVisibility(View.INVISIBLE);
                    adapter.setDiffer(((UiState.Success<List<Treatment>>) listUiState).getData());
                    if (adapter.getItemCount() == 0) {
                        notElemTreatment.setVisibility(View.VISIBLE);
                        addArrow.setVisibility(View.VISIBLE);
                        addTextView.setVisibility(View.VISIBLE);
                    } else {
                        notElemTreatment.setVisibility(View.INVISIBLE);
                        addArrow.setVisibility(View.INVISIBLE);
                        addTextView.setVisibility(View.INVISIBLE);
                    }
                } else if (listUiState.getClass() == UiState.Failure.class) {
                    progressBarTreatment.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModelTreatment.getRemoveTreatment().observe(getViewLifecycleOwner(), new Observer<UiState<String>>() {
            @Override
            public void onChanged(UiState<String> stringUiState) {
                if (stringUiState == UiState.Loading.INSTANCE) {
                    progressBarTreatment.setVisibility(View.VISIBLE);
                } else if (stringUiState.getClass() == UiState.Success.class) {
                    progressBarTreatment.setVisibility(View.INVISIBLE);
                    viewModelTreatment.getAllTreatment(petName);
                } else if (stringUiState.getClass() == UiState.Failure.class) {
                    progressBarTreatment.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModelTreatment.getAllTreatment(petName);
    }

    private void initAdapter() {
        adapter = new TreatmentAdapter();
        recyclerTreatment.setAdapter(adapter);
        recyclerTreatment.setLayoutManager(new StaggeredGridLayoutManager(
                1, LinearLayoutManager.VERTICAL
        ));
    }

    private void showPopUp(Treatment currentTreatment, CardView cardView) {
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
                            viewModelTreatment.deleteTreatment(petName, currentTreatment.getId());
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
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }
}