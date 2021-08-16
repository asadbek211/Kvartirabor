package com.bizmiz.kvartirabor.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentSaralanganlarBinding

class SaralanganlarFragment : Fragment(R.layout.fragment_saralanganlar) {
    lateinit var binding: FragmentSaralanganlarBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        binding = FragmentSaralanganlarBinding.bind(view)
    }
}