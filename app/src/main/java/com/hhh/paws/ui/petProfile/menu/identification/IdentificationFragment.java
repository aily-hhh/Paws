package com.hhh.paws.ui.petProfile.menu.identification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hhh.paws.R;
import com.hhh.paws.databinding.FragmentIdentificationBinding;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class IdentificationFragment extends Fragment {

    private FragmentIdentificationBinding _binding = null;
    private FragmentIdentificationBinding getBinding() {
        return _binding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentIdentificationBinding.inflate(inflater, container, false);
        return getBinding().getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}