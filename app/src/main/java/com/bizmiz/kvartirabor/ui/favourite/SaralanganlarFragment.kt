package com.bizmiz.kvartirabor.ui.favourite

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
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
import com.bizmiz.kvartirabor.data.Adapters.SaralanganlarAdapter
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.databinding.FragmentSaralanganlarBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.ui.elon.Elonlar.ElonlarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SaralanganlarFragment : Fragment(R.layout.fragment_saralanganlar) {
    lateinit var binding: FragmentSaralanganlarBinding
    private val viewModel: ElonlarViewModel by viewModel()
    private val saralanganlarViewModel: SaralanganlarViewModel by viewModel()
    private lateinit var adapter: SaralanganlarAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        binding = FragmentSaralanganlarBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),
                R.color.splashColor
            )
        }else{
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.marshmallow)
        }
        val prefs: SharedPreferences = requireActivity().getSharedPreferences(Constant.PREFS, MODE_PRIVATE)
        adapter = SaralanganlarAdapter()
        binding.recViewFavourite.adapter = adapter
        saralanganlarViewModel.setSaralanganData(prefs)
        observe()
        adapter.saralanganlarListener { key, id, editor ->
            if (key){
                editor.remove(id).apply()
            }else{
                editor.putBoolean(id,true).apply()
            }
            saralanganlarViewModel.setSaralanganData(prefs)
        }
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
                "viloyat" to data.viloyat,
            )
            val navController: NavController =
                Navigation.findNavController(
                    requireActivity(),
                    R.id.mainFragmentContener
                )
            navController.navigate(
                R.id.action_saralanganlarFragment_to_elonFullFragment,
                bundle
            )
        }
    }
    private fun observe() {
        saralanganlarViewModel.saralanganlar.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    if (it.data!!.isEmpty()) {
                        binding.txtEmpty.visibility = View.VISIBLE
                        binding.recViewFavourite.visibility = View.GONE
                    }
                    binding.loading.visibility = View.GONE
                    adapter.filterList = it.data
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}