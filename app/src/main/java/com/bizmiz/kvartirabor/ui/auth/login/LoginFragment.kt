package com.bizmiz.kvartirabor.ui.auth.login

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentLoginBinding
import androidx.core.content.ContextCompat

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentLoginBinding.bind(view)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),R.color.statusBarColor)
        binding.btnNext.setOnClickListener {
            binding.countryCode.registerCarrierNumberEditText(binding.etTelNumber)
            val number = binding.countryCode.fullNumberWithPlus
            val bundle = bundleOf(
                "number" to number
            )
            val navController: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navController.navigate(R.id.action_loginFragment_to_SMSFragment, bundle)

        }
    }
}