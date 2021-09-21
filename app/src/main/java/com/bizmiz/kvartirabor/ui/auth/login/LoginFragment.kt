package com.bizmiz.kvartirabor.ui.auth.login

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentLoginBinding
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentLoginBinding.bind(view)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),R.color.statusBarColor)
        binding.apply {
            if (etTelNumber.text.isEmpty()){
                binding.btnNext.setBackgroundResource(R.drawable.black_shape)
                binding.btnNext.isEnabled = false
            }
            binding.btnNext.setBackgroundResource(R.drawable.black_shape)
            binding.btnNext.isEnabled = false
            etTelNumber.doOnTextChanged { text, _, _, _ ->
                when{
                    text!!.length<9 || text.length>9->{
                        binding.btnNext.setBackgroundResource(R.drawable.black_shape)
                        binding.btnNext.isEnabled = false
                    }
                    text.length==9->{
                        binding.btnNext.setBackgroundResource(R.drawable.sms_shape)
                        binding.btnNext.isEnabled = true
                    }
                }
            }
            btnNext.setOnClickListener {
                if (isNetworkAvialable()){
                    countryCode.registerCarrierNumberEditText(etTelNumber)
                    val number = countryCode.fullNumberWithPlus
                    val bundle = bundleOf(
                        "number" to number
                    )
                    val navController: NavController =
                        Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
                    navController.navigate(R.id.action_loginFragment_to_SMSFragment, bundle)
                }else{
                    Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
        }
    fun isNetworkAvialable():Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo
        return internetInfo != null && internetInfo.isConnected
    }
}