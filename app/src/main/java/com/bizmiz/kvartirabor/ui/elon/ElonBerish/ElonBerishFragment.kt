package com.bizmiz.kvartirabor.ui.elon.ElonBerish

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.*
import com.bizmiz.kvartirabor.data.Adapters.ImageAdapter
import com.bizmiz.kvartirabor.databinding.FragmentElonBerishBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ElonBerishFragment : Fragment(R.layout.fragment_elon_berish), View.OnClickListener {
    private val elonBerishViewModel: ElonBerishViewModel by viewModel()
    private lateinit var adapter: ImageAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var qoshimcha = true
    private val mAuth = FirebaseAuth.getInstance()
    private val pulBirlik: Array<String> = arrayOf("so'm", "$")
    private val xonaSoni: Array<String> = arrayOf("1", "2", "3", "4", "5", "5+")
    private val ijarachiSoni: Array<String> = arrayOf("0", "1", "2", "3", "4", "5", "5+")
    lateinit var binding: FragmentElonBerishBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentElonBerishBinding.bind(view)
        adapter = ImageAdapter()
        binding.apply {
            imageRecView.adapter = adapter
            exit.setOnClickListener {
                val navController: NavController =
                    Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
                navController.popBackStack()
            }
            imgClear.setOnClickListener {
                ImageLinear.visibility = View.GONE
                ImageAdd.visibility = View.VISIBLE
                adapter.models.clear()
                adapter.notifyDataSetChanged()

            }
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            val prefs: SharedPreferences =
                requireActivity().getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE)
            if (mAuth.currentUser != null) {
                etTel.setText(mAuth.currentUser?.phoneNumber)
            }

            qoshimchaMalumot.setOnClickListener {

                if (qoshimcha) {
                    Toast.makeText(
                        requireContext(),
                        "Kechirasiz bu bo'lim hali tayyor emas",
                        Toast.LENGTH_SHORT
                    ).show()
                    AsosiyConsLayout.visibility = View.VISIBLE
                    qoshimcha = false
                } else {
                    AsosiyConsLayout.visibility = View.GONE
                    qoshimcha = true
                }
            }
            rdbSotiladi.setOnClickListener {
                radioButton(rdbIjaraBerish)

            }
            rdbIjaraBerish.setOnClickListener {
                radioButton(rdbSotiladi)

            }
            rdbKopQavat.setOnClickListener {
                radioButton(rdbYerJoy)
            }
            rdbYerJoy.setOnClickListener {
                radioButton(rdbKopQavat)
            }
            rdbYakkaTolash.setOnClickListener {
                radioButton(rdbUmumiyTolash)
            }
            rdbUmumiyTolash.setOnClickListener {
                radioButton(rdbYakkaTolash)
            }

            rdbDoimiy.setOnClickListener {
                threeCheckRadioButton(rdbDoimiy, rdbKelishimli, rdbKunlik)
            }
            rdbKelishimli.setOnClickListener {
                threeCheckRadioButton(rdbKelishimli, rdbDoimiy, rdbKunlik)
            }
            rdbKunlik.setOnClickListener {
                threeCheckRadioButton(rdbKunlik, rdbDoimiy, rdbKelishimli)
            }
            rdbNarxIchida.setOnClickListener {
                radioButton1(rdbNarxTashqari, rdbKelishiladi)
            }
            rdbNarxTashqari.setOnClickListener {
                radioButton1(rdbNarxIchida, rdbKelishiladi)
            }
            rdbKelishiladi.setOnClickListener {
                radioButton1(rdbNarxTashqari, rdbNarxIchida)
            }
            pulBirligi.adapter = adapter(pulBirlik)
            xonalarSoni.adapter = adapter(xonaSoni)
            yangiIjarachilarSoni.adapter = adapter(xonaSoni)
            ijaradagilarSoni.adapter = adapter(ijarachiSoni)
            uyQavatliligi.adapter = adapter(xonaSoni)
            sizniQavat.adapter = adapter(xonaSoni)
            btnMap.setOnClickListener {
                getLastLocation()

            }
            ImageAdd.setOnClickListener {
                adapter.models.clear()
                picImageIntent()
            }
            rasmlar.setOnClickListener {
                adapter.models.clear()
                picImageIntent()
            }
            elonJoylash.setOnClickListener {
                if (isNetworkAvialable()){
                    elonBerishViewModel.setElonData(
                        adapter,
                        etSarlavha,
                        etManzil,
                        etTel,
                        etNarx,
                        pulBirligi.selectedItem.toString(),
                        prefs
                    )
                }else{
                    Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT).show()
                }


            }
            elonBerishViewModel.elonList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.SUCCESS -> {
                        if (it.data == "success")
                            loading.visibility = View.VISIBLE
                    }
                    ResourceState.ERROR -> {
                        Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                        loading.visibility = View.GONE
                    }
                    ResourceState.CHECK -> {
                        if (it.check == "check") {
                            val message = AlertDialog.Builder(requireActivity())
                            message.setTitle("Kvartirabor")
                                .setMessage("E'loningiz saqlandi\nE'lonlarim bo'limida e'loningizni tahrirlashingiz yoki o'chirishingiz mumkin.")
                                .setCancelable(false)
                                .setPositiveButton("OK") { message, _ ->
                                    message.dismiss()
                                }
                                .create().show()

                            val navController: NavController = Navigation.findNavController(
                                requireActivity(),
                                R.id.mainFragmentContener
                            )
                            navController.popBackStack()
                        } else if (it.check == "validate") {
                            loading.visibility = View.VISIBLE
                        }
                    }
                }
            })
            setOnClick()
        }

    }

    private fun setOnClick() {
        binding.apply {
            rdbAbiturient.setOnClickListener(this@ElonBerishFragment)
            rdbAriston.setOnClickListener(this@ElonBerishFragment)
            rdbBalkon.setOnClickListener(this@ElonBerishFragment)
            rdbDoimiy.setOnClickListener(this@ElonBerishFragment)
            rdbGilam.setOnClickListener(this@ElonBerishFragment)
            rdbIjaraBerish.setOnClickListener(this@ElonBerishFragment)
            rdbIshchi.setOnClickListener(this@ElonBerishFragment)
            rdbKelishiladi.setOnClickListener(this@ElonBerishFragment)
            rdbKelishimli.setOnClickListener(this@ElonBerishFragment)
            rdbKirMashina.setOnClickListener(this@ElonBerishFragment)
            rdbKopQavat.setOnClickListener(this@ElonBerishFragment)
            rdbKunlik.setOnClickListener(this@ElonBerishFragment)
            rdbMuzlatgich.setOnClickListener(this@ElonBerishFragment)
            rdbNarxIchida.setOnClickListener(this@ElonBerishFragment)
            rdbNarxTashqari.setOnClickListener(this@ElonBerishFragment)
            rdbOgilBola.setOnClickListener(this@ElonBerishFragment)
            rdbOilaviy.setOnClickListener(this@ElonBerishFragment)
            rdbOshxona.setOnClickListener(this@ElonBerishFragment)
            rdbQizBola.setOnClickListener(this@ElonBerishFragment)
            rdbQoshimchaXona.setOnClickListener(this@ElonBerishFragment)
            rdbSotiladi.setOnClickListener(this@ElonBerishFragment)
            rdbStudent.setOnClickListener(this@ElonBerishFragment)
            rdbTelevizor.setOnClickListener(this@ElonBerishFragment)
            rdbUmumiyTolash.setOnClickListener(this@ElonBerishFragment)
            rdbWiFi.setOnClickListener(this@ElonBerishFragment)
            rdbYakkaTolash.setOnClickListener(this@ElonBerishFragment)
            rdbYerJoy.setOnClickListener(this@ElonBerishFragment)
        }

    }


    private fun adapter(ItemList: Array<String>): SpinnerAdapter {
        val adapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, ItemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun radioButton(radioButtonChecked: RadioButton) {
        radioButtonChecked.isChecked = false
    }

    private fun radioButton1(radioButton2: RadioButton, radioButton3: RadioButton) {
        radioButton2.isChecked = false
        radioButton3.isChecked = false
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isGPSEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        newLocationData()
                    } else {
                        val navController: NavController = Navigation.findNavController(
                            requireActivity(),
                            R.id.mainFragmentContener
                        )
                        navController.navigate(R.id.action_elonBerishFragment_to_mapFragment)
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }

    private fun newLocationData() {
        locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            interval = 0
            fastestInterval = 0
            numUpdates = 1
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val lat = locationResult.lastLocation.latitude
                val long = locationResult.lastLocation.longitude
                Toast.makeText(
                    requireContext(),
                    "Latitute: $lat\nLongitute: $long",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
        if (checkPermission()) fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()!!
        )
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) ==
                PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ), 1010
        )
    }

    private fun Fragment.isGPSEnable(): Boolean =
        requireContext().getLocationManager().isProviderEnabled(LocationManager.GPS_PROVIDER)

    private fun Context.getLocationManager() =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager


    private fun picImageIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select image(s)"), 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data!!.clipData != null) {
                    val image: ArrayList<Uri?> = arrayListOf()
                    val cout = data.clipData!!.itemCount
                    for (i in 0 until cout) {
                        val imageUrl = data.clipData!!.getItemAt(i).uri
                        image.add(imageUrl)
                    }
                    adapter.models.clear()
                    adapter.notifyDataSetChanged()
                    adapter.models = image
                    binding.ImageAdd.visibility = View.GONE
                    binding.ImageLinear.visibility = View.VISIBLE
                } else {
                    val image: ArrayList<Uri?> = arrayListOf()
                    val imageUrl = data.data
                    binding.ImageAdd.visibility = View.GONE
                    binding.ImageLinear.visibility = View.VISIBLE
                    image.add(imageUrl)
                    adapter.models.clear()
                    adapter.notifyDataSetChanged()
                    adapter.models = image

                }
            }
        }
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v!!.id) {
                R.id.rdbAbiturient -> {
                    radioButtonOnClick(rdbAbiturient)
                }
                R.id.rdbAriston -> {
                }
                R.id.rdbBalkon -> {
                }
                R.id.rdbDoimiy -> {
                }
                R.id.rdbGilam -> {
                }
                R.id.rdbIjaraBerish -> {
                    twoCheckRadioButton(rdbIjaraBerish, rdbSotiladi)
                    lay2.visibility = View.VISIBLE
                    lay3.visibility = View.VISIBLE
                    lay5.visibility = View.VISIBLE
                    lay7.visibility = View.VISIBLE
                    lay9.visibility = View.VISIBLE
                    consa.visibility = View.VISIBLE
                    consa2.visibility = View.VISIBLE
                    textInputLayout6.visibility = View.GONE
                }
                R.id.rdbIshchi -> {
                }
                R.id.rdbKelishiladi -> {
                }
                R.id.rdbKelishimli -> {
                }
                R.id.rdbKirMashina -> {
                }
                R.id.rdbKopQavat -> {
                }
                R.id.rdbKunlik -> {
                }
                R.id.rdbMuzlatgich -> {
                }
                R.id.rdbNarxIchida -> {
                }
                R.id.rdbNarxTashqari -> {
                }
                R.id.rdbOgilBola -> {
                }
                R.id.rdbOilaviy -> {
                }
                R.id.rdbOshxona -> {
                }
                R.id.rdbQizBola -> {
                }
                R.id.rdbQoshimchaXona -> {
                }
                R.id.rdbSotiladi -> {
                    twoCheckRadioButton(rdbSotiladi, rdbIjaraBerish)
                    lay2.visibility = View.GONE
                    lay3.visibility = View.GONE
                    lay5.visibility = View.GONE
                    lay7.visibility = View.GONE
                    lay9.visibility = View.GONE
                    consa.visibility = View.GONE
                    consa2.visibility = View.GONE
                    textInputLayout6.visibility = View.VISIBLE
                }
                R.id.rdbStudent -> {
                }
                R.id.rdbTelevizor -> {
                }
                R.id.rdbUmumiyTolash -> {
                }
                R.id.rdbWiFi -> {
                }
                R.id.rdbYakkaTolash -> {
                }
                R.id.rdbYerJoy -> {
                }
            }
        }
    }


        fun isNetworkAvialable():Boolean {
            val conManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val internetInfo = conManager.activeNetworkInfo

            return internetInfo != null && internetInfo.isConnected
        }
    }
