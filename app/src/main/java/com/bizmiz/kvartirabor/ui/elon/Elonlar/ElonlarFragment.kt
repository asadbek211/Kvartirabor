package com.bizmiz.kvartirabor.ui.elon.Elonlar

import android.content.Context
import android.net.ConnectivityManager
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
//                binding.etSearch.clearFocus();
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        binding.swipeContainer.setOnRefreshListener {
            if (isNetworkAvialable()) {
                setObservers()
            } else {
                binding.swipeContainer.isRefreshing = false
                Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }

    private fun setData() {
        val listBolim: ArrayList<String> =
            arrayListOf("Barcha e'lonlar", "Ijaraga berish", "Sotish", "Ayirboshlash")
        val list: ArrayList<BolimModel> = arrayListOf()
        for (i in 0 until listBolim.size) {
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
                    adapter.setOnClickListener { _, data ->
                        val bundle = bundleOf(
                            "sarlavha" to data.sarlavha,
                            "bolim" to data.bolim,
                            "uyQavatliligi" to data.uyQavatliligi,
                            "umumiyMaydon" to data.umumiyMaydon,
                            "oshxonaMaydoni" to data.oshxonaMaydoni,
                            "uyTamiri" to data.uyTamiri,
                            "yashashMaydoni" to data.yashashMaydoni,
                            "narxi" to data.narxi,
                            "yashashQavati" to data.yashashQavati,
                            "xonaSoni" to data.xonaSoni,
                            "tavsif" to data.tavsif,
                            "telRaqam" to data.telRaqam,
                            "type" to data.type,
                            "mebel" to data.mebel,
                            "kelishuv" to data.kelishuv,
                            "sharoitlari" to data.sharoitlari,
                            "qurilishTuri" to data.qurilishTuri,
                            "imageUrlList" to data.imageUrlList,
                            "createdDate" to data.createdDate,
                            "latitude" to data.latitude,
                            "longitude" to data.longitude
                        )
                        val navController: NavController =
                            Navigation.findNavController(
                                requireActivity(),
                                R.id.mainFragmentContener
                            )
                        navController.navigate(
                            R.id.action_elonlarFragment_to_elonFullFragment,
                            bundle
                        )
                    }
                    if (!isNetworkAvialable()) {
                        Toast.makeText(
                            requireActivity(),
                            "Not Internet Connection",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun isNetworkAvialable(): Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo

        return internetInfo != null && internetInfo.isConnected
    }
}