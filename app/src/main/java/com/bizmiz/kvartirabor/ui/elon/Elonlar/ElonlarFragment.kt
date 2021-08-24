package com.bizmiz.kvartirabor.ui.elon.Elonlar

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Adapters.BolimAdapter
import com.bizmiz.kvartirabor.data.Adapters.ElonlarAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.data.model.BolimModel
import com.bizmiz.kvartirabor.databinding.FragmentElonlarBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class ElonlarFragment : Fragment(R.layout.fragment_elonlar) {
    private val viewModel: ElonlarViewModel by viewModel()
    lateinit var adapter: ElonlarAdapter
    lateinit var bolimAdapter: BolimAdapter
    lateinit var binding: FragmentElonlarBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentElonlarBinding.bind(view)
        setObservers()
        viewModel.getElonlarData()
        bolimAdapter = BolimAdapter()
        binding.rvBolim.adapter = bolimAdapter
        setData()
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
    private fun setData(){
        val listBolim:ArrayList<String> = arrayListOf("Barcha ruknlar","Sotish","Ijaraga berish")
        val list:ArrayList<BolimModel> = arrayListOf()
        for (i in 0 until listBolim.size){
            list.add(BolimModel(listBolim[i]))
        }
        bolimAdapter.models = list

    }

    private fun setObservers() {
        viewModel.elon.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.LOADING -> {
                    (activity as MainActivity).visibility(false)
                    binding.swipeContainer.isRefreshing = false
                }
                ResourceState.SUCCESS -> {
                    adapter = ElonlarAdapter(it.data!!, true)
                    binding.recView.adapter = adapter
                    binding.loading.visibility = View.GONE
                    binding.etSearch.setBackgroundResource(R.color.colorPrimaryDark)
                    binding.swipeContainer.isRefreshing = false
                    adapter.setOnClickListener { _, data->
                        val bundle = bundleOf(
                            "image" to data.imageUrlList,
                            "sarlavha" to data.sarlavha,
                            "manzil" to data.manzil,
                            "telefon_raqam" to data.telefon_raqam,
                            "createdDate" to data.createdDate,
                            "narxi" to data.narxi,
                            "type" to data.type
                        )
                        val navController: NavController =
                            Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
                        navController.navigate(R.id.action_elonlarFragment_to_elonFullFragment,bundle)
                    }
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}