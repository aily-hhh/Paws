package com.hhh.paws.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig.GoogleBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.hhh.paws.R
import com.hhh.paws.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var mFirebaseAuth: FirebaseAuth? = null
    private var mAuthStateListener: AuthStateListener? = null
    var provider = GoogleBuilder().build()
    private var _binding: FragmentAuthBinding? = null
    private fun mBinding(): FragmentAuthBinding? {
        return _binding
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthBinding.inflate(layoutInflater, container, false)
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthStateListener = AuthStateListener { firebaseAuth: FirebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                findNavController(requireActivity(), R.id.navHostFragment)
                    .navigate(R.id.action_authFragment_to_mainFragment)
            } else {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(listOf(provider))
                        .setLogo(R.mipmap.logo_paws).setTheme(R.style.Theme_Paws)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }
        return mBinding()!!.root
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFirebaseAuth!!.addAuthStateListener(mAuthStateListener!!)
    }

    companion object {
        const val RC_SIGN_IN = 1
    }
}