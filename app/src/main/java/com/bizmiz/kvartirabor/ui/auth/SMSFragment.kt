package com.bizmiz.kvartirabor.ui.auth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentSMSBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class SMSFragment : Fragment(R.layout.fragment_s_m_s) {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var phonenumber: String
    var otpid: String? = null
    lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSMSBinding

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentSMSBinding.bind(view)
        phonenumber = requireArguments().getString("number").toString()
        mAuth = FirebaseAuth.getInstance()
        binding.txtInfo.text = phonenumber + " " + resources.getString(R.string.nomer)
        initiateotp()
        binding.btnConfirm.setOnClickListener {
            if (binding.etSmsCode.text.toString().isEmpty())
                Toast.makeText(
                    requireContext(), "Sms xabardagi kodni kiriting", Toast.LENGTH_LONG
                ).show()
            else if (binding.etSmsCode.text.toString().length != 6) Toast.makeText(
                requireContext(),
                "Kod xato",
                Toast.LENGTH_LONG
            ).show() else {
                val credential =
                    PhoneAuthProvider.getCredential(otpid!!, binding.etSmsCode.text.toString())
                signInWithPhoneAuthCredential(credential)
            }
        }
    }

    private fun initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenumber,  // Phone number to verify
            60,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            requireActivity(),  // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(
                    s: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    otpid = s
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                }
            }) // OnVerificationStateChangedCallbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    db.collection("users").document(mAuth.currentUser?.uid!!).get()
                        .addOnCompleteListener {
                            if (it.isSuccessful && !it.result?.exists()!!) {
                                val map: MutableMap<String, Any?> = mutableMapOf()
                                map["Telefon raqam"] =
                                    requireArguments().getString("number").toString()
                                db.collection("users").document(mAuth.currentUser?.uid!!.toString())
                                    .set(map)

                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            requireContext(),
                                            e.localizedMessage,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }
                    requireActivity().finish()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Kodda xatolik bor",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}