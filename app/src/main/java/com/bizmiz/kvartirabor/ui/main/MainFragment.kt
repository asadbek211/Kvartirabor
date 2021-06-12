package com.bizmiz.kvartirabor.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.bizmiz.kvartirabor.R
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navControl: NavController = Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
        NavigationUI.setupWithNavController(bottom_navigation, navControl)
    }
}