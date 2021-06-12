package com.bizmiz.kvartirabor.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_auth_user.view.*

class AuthUserFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_auth_user, container, false)


    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController: NavController = Navigation.findNavController(requireActivity(),R.id.mainFragmentContener)
        view.title1.text = "Salom, ${mAuth.currentUser?.phoneNumber}"
        view.raqam.setText(mAuth.currentUser?.phoneNumber.toString())
        view.signOut.setOnClickListener {
            mAuth.signOut()
            navController.navigate(R.id.authRegFragment)
        }

    }
}