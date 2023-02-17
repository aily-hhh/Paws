package com.hhh.paws.ui.auth;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hhh.paws.R;
import com.hhh.paws.databinding.FragmentAuthBinding;
import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthFragment extends Fragment {

    private FirebaseAuth mFirebaseAuth;
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    AuthUI.IdpConfig provider = new AuthUI.IdpConfig.GoogleBuilder().build();

    private FragmentAuthBinding _binding = null;
    private FragmentAuthBinding mBinding() {
        return _binding;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        _binding = FragmentAuthBinding.inflate(getLayoutInflater(), container, false);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Navigation.findNavController(getActivity(), R.id.navHostFragment)
                .navigate(R.id.action_authFragment_to_mainFragment);
            } else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setIsSmartLockEnabled(false)
                                .setAvailableProviders(Collections.singletonList(provider))
                                .setLogo(R.mipmap.logo_paws).setTheme(R.style.Theme_Paws)
                                .build(),
                        RC_SIGN_IN
                );
            }
        };

        return mBinding().getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}