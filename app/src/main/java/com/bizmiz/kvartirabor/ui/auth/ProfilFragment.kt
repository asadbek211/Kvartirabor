package com.bizmiz.kvartirabor.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bizmiz.kvartirabor.R
import com.google.firebase.auth.FirebaseAuth

class ProfilFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navControl: NavController = Navigation.findNavController(requireActivity(), R.id.profil_contener)
            val mAuth = FirebaseAuth.getInstance()
            if (mAuth.currentUser!=null){
                    navControl.navigate(R.id.authUserFragment)
                }else{
                    navControl.navigate(R.id.authRegFragment)
                }

    }
}