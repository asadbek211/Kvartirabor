package com.bizmiz.kvartirabor.ui.auth.UserSignIn

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentAuthUserBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.ui.auth.UserReg.AuthRegFragment
import com.google.firebase.auth.FirebaseAuth

class AuthUserFragment : Fragment(R.layout.fragment_auth_user) {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentAuthUserBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        mAuth = FirebaseAuth.getInstance()
        binding = FragmentAuthUserBinding.bind(view)
        binding.title1.text = "Salom, ${mAuth.currentUser?.phoneNumber}"
        binding.raqam.text = mAuth.currentUser?.phoneNumber.toString()
        binding.signOut.setOnClickListener {
            val message = AlertDialog.Builder(requireActivity())
            message.setTitle("Kvartirabor")
                .setMessage("Tizimdan chiqishni istaysizmi?")
                .setCancelable(false)
                .setPositiveButton("Ha") { message, _ ->
                    mAuth.signOut()
                    val authUserFragment = AuthUserFragment()
                    val authRegFragment = AuthRegFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .remove(authUserFragment)
                        .commit()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.profil_container, authRegFragment).commit()
                }.setNegativeButton("Yo'q") { message, _ ->
                    message.dismiss()
                }.create().show()
        }
        binding.etElonlarim.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navController.navigate(R.id.action_profilFragment_to_meningElonlarimFragment)
        }
        binding.kirishOchish.setOnClickListener {
            binding.kirishOchish.isEnabled = false
            binding.signOut.isEnabled = false
            binding.loading.visibility = View.VISIBLE
            val navController: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navController.navigate(R.id.action_profilFragment_to_elonBerishFragment)
            (activity as MainActivity).visibility(true)
        }
    }
}