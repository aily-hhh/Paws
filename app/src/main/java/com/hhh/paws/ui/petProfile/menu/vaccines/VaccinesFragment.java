package com.hhh.paws.ui.petProfile.menu.vaccines;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hhh.paws.R;
import com.hhh.paws.database.model.Reproduction;
import com.hhh.paws.database.model.Vaccine;
import com.hhh.paws.database.viewModel.VaccinesViewModel;
import com.hhh.paws.databinding.FragmentVaccinesBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;
import com.hhh.paws.util.PetName;
import com.hhh.paws.util.UiState;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class VaccinesFragment extends Fragment {

    private FragmentVaccinesBinding _binding = null;
    private FragmentVaccinesBinding getBinding() {
        return _binding;
    }

    private RecyclerView recyclerVaccines;
    private FloatingActionButton addVaccinesButton;
    private TextView notElemVaccines, addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarVaccines;

    private VaccinesViewModel viewModelVaccines;
    private VaccinesAdapter adapter;
    private String petName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding= FragmentVaccinesBinding.inflate(inflater, container, false);
        viewModelVaccines = new ViewModelProvider(VaccinesFragment.this).get(VaccinesViewModel.class);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petName = PetName.INSTANCE.getName();

        recyclerVaccines = getBinding().recyclerVaccines;
        initAdapter();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("vaccine", (Parcelable) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_vaccines_to_detailVaccineFragment, bundle);
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemLongClickListener(Object object, CardView cardView) {
                showPopUp((Vaccine) object, cardView);
            }
        });

        addVaccinesButton = getBinding().addVaccinesButton;
        addVaccinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_vaccines_to_detailVaccineFragment);
            }
        });

        notElemVaccines = getBinding().notElemVaccines;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;
        progressBarVaccines = getBinding().progressBarVaccines;

        viewModelVaccines.getAllVaccines().observe(getViewLifecycleOwner(), new Observer<UiState<List<Vaccine>>>() {
            @Override
            public void onChanged(UiState<List<Vaccine>> listUiState) {
                if (listUiState == UiState.Loading.INSTANCE) {
                    progressBarVaccines.setVisibility(View.VISIBLE);
                } else if (listUiState.getClass() == UiState.Success.class) {
                    progressBarVaccines.setVisibility(View.INVISIBLE);
                    adapter.setDiffer(((UiState.Success<List<Vaccine>>) listUiState).getData());
                    if (adapter.getItemCount() == 0) {
                        notElemVaccines.setVisibility(View.VISIBLE);
                        addArrow.setVisibility(View.VISIBLE);
                        addTextView.setVisibility(View.VISIBLE);
                    } else {
                        notElemVaccines.setVisibility(View.INVISIBLE);
                        addArrow.setVisibility(View.INVISIBLE);
                        addTextView.setVisibility(View.INVISIBLE);
                    }
                } else if (listUiState.getClass() == UiState.Failure.class) {
                    progressBarVaccines.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModelVaccines.getRemoveVaccine().observe(getViewLifecycleOwner(), new Observer<UiState<String>>() {
            @Override
            public void onChanged(UiState<String> stringUiState) {
                if (stringUiState == UiState.Loading.INSTANCE) {
                    progressBarVaccines.setVisibility(View.VISIBLE);
                } else if (stringUiState.getClass() == UiState.Success.class) {
                    progressBarVaccines.setVisibility(View.INVISIBLE);
                    viewModelVaccines.getAllVaccines(petName);
                } else if (stringUiState.getClass() == UiState.Failure.class) {
                    progressBarVaccines.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModelVaccines.getAllVaccines(petName);
    }

    private void initAdapter() {
        adapter = new VaccinesAdapter();
        recyclerVaccines.setAdapter(adapter);
        recyclerVaccines.setLayoutManager(new StaggeredGridLayoutManager(
                1, LinearLayoutManager.VERTICAL
        ));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void showPopUp(Vaccine currentVaccine, CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this.getContext(), cardView);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.deleteMenu) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                    alertDialog.setIcon(R.mipmap.logo_paws);
                    alertDialog.setTitle(R.string.delete_note);
                    alertDialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            viewModelVaccines.deleteVaccine(petName, currentVaccine.getId());
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
        popupMenu.setForceShowIcon(true);
        popupMenu.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _binding = null;
    }
}