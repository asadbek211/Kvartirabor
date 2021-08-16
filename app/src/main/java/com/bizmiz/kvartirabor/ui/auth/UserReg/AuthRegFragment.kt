package com.bizmiz.kvartirabor.ui.auth.UserReg

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentAuthRegBinding

class AuthRegFragment : Fragment(R.layout.fragment_auth_reg) {
    private lateinit var binding: FragmentAuthRegBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        binding = FragmentAuthRegBinding.bind(view)
        binding.kirishOchish.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            val navControl: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navControl.navigate(R.id.action_profilFragment_to_loginFragment)
            (activity as MainActivity).visibility(true)
        }
    }
}