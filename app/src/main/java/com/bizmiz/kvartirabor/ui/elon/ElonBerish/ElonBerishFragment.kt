package com.bizmiz.kvartirabor.ui.elon.ElonBerish

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.*
import com.bizmiz.kvartirabor.data.Adapters.ImageAdapter
import com.bizmiz.kvartirabor.data.Adapters.ViloyatlarAdapter
import com.bizmiz.kvartirabor.databinding.FragmentElonBerishBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ElonBerishFragment : Fragment(R.layout.fragment_elon_berish), View.OnClickListener {
    private lateinit var mapFragment: SupportMapFragment
    private var isCheck: ArrayList<Int> = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private lateinit var mMap: GoogleMap
    private val elonBerishViewModel: ElonBerishViewModel by viewModel()
    private lateinit var adapter: ImageAdapter
    private lateinit var vilAdapter: ViloyatlarAdapter
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val mAuth = FirebaseAuth.getInstance()
    lateinit var binding: FragmentElonBerishBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentElonBerishBinding.bind(view)
        adapter = ImageAdapter()
        vilAdapter = ViloyatlarAdapter()
        binding.apply {
            imageRecView.adapter = adapter
            rdbGishtli.setOnClickListener(this@ElonBerishFragment)
            rdbPanelli.setOnClickListener(this@ElonBerishFragment)
            rdbBlokli.setOnClickListener(this@ElonBerishFragment)
            rdbMonolitli.setOnClickListener(this@ElonBerishFragment)
            rdbYogoch.setOnClickListener(this@ElonBerishFragment)
            rdbGilam.setOnClickListener(this@ElonBerishFragment)
            rdbKirMashina.setOnClickListener(this@ElonBerishFragment)
            rdbOshxona.setOnClickListener(this@ElonBerishFragment)
            rdbBalkon.setOnClickListener(this@ElonBerishFragment)
            rdbKonditsioner.setOnClickListener(this@ElonBerishFragment)
            rdbVanna.setOnClickListener(this@ElonBerishFragment)
            rdbWiFi.setOnClickListener(this@ElonBerishFragment)
            rdbMuzlatgich.setOnClickListener(this@ElonBerishFragment)
            rdbAriston.setOnClickListener(this@ElonBerishFragment)
            rdbTelevizor.setOnClickListener(this@ElonBerishFragment)
            rdbKatyol.setOnClickListener(this@ElonBerishFragment)
            rdbTitan.setOnClickListener(this@ElonBerishFragment)
            exit.setOnClickListener {
                val navController: NavController =
                    Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
                navController.popBackStack()
            }
            nextBack.setOnClickListener {
                mapImage.visibility = View.GONE
                nextBack.visibility = View.GONE
                lat = 0.0
                long = 0.0
            }
            imgClear.setOnClickListener {
                ImageLinear.visibility = View.GONE
                ImageAdd.visibility = View.VISIBLE
                adapter.models.clear()
                adapter.notifyDataSetChanged()
            }
            txtViloyatlar.setOnClickListener {
                binding.vilRecView.adapter = vilAdapter
                vilLayout.visibility = View.VISIBLE
                scrollView.visibility = View.GONE
            }
            viloyatClose.setOnClickListener {
                vilLayout.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                txtViloyatlar.text = "Kvartira manzili"
            }
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
            if (mAuth.currentUser != null) {
                etTel.setText(mAuth.currentUser?.phoneNumber)
            }
            pulBirligi.adapter = adapter(pulBirlik)
            spBolimlar.adapter = adapter(bolimlar)
            spUyTamiri.adapter = adapter(tamiri)
            spMebel.adapter = adapter(mebel)
            spNarxKelishish.adapter = adapter(mebel)
            txtMap.setOnClickListener {
                if (isNetworkAvialable()) {
                    getLastLocation()
                } else {
                    Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT)
                        .show()
                }


            }
            ImageAdd.setOnClickListener {
                adapter.models.clear()
                picImageIntent()
            }
            rasmlar.setOnClickListener {
                adapter.models.clear()
                picImageIntent()
            }
            vilAdapter.onClickItemListener { vilNomi ->
                vilLayout.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                txtViloyatlar.text = vilNomi
            }
            elonJoylash.setOnClickListener {
                if (spBolimlar.selectedItemPosition != 0) {
                    spBolimlar.setBackgroundResource(R.drawable.shape_stroke)
                }
                if (spUyTamiri.selectedItemPosition != 0) {
                    spUyTamiri.setBackgroundResource(R.drawable.shape_stroke)
                }
                if (txtViloyatlar.text != "Kvartira manzili") {
                    txtViloyatlar.setBackgroundResource(R.drawable.shape_stroke)
                }
                if (lat != 0.0) {
                    txtMap.setBackgroundResource(R.drawable.shape_stroke)
                }
                if (isNetworkAvialable()) {
                    elonBerishViewModel.setElonData(
                        adapter,
                        etSarlavha,
                        spBolimlar,
                        etQavatliligi,
                        etMaydoni,
                        etOshxonaMaydoni,
                        spUyTamiri,
                        etYashashMaydoni,
                        etNarx,
                        etQavati,
                        etXonaSoni,
                        etTavsif,
                        txtMap,
                        etTel,
                        pulBirligi,
                        spMebel,
                        spNarxKelishish,
                        sharoitlari,
                        qurilishTuri,
                        lat,
                        long,
                        txtViloyatlar
                    )
                } else {
                    Toast.makeText(requireActivity(), "Not Internet Connection", Toast.LENGTH_SHORT)
                        .show()
                }


            }
            mapBack.setOnClickListener {
                (activity as MainActivity).check = false
                binding.MapContainer.visibility = View.GONE
                lat = 0.0
                long = 0.0
            }
            elonBerishViewModel.elonList.observe(viewLifecycleOwner, Observer {
                when (it.status) {
                    ResourceState.SUCCESS -> {
                        if (it.data == "success")
                            loading.visibility = View.VISIBLE
                    }
                    ResourceState.ERROR -> {
                        Snackbar.make(view, it.message.toString(), Snackbar.LENGTH_SHORT).show()
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
        }

    }

    private fun adapter(ItemList: Array<String>): SpinnerAdapter {
        val adapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, ItemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }

    private fun getLastLocation() {
        if (checkPermission()) {
            if (isGPSEnable()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        newLocationData()
                    } else {
                        binding.MapContainer.visibility = View.VISIBLE
                        (activity as MainActivity).check = true
                        map()
                    }
                }
            } else {
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
        } else {
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun map() {
        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled
            mMap.uiSettings.isMapToolbarEnabled
            mMap.setOnMapClickListener { it ->
                binding.MapLoc.setBackgroundResource(R.color.colorPrimary)
                binding.MapLoc.setTextColor(resources.getColor(R.color.white))
                val markerOptions = MarkerOptions()
                markerOptions.position(it)
                lat = it.latitude
                long = it.longitude
                markerOptions.title("$lat : $long")
                mMap.clear()
                mMap.addMarker(markerOptions)
                binding.MapLoc.setOnClickListener {
                    binding.MapContainer.visibility = View.GONE
                    binding.mapImage.visibility = View.VISIBLE
                    binding.nextBack.visibility = View.VISIBLE
                    mMap.snapshot { bitmap ->
                        binding.mapImage.setImageBitmap(bitmap)
                    }
                }

            }

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

    fun isNetworkAvialable(): Boolean {
        val conManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val internetInfo = conManager.activeNetworkInfo

        return internetInfo != null && internetInfo.isConnected
    }

    override fun onClick(v: View?) {
        binding.apply {
            when (v!!.id) {
                R.id.rdbGishtli -> {
                    fiveCheckRadioButton(rdbGishtli, rdbPanelli, rdbBlokli, rdbMonolitli, rdbYogoch)
                    isChecked(rdbGishtli)
                }
                R.id.rdbPanelli -> {
                    fiveCheckRadioButton(rdbPanelli, rdbGishtli, rdbBlokli, rdbMonolitli, rdbYogoch)
                    isChecked(rdbPanelli)
                }
                R.id.rdbBlokli -> {
                    fiveCheckRadioButton(rdbBlokli, rdbPanelli, rdbGishtli, rdbMonolitli, rdbYogoch)
                    isChecked(rdbBlokli)
                }
                R.id.rdbMonolitli -> {
                    fiveCheckRadioButton(rdbMonolitli, rdbPanelli, rdbBlokli, rdbGishtli, rdbYogoch)
                    isChecked(rdbMonolitli)
                }
                R.id.rdbYogoch -> {
                    fiveCheckRadioButton(rdbYogoch, rdbPanelli, rdbBlokli, rdbMonolitli, rdbGishtli)
                    isChecked(rdbYogoch)
                }
                R.id.rdbGilam -> {
                    if (isCheck[0] == 0) {
                        rdbGilam.isChecked = true
                        isCheck[0] = 1
                    } else {
                        rdbGilam.isChecked = false
                        isCheck[0] = 0
                    }
                    isCheckedSharoit(rdbGilam)
                }
                R.id.rdbKirMashina -> {
                    if (isCheck[1] == 0) {
                        rdbKirMashina.isChecked = true
                        isCheck[1] = 1
                    } else {
                        rdbKirMashina.isChecked = false
                        isCheck[1] = 0
                    }
                    isCheckedSharoit(rdbKirMashina)
                }
                R.id.rdbOshxona -> {
                    if (isCheck[2] == 0) {
                        rdbOshxona.isChecked = true
                        isCheck[2] = 1
                    } else {
                        rdbOshxona.isChecked = false
                        isCheck[2] = 0
                    }
                    isCheckedSharoit(rdbOshxona)
                }
                R.id.rdbBalkon -> {
                    if (isCheck[3] == 0) {
                        rdbBalkon.isChecked = true
                        isCheck[3] = 1
                    } else {
                        rdbBalkon.isChecked = false
                        isCheck[3] = 0
                    }
                    isCheckedSharoit(rdbBalkon)
                }
                R.id.rdbWiFi -> {
                    if (isCheck[4] == 0) {
                        rdbWiFi.isChecked = true
                        isCheck[4] = 1
                    } else {
                        rdbWiFi.isChecked = false
                        isCheck[4] = 0
                    }
                    isCheckedSharoit(rdbWiFi)
                }
                R.id.rdbMuzlatgich -> {
                    if (isCheck[5] == 0) {
                        rdbMuzlatgich.isChecked = true
                        isCheck[5] = 1
                    } else {
                        rdbMuzlatgich.isChecked = false
                        isCheck[5] = 0
                    }
                    isCheckedSharoit(rdbMuzlatgich)
                }
                R.id.rdbAriston -> {
                    if (isCheck[6] == 0) {
                        rdbAriston.isChecked = true
                        isCheck[6] = 1
                    } else {
                        rdbAriston.isChecked = false
                        isCheck[6] = 0
                    }
                    isCheckedSharoit(rdbAriston)
                }
                R.id.rdbTelevizor -> {
                    if (isCheck[7] == 0) {
                        rdbTelevizor.isChecked = true
                        isCheck[7] = 1
                    } else {
                        rdbTelevizor.isChecked = false
                        isCheck[7] = 0
                    }
                    isCheckedSharoit(rdbTelevizor)
                }
                R.id.rdbKonditsioner -> {
                    if (isCheck[8] == 0) {
                        rdbKonditsioner.isChecked = true
                        isCheck[8] = 1
                    } else {
                        rdbKonditsioner.isChecked = false
                        isCheck[8] = 0
                    }
                    isCheckedSharoit(rdbKonditsioner)
                }
                R.id.rdbVanna -> {
                    if (isCheck[9] == 0) {
                        rdbVanna.isChecked = true
                        isCheck[9] = 1
                    } else {
                        rdbVanna.isChecked = false
                        isCheck[9] = 0
                    }
                    isCheckedSharoit(rdbVanna)
                }
                R.id.rdbKatyol -> {
                    if (isCheck[10] == 0) {
                        rdbKatyol.isChecked = true
                        isCheck[10] = 1
                    } else {
                        rdbKatyol.isChecked = false
                        isCheck[10] = 0
                    }
                    isCheckedSharoit(rdbKatyol)
                }
                R.id.rdbTitan -> {
                    if (isCheck[11] == 0) {
                        rdbTitan.isChecked = true
                        isCheck[11] = 1
                    } else {
                        rdbTitan.isChecked = false
                        isCheck[11] = 0
                    }
                    isCheckedSharoit(rdbTitan)
                }
            }
        }
    }

    private fun isChecked(rdb: RadioButton) {
        if (rdb.isChecked) {
            qurilishTuri = rdb.text.toString()
        }
    }

    private fun isCheckedSharoit(rdb: RadioButton) {
        if (rdb.isChecked) {
            if (!sharoitlari.contains(rdb.text.toString())) {
                sharoitlari.add(rdb.text.toString())
            }
        } else {
            sharoitlari.remove(rdb.text.toString())
        }
    }
}
