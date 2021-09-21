package com.bizmiz.kvartirabor.ui.elon.MeniElonlarim

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
import com.bizmiz.kvartirabor.data.Adapters.MeniElonlarimAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.data.model.ElonlarimData
import com.bizmiz.kvartirabor.databinding.FragmentMeningElonlarimBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MeningElonlarimFragment : Fragment(R.layout.fragment_mening_elonlarim) {
    private val elonlarimViewModel: MeningElonlarimViewModel by viewModel()
    lateinit var adapter: MeniElonlarimAdapter
    lateinit var binding: FragmentMeningElonlarimBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        elonlarimViewModel.getElonlarimData()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentMeningElonlarimBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),
                R.color.splashColor
            )
        }else{
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.marshmallow)
        }
        adapter = MeniElonlarimAdapter()
        binding.recView.adapter = adapter
        setObserves()
        if (!isNetworkAvialable())Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT).show()
      adapter.itemDelete { data->
          if (!isNetworkAvialable()){
              Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT).show()
          }else{
              val message = AlertDialog.Builder(requireActivity())
              message.setTitle("Kvartirabor")
                  .setMessage("E'loningizni o'chirmoqchimisiz?")
                  .setCancelable(false)
                  .setPositiveButton("Ha") { message, _ ->
                      elonlarimViewModel.deleteItem(data)
                      binding.loading.visibility = View.VISIBLE
                  }.setNegativeButton("Yo'q"){message,_->
                      message.dismiss()
                  }.create().show()

          }
      }
        adapter.setOnClickListener {data->
            bundle(R.id.action_meningElonlarimFragment_to_elonFullFragment,data)
        }
        adapter.itemUpdate { data->
            bundle(R.id.action_meningElonlarimFragment_to_elonTahrirlashFragment,data)
        }
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
     elonlarimViewModel.deleteItem.observe(viewLifecycleOwner, Observer {
         when(it.status){
             ResourceState.SUCCESS->{
                 elonlarimViewModel.getElonlarimData()
                 binding.loading.visibility = View.GONE
                 Snackbar.make(binding.root,it.data.toString(),Snackbar.LENGTH_SHORT).show()

             }
             ResourceState.ERROR->{
                 Snackbar.make(binding.root,it.message.toString(),Snackbar.LENGTH_SHORT).show()
             }
         }
     })
    }
    fun isNetworkAvialable():Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo

        return internetInfo != null && internetInfo.isConnected
    }
    private fun bundle(destination:Int,data:ElonlarimData){
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
            destination,
            bundle
        )
    }
}
