package com.bizmiz.kvartirabor.ui.elon.MeniElonlarim

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Adapters.MeniElonlarimAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.databinding.FragmentMeningElonlarimBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MeningElonlarimFragment : Fragment(R.layout.fragment_mening_elonlarim) {
    private val elonlarimViewModel: MeningElonlarimViewModel by viewModel()
    lateinit var adapter: MeniElonlarimAdapter
    lateinit var binding: FragmentMeningElonlarimBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        binding = FragmentMeningElonlarimBinding.bind(view)
        adapter = MeniElonlarimAdapter()
        binding.recView.adapter = adapter
        setObserves()
        elonlarimViewModel.getElonlarimData()
    }

    fun setObserves() {
        elonlarimViewModel.elonlarim.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.CHECK -> {
                    binding.loading.visibility = View.GONE
                    binding.notReg.visibility = View.VISIBLE
                }
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!
                    binding.loading.visibility = View.GONE
                    if (it.data.isNullOrEmpty()) {
                        binding.notReg.visibility = View.VISIBLE
                        binding.notReg.text = "Sizda e'lonlar yo'q"
                    } else {
                        binding.notReg.visibility = View.GONE
                    }

                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}
//adapter.models = list
//binding.loading.visibility = View.GONE
//binding.notReg.visibility = View.VISIBLE
//binding.notReg.text = "Siz e'lon bermagansiz"
//else {
//
//}
