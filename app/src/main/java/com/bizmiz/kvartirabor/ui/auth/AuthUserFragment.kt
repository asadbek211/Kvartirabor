package com.bizmiz.kvartirabor.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_auth_user.*
import kotlinx.android.synthetic.main.fragment_auth_user.view.*

class AuthUserFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).visibility(false)
        mAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_auth_user, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.title1.text = "Salom, ${mAuth.currentUser?.phoneNumber}"
        view.raqam.setText(mAuth.currentUser?.phoneNumber.toString())
        view.signOut.setOnClickListener {
            mAuth.signOut()
            val authUserFragment = AuthUserFragment()
            val authRegFragment = AuthRegFragment()
            requireActivity().supportFragmentManager.beginTransaction().remove(authUserFragment).commit()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.profil_container,authRegFragment).commit()
        }
        view.kirish_ochish.setOnClickListener {
            kirish_ochish.isEnabled = false
            loading.visibility = View.VISIBLE
            val navController: NavController = Navigation.findNavController(requireActivity(),R.id.mainFragmentContener)
            navController.navigate(R.id.elonBerishFragment)
            (activity as MainActivity).visibility(true)
        }
    }
}