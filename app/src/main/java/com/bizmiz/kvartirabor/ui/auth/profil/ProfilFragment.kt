package com.bizmiz.kvartirabor.ui.auth.profil

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentProfilBinding
import com.bizmiz.kvartirabor.ui.auth.UserReg.AuthRegFragment
import com.bizmiz.kvartirabor.ui.auth.UserSignIn.AuthUserFragment
import com.google.firebase.auth.FirebaseAuth

class ProfilFragment : Fragment(R.layout.fragment_profil) {
    private lateinit var binding: FragmentProfilBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        binding = FragmentProfilBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),
                R.color.splashColor
            )
        }else{
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.marshmallow)
        }
        val mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            val authUserFragment = AuthUserFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.profil_container, authUserFragment).commit()
        } else {
            val authRegFragment = AuthRegFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.profil_container, authRegFragment).commit()
        }
    }
}