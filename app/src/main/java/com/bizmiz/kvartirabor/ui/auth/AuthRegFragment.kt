package com.bizmiz.kvartirabor.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_auth_reg.*

class AuthRegFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).visibility(false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth_reg, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kirish_ochish.setOnClickListener {
            val navControl: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navControl.navigate(R.id.action_profilFragment_to_loginFragment)
            (activity as MainActivity).visibility(true)
        }
    }
}