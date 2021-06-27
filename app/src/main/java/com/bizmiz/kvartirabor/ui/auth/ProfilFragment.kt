package com.bizmiz.kvartirabor.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bizmiz.kvartirabor.R
import com.google.firebase.auth.FirebaseAuth

class ProfilFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser != null) {
            val authUserFragment = AuthUserFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.profil_container,authUserFragment).commit()
        } else {
            val authRegFragment = AuthRegFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.profil_container,authRegFragment).commit()
        }
    }
}