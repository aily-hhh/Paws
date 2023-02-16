package com.hhh.paws.ui.petProfile.menu.reproduction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.CustomPopUpMenu;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.model.Dehelmintization;
import com.hhh.paws.database.model.Reproduction;
import com.hhh.paws.database.viewModel.ReproductionViewModel;
import com.hhh.paws.databinding.FragmentReproductionBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;
import com.hhh.paws.util.PetName;
import com.hhh.paws.util.UiState;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ReproductionFragment extends Fragment {

    private FragmentReproductionBinding _binding = null;
    private FragmentReproductionBinding getBinding() {
        return _binding;
    }

    private RecyclerView recyclerReproduction;
    private FloatingActionButton addReproductionButton;
    private TextView notElemReproduction, addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarReproduction;

    private ReproductionViewModel viewModelReproduction;
    private ReproductionAdapter adapter;
    private String petName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModelReproduction = new ViewModelProvider(ReproductionFragment.this).get(ReproductionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentReproductionBinding.inflate(inflater, container, false);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petName = PetName.INSTANCE.getName();

        recyclerReproduction = getBinding().recyclerReproduction;
        initAdapter();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("reproduction", (Parcelable) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_reproduction_to_detailReproductionFragment, bundle);
            }

            @Override
            public void onItemLongClickListener(Object object, CardView cardView) {
                showPopUp((Reproduction) object, cardView);
            }
        });

        progressBarReproduction = getBinding().progressBarReproduction;
        notElemReproduction = getBinding().notElemReproduction;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;

        addReproductionButton = getBinding().addReproductionButton;
        addReproductionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_reproduction_to_detailReproductionFragment);
            }
        });

        viewModelReproduction.getAllReproduction().observe(getViewLifecycleOwner(),
                new Observer<UiState<List<Reproduction>>>() {
            @Override
            public void onChanged(UiState<List<Reproduction>> listUiState) {
                if (listUiState == UiState.Loading.INSTANCE) {
                    progressBarReproduction.setVisibility(View.VISIBLE);
                } else if (listUiState.getClass() == UiState.Success.class) {
                    progressBarReproduction.setVisibility(View.INVISIBLE);
                    adapter.setDiffer(((UiState.Success<List<Reproduction>>) listUiState).getData());
                    if (adapter.getItemCount() == 0) {
                        notElemReproduction.setVisibility(View.VISIBLE);
                        addArrow.setVisibility(View.VISIBLE);
                        addTextView.setVisibility(View.VISIBLE);
                    } else {
                        notElemReproduction.setVisibility(View.INVISIBLE);
                        addArrow.setVisibility(View.INVISIBLE);
                        addTextView.setVisibility(View.INVISIBLE);
                    }
                } else if (listUiState.getClass() == UiState.Failure.class) {
                    progressBarReproduction.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModelReproduction.getAllReproduction(petName);

        viewModelReproduction.getRemoveReproduction().observe(getViewLifecycleOwner(),
                new Observer<UiState<String>>() {
                    @Override
                    public void onChanged(UiState<String> listUiState) {
                        if (listUiState == UiState.Loading.INSTANCE) {
                            progressBarReproduction.setVisibility(View.VISIBLE);
                        } else if (listUiState.getClass() == UiState.Success.class) {
                            progressBarReproduction.setVisibility(View.INVISIBLE);
                            viewModelReproduction.getAllReproduction(petName);
                        } else if (listUiState.getClass() == UiState.Failure.class) {
                            progressBarReproduction.setVisibility(View.INVISIBLE);
                            Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initAdapter() {
        adapter = new ReproductionAdapter();
        recyclerReproduction.setAdapter(adapter);
        recyclerReproduction.setLayoutManager(new StaggeredGridLayoutManager(
                1, LinearLayoutManager.VERTICAL
        ));
    }

    private void showPopUp(Reproduction currentReproduction, CardView cardView) {
        CustomPopUpMenu popupMenu = new CustomPopUpMenu(this.getContext(), cardView);
        popupMenu.setOnMenuItemClickListener(new CustomPopUpMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteMenu) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                    alertDialog.setIcon(R.mipmap.logo_paws);
                    alertDialog.setTitle(R.string.delete_note);
                    alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModelReproduction.deleteReproduction(petName, currentReproduction.getId());
                        }
                    });
                    alertDialog.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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