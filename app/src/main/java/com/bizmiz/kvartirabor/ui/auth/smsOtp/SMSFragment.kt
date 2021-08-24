package com.bizmiz.kvartirabor.ui.auth.smsOtp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.databinding.FragmentSMSBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class SMSFragment : Fragment(R.layout.fragment_s_m_s) {
    private lateinit var phonenumber: String
    var otpid: String? = null
    var a = false
    lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentSMSBinding
    private val smsViewModel: SmsViewModel by viewModel()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentSMSBinding.bind(view)
        phonenumber = requireArguments().getString("number").toString()
        mAuth = FirebaseAuth.getInstance()
        binding.txtInfo.text = phonenumber + " " + resources.getString(R.string.nomer)
        initiateotp()
        binding.imgReset.setOnClickListener {
            initiateotp()
            binding.apply {
                loading.visibility = View.VISIBLE
                imgReset.visibility = View.GONE
                txtTimer.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.btnConfirm.setOnClickListener {
            when {
                binding.etSmsCode.text.toString().isEmpty() -> Snackbar.make(
                    view,
                    "sms kod kiritilmagan",
                    Snackbar.LENGTH_SHORT
                ).show()
                binding.etSmsCode.text.toString().length != 6 -> Snackbar.make(
                    view,
                    "6 raqamli kod kiriting",
                    Snackbar.LENGTH_SHORT
                ).show()
                else -> {
                    a = true
                    val credential =
                        PhoneAuthProvider.getCredential(otpid!!,binding.etSmsCode.text.toString())
                    signInWithPhoneAuthCredential(credential)
                }
            }

        }

    }

    private fun initiateotp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonenumber,  // Phone number to verify
            30,  // Timeout duration
            TimeUnit.SECONDS,  // Unit of timeout
            requireActivity(),  // Activity (for callback binding)
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(
                    s: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    otpid = s
                    binding.loading.visibility = View.GONE
                    timer()
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                    a = true
                    binding.apply {
                        loading.visibility = View.GONE
                        btnConfirm.isEnabled = false
                        btnConfirm.setBackgroundResource(R.drawable.black_shape)
                    }

                }
            }) // OnVerificationStateChangedCallbacks
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    smsViewModel.smsCredential(phonenumber)
                    smsViewModel.sms.observe(viewLifecycleOwner, Observer {
                        if (it.status == ResourceState.ERROR) {
                            Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                      a = true
                    requireActivity().finish()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                } else {
                    Snackbar.make(
                        binding.root,
                        "sms kod xato",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun timer() {
        val time = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (a) {
                    cancel()
                }
                val simpleDateFormat = SimpleDateFormat("mm:ss")
                val dateString = simpleDateFormat.format(millisUntilFinished)
                binding.txtTimer.text = String.format("%s", dateString)
            }
            override fun onFinish() {
                binding.imgReset.visibility = View.VISIBLE
                binding.txtTimer.setTextColor(resources.getColor(android.R.color.holo_red_light))
            }
        }
         time.start()
    }
}