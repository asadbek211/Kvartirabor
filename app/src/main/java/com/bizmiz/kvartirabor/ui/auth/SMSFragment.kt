package com.bizmiz.kvartirabor.ui.auth

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_elon_berish.*
import kotlinx.android.synthetic.main.fragment_s_m_s.*
import kotlinx.android.synthetic.main.fragment_s_m_s.view.*
import java.util.concurrent.TimeUnit

class SMSFragment : Fragment() {
    private  val db = FirebaseFirestore.getInstance()
    private lateinit var phonenumber:String
    var otpid: String? = null
    lateinit var mAuth: FirebaseAuth

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_s_m_s, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phonenumber = requireArguments().getString("number").toString()
        mAuth = FirebaseAuth.getInstance()
        sms_raqam.text = phonenumber+" "+resources.getString(R.string.nomer)
        initiateotp()
        view.tasdiqlash.setOnClickListener {
            if (view.sms_kod.text.toString().isEmpty())
                Toast.makeText(
                    requireContext(), "Sms xabardagi kodni kiriting", Toast.LENGTH_LONG
                ).show()
            else if (view.sms_kod.text.toString().length != 6) Toast.makeText(
                requireContext(),
                "Kod xato",
                Toast.LENGTH_LONG
            ).show() else {
                val credential = PhoneAuthProvider.getCredential(otpid!!,view.sms_kod.text.toString())
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
                override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
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
                            Log.d("users",it.result.toString())
                            if (it.isSuccessful && !it.result?.exists()!!){
                                val map:MutableMap<String,Any?> = mutableMapOf()
                                map["Telefon raqam"] =  requireArguments().getString("number").toString()
                                db.collection("users").document(mAuth.currentUser?.uid!!.toString()).set(map)

                                    .addOnFailureListener {e ->
                                        Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                                    }
                            }
                        }
                    val navController: NavController = Navigation.findNavController(requireActivity(),R.id.mainFragmentContener)
                    navController.navigate(R.id.action_SMSFragment_to_elonlarFragment)
                    (activity as MainActivity).visibility(false)
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