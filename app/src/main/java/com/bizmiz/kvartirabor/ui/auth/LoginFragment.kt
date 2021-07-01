package com.bizmiz.kvartirabor.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.next.setOnClickListener {
            countryCode.registerCarrierNumberEditText(tel_nomer)
            val number = countryCode.fullNumberWithPlus
            Toast.makeText(requireContext(), number.toString(), Toast.LENGTH_SHORT).show()
            val bundle = bundleOf(
                "number" to number )
            val navController: NavController = Navigation.findNavController(requireActivity(),R.id.mainFragmentContener)
            navController.navigate(R.id.action_loginFragment_to_SMSFragment,bundle)

        }
    }
}