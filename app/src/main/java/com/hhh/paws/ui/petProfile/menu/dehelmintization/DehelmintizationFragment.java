package com.hhh.paws.ui.petProfile.menu.dehelmintization;

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
import com.hhh.paws.database.model.Dehelmintization;
import com.hhh.paws.database.viewModel.DehelmintizationViewModel;
import com.hhh.paws.databinding.FragmentDehelmintizationBinding;
import com.hhh.paws.ui.petProfile.menu.ItemClickListener;
import com.hhh.paws.util.PetName;
import com.hhh.paws.util.UiState;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DehelmintizationFragment extends Fragment {

    private FragmentDehelmintizationBinding _binding = null;
    private FragmentDehelmintizationBinding getBinding(){
        return _binding;
    }

    private RecyclerView recyclerDehelmintization;
    private FloatingActionButton addDehelmintizationButton;
    private TextView notElemDehelmintization, addTextView;
    private ImageView addArrow;
    private ProgressBar progressBarDehelmintization;

    private DehelmintizationViewModel viewModelDehelmintization;
    private DehelmintizationAdapter adapter;
    private String petNameThis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentDehelmintizationBinding.inflate(inflater, container, false);
        viewModelDehelmintization = new ViewModelProvider(
                DehelmintizationFragment.this).get(DehelmintizationViewModel.class
        );
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petNameThis = PetName.INSTANCE.getName();

        recyclerDehelmintization = getBinding().recyclerDehelmintization;
        initAdapter();
        adapter.setClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(Object object) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("dehelmintization", (Parcelable) object);
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_dehelmintization_to_detailDehelmintizationFragment, bundle);
            }

            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onItemLongClickListener(Object object, CardView cardView) {
                showPopUp((Dehelmintization) object, cardView);
            }
        });

        addDehelmintizationButton = getBinding().addDehelmintizationButton;
        addDehelmintizationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_vet_passport)
                        .navigate(R.id.action_nav_dehelmintization_to_detailDehelmintizationFragment);
            }
        });

        notElemDehelmintization = getBinding().notElemDehelmintization;
        addTextView = getBinding().addTextView;
        addArrow = getBinding().addArrow;
        progressBarDehelmintization = getBinding().progressBarDehelmintization;

        viewModelDehelmintization.getAllDehelmintization(petNameThis);
        viewModelDehelmintization.getDehelmintizationList().observe(getViewLifecycleOwner(),
                new Observer<UiState<List<Dehelmintization>>>() {
            @Override
            public void onChanged(UiState<List<Dehelmintization>> listUiState) {
                if (listUiState == UiState.Loading.INSTANCE) {
                    progressBarDehelmintization.setVisibility(View.VISIBLE);
                } else if (listUiState.getClass() == UiState.Success.class) {
                    progressBarDehelmintization.setVisibility(View.INVISIBLE);
                    adapter.setDiffer(((UiState.Success<List<Dehelmintization>>) listUiState).getData());
                    if (adapter.getItemCount() == 0) {
                        notElemDehelmintization.setVisibility(View.VISIBLE);
                        addArrow.setVisibility(View.VISIBLE);
                        addTextView.setVisibility(View.VISIBLE);
                    } else {
                        notElemDehelmintization.setVisibility(View.INVISIBLE);
                        addArrow.setVisibility(View.INVISIBLE);
                        addTextView.setVisibility(View.INVISIBLE);
                    }
                } else if (listUiState.getClass() == UiState.Failure.class) {
                    progressBarDehelmintization.setVisibility(View.INVISIBLE);
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModelDehelmintization.getDelete().observe(getViewLifecycleOwner(),
                new Observer<UiState<String>>() {
                    @Override
                    public void onChanged(UiState<String> listUiState) {
                        if (listUiState == UiState.Loading.INSTANCE) {
                            progressBarDehelmintization.setVisibility(View.VISIBLE);
                        } else if (listUiState.getClass() == UiState.Success.class) {
                            viewModelDehelmintization.getAllDehelmintization(petNameThis);
                        } else if (listUiState.getClass() == UiState.Failure.class) {
                            progressBarDehelmintization.setVisibility(View.INVISIBLE);
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initAdapter() {
        adapter = new DehelmintizationAdapter();
        recyclerDehelmintization.setAdapter(adapter);
        recyclerDehelmintization.setLayoutManager(new StaggeredGridLayoutManager(
                1, LinearLayoutManager.VERTICAL)
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void showPopUp(Dehelmintization currentDehelmintization, CardView cardView) {
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
                            viewModelDehelmintization.deleteDehelmintization(currentDehelmintization.getId(), petNameThis);
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