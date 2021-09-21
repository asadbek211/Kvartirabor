package com.bizmiz.kvartirabor.ui.elon.Elonlar.barchaElonlar

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Adapters.ElonlarAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.databinding.FragmentBarchaElonlarBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarchaElonlarFragment : Fragment(R.layout.fragment_barcha_elonlar) {
    private lateinit var binding: FragmentBarchaElonlarBinding
    private val viewModel: BarchaElonlarViewModel by viewModel()
    lateinit var adapter: ElonlarAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getBarchaElonlarData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBarchaElonlarBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(
                requireActivity(),
                R.color.splashColor
            )
        } else {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.marshmallow)
        }
        adapter = ElonlarAdapter()
        binding.recView.adapter = adapter
        setObservers()
        binding.swipeContainer.setOnRefreshListener {
            if (isNetworkAvialable()) {
                viewModel.getBarchaElonlarData()
            } else {
                binding.swipeContainer.isRefreshing = false
                Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        binding.imgBack.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(
                    requireActivity(),
                    R.id.mainFragmentContener
                )
            navController.popBackStack()
        }
        binding.txtSearch.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(
                    requireActivity(),
                    R.id.mainFragmentContener
                )
            navController.navigate(R.id.action_barchaElonlarFragment_to_searchFragment)
        }
    }
    private fun setObservers() {
        viewModel.barchaElon.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).visibility(false)
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.swipeContainer.isRefreshing = false
                }
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!
                    binding.recView.adapter = adapter
                    binding.loading.visibility = View.GONE
                    binding.txtSearch.setBackgroundResource(R.color.white)
                    binding.imgBack.setBackgroundResource(R.color.white)
                    binding.swipeContainer.isRefreshing = false
                    adapter.setOnClickListener { data ->
                        val bundle = bundleOf(
                            "id" to data.id,
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
                            "longitude" to data.longitude,
                            "viloyat" to data.viloyat
                        )
                        val navController: NavController =
                            Navigation.findNavController(
                                requireActivity(),
                                R.id.mainFragmentContener
                            )
                        navController.navigate(
                            R.id.action_barchaElonlarFragment_to_elonFullFragment,
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