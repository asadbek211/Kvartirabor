package com.bizmiz.kvartirabor.ui.elon.Elonlar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Adapters.ElonlarAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.databinding.FragmentElonlarBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ElonlarFragment : Fragment(R.layout.fragment_elonlar) {
    private val viewModel: ElonlarViewModel by viewModel()
    lateinit var adapter: ElonlarAdapter
    lateinit var binding: FragmentElonlarBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentElonlarBinding.bind(view)
        setObservers()
        viewModel.getElonlarData()
        binding.etSearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.etSearch.clearFocus();
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        binding.swipeContainer.setOnRefreshListener {
            setObservers()
        }

    }

    private fun setObservers() {
        viewModel.elon.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.swipeContainer.isRefreshing = false
                }
                ResourceState.SUCCESS -> {
                    adapter = ElonlarAdapter(it.data!!, true)
                    binding.recView.adapter = adapter
                    binding.loading.visibility = View.GONE
                    binding.swipeContainer.isRefreshing = false
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}