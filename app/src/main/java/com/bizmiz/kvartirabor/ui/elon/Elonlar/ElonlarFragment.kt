package com.bizmiz.kvartirabor.ui.elon.Elonlar

import android.app.AlertDialog
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
import com.bizmiz.kvartirabor.data.Adapters.BolimAdapter
import com.bizmiz.kvartirabor.data.Adapters.ElonlarAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.data.listBolim
import com.bizmiz.kvartirabor.data.model.BolimModel
import com.bizmiz.kvartirabor.databinding.FragmentElonlarBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ElonlarFragment : Fragment(R.layout.fragment_elonlar) {
    private val viewModel: ElonlarViewModel by viewModel()
    lateinit var adapter: ElonlarAdapter
    lateinit var bolimAdapter: BolimAdapter
    lateinit var binding: FragmentElonlarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getElonlarData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentElonlarBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(
                requireActivity(),
                R.color.white
            )
        } else {
            requireActivity().window.statusBarColor =
                ContextCompat.getColor(requireActivity(), R.color.marshmallow)
        }
        bolimAdapter = BolimAdapter()
        adapter = ElonlarAdapter()
        binding.recView.adapter = adapter
        binding.rvBolim.adapter = bolimAdapter
        setData()
        setObservers()
        binding.namunaliElonlarInfo.setOnClickListener {
            val message = AlertDialog.Builder(requireActivity())
            message.setTitle("Kvartirabor")
                .setMessage("Barcha ma'lumotlari to'liq to'ldirilgan e'lonlar shu yerda joylashadi.")
                .setCancelable(false)
                .setPositiveButton("OK") { message, _ ->
                    message.dismiss()
                }
                .create().show()
        }
        bolimAdapter.onClickListener { position ->
            when (position) {
                0 -> {
                    navigate(R.id.action_elonlarFragment_to_barchaElonlarFragment)
                }
                1 -> {
                    navigate(R.id.action_elonlarFragment_to_ijaraElonlarFragment)
                }
                2 -> {
                    navigate(R.id.action_elonlarFragment_to_sotishElonlarFragment)
                }
                3 -> {
                    navigate(R.id.action_elonlarFragment_to_ayrboshlashElonlarFragment)
                }
            }
        }
        binding.swipeContainer.setOnRefreshListener {
            if (isNetworkAvialable()) {
                viewModel.getElonlarData()
            } else {
                binding.swipeContainer.isRefreshing = false
                Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setData() {
        val list: ArrayList<BolimModel> = arrayListOf()
        for (i in 0 until listBolim.size) {
            list.add(BolimModel(listBolim[i]))
        }
        bolimAdapter.models = list
    }

    private fun setObservers() {
        viewModel.elon.observe(viewLifecycleOwner, Observer {
            (activity as MainActivity).visibility(false)
            when (it.status) {
                ResourceState.LOADING -> {
                    binding.swipeContainer.isRefreshing = false
                }
                ResourceState.SUCCESS -> {
                    adapter.models = it.data!!
                    binding.loading.visibility = View.GONE
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

    fun navigate(destination: Int) {
        val navController: NavController =
            Navigation.findNavController(
                requireActivity(),
                R.id.mainFragmentContener
            )
        navController.navigate(
            destination
        )

    }
}