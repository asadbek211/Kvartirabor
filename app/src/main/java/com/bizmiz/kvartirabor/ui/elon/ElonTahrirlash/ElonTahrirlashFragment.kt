package com.bizmiz.kvartirabor.ui.elon.ElonTahrirlash

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
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
import com.bizmiz.kvartirabor.data.Adapters.ViloyatlarAdapter
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.data.fiveCheckRadioButton
import com.bizmiz.kvartirabor.databinding.FragmentElonTahrirlashBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ElonTahrirlashFragment : Fragment(R.layout.fragment_elon_tahrirlash), View.OnClickListener {
    private lateinit var mapFragment: SupportMapFragment
    private var isCheck: ArrayList<Int> = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    private var lat: Double = 0.0
    private var long: Double = 0.0
    private lateinit var mMap: GoogleMap
    private lateinit var vilAdapter:ViloyatlarAdapter
    private val elonTahrirlashViewModel: ElonTahrirlashViewModel by viewModel()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val pulBirlik: Array<String> = arrayOf("So'm", "$")
    private val mebel: Array<String> = arrayOf("Ha", "Yo'q")
    private var sharoitlari: ArrayList<String> = arrayListOf()
    private var qurilishTuri = "G'ishtli"
    private val bolimlar: Array<String> =
        arrayOf("Bo'lim tanlang", "Ijaraga berish", "Sotish", "Ayirboshlash")
    private val tamiri: Array<String> = arrayOf(
        "Ta'miri",
        "Mualliflik loyixasi",
        "Evrota'mir",
        "O'rtacha",
        "Ta'mir talab",
        "Qora suvoq",
        "Tozalashdan avvalgi pardoz"
    )
    lateinit var binding: FragmentElonTahrirlashBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentElonTahrirlashBinding.bind(view)
        vilAdapter = ViloyatlarAdapter()
        binding.apply {
            rdbGishtli.setOnClickListener(this@ElonTahrirlashFragment)
            rdbPanelli.setOnClickListener(this@ElonTahrirlashFragment)
            rdbBlokli.setOnClickListener(this@ElonTahrirlashFragment)
            rdbMonolitli.setOnClickListener(this@ElonTahrirlashFragment)
            rdbYogoch.setOnClickListener(this@ElonTahrirlashFragment)
            rdbGilam.setOnClickListener(this@ElonTahrirlashFragment)
            rdbKirMashina.setOnClickListener(this@ElonTahrirlashFragment)
            rdbOshxona.setOnClickListener(this@ElonTahrirlashFragment)
            rdbBalkon.setOnClickListener(this@ElonTahrirlashFragment)
            rdbKonditsioner.setOnClickListener(this@ElonTahrirlashFragment)
            rdbVanna.setOnClickListener(this@ElonTahrirlashFragment)
            rdbWiFi.setOnClickListener(this@ElonTahrirlashFragment)
            rdbMuzlatgich.setOnClickListener(this@ElonTahrirlashFragment)
            rdbAriston.setOnClickListener(this@ElonTahrirlashFragment)
            rdbTelevizor.setOnClickListener(this@ElonTahrirlashFragment)
            rdbKatyol.setOnClickListener(this@ElonTahrirlashFragment)
            rdbTitan.setOnClickListener(this@ElonTahrirlashFragment)
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
            txtViloyatlar.setOnClickListener {
                binding.vilRecView.adapter = vilAdapter
                vilLayout.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            }
            viloyatClose.setOnClickListener {
                vilLayout.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
                txtViloyatlar.text = "Kvartira manzili"
            }
            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireContext())
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
            if (!requireArguments().getString("sarlavha").isNullOrEmpty()) {
                etSarlavha.setText(requireArguments().getString("sarlavha"))
                etTavsif.setText(requireArguments().getString("tavsif"))
                etXonaSoni.setText(requireArguments().getString("xonaSoni"))
                etMaydoni.setText(requireArguments().getString("umumiyMaydon"))
                etYashashMaydoni.setText(requireArguments().getString("yashashMaydoni"))
                etOshxonaMaydoni.setText(requireArguments().getString("oshxonaMaydoni"))
                etQavati.setText(requireArguments().getString("yashashQavati"))
                etQavatliligi.setText(requireArguments().getString("uyQavatliligi"))
                etTel.setText(requireArguments().getString("telRaqam"))
                etNarx.setText(requireArguments().getString("narxi"))
                spinnerSetSelected(requireArguments().getString("uyTamiri"), spUyTamiri)
                spinnerSetSelected(requireArguments().getString("bolim"), spBolimlar)
                spinnerSetSelected(requireArguments().getString("type"), pulBirligi)
                spinnerSetSelected(requireArguments().getString("kelishuv"), spNarxKelishish)
                spinnerSetSelected(requireArguments().getString("mebel"), spMebel)
                radiobuttonIsChecked(requireArguments().getString("qurilishTuri"))
                sharoitlari =
                    requireArguments().getStringArrayList("sharoitlari") as ArrayList<String>
                sharoitlariIsSelected(requireArguments().getStringArrayList("sharoitlari"))
            }
            vilAdapter.onClickItemListener { vilNomi ->
                vilLayout.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                txtViloyatlar.text = vilNomi
            }
            txtViloyatlar.text = requireArguments().getString("viloyat")
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
                    elonTahrirlashViewModel.setElonData(
                        requireArguments().getString("id"),
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
            elonTahrirlashViewModel.elonTahrirList.observe(viewLifecycleOwner, Observer {
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
                        if (it.check == "update") {
                            val message = AlertDialog.Builder(requireActivity())
                            message.setTitle("Kvartirabor")
                                .setMessage("E'loningiz muaffaqiyatli tahrirlandi.")
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
        map()
    }

    private fun sharoitlariIsSelected(sharoitlari: ArrayList<String>?) {
        binding.apply {
            if (sharoitlari!!.contains("Gilam")) {
                rdbGilam.isChecked = true
            }
            if (sharoitlari.contains("Kir yuvish mashinasi")) {
                rdbKirMashina.isChecked = true
            }
            if (sharoitlari.contains("Oshxona")) {
                rdbOshxona.isChecked = true
            }
            if (sharoitlari.contains("Balkon")) {
                rdbBalkon.isChecked = true
            }
            if (sharoitlari.contains("Konditsioner")) {
                rdbKonditsioner.isChecked = true
            }
            if (sharoitlari.contains("Vanna")) {
                rdbVanna.isChecked = true
            }
            if (sharoitlari.contains("Wi-fi")) {
                rdbWiFi.isChecked = true
            }
            if (sharoitlari.contains("Muzlatgich")) {
                rdbMuzlatgich.isChecked = true
            }
            if (sharoitlari.contains("Ariston")) {
                rdbAriston.isChecked = true
            }
            if (sharoitlari.contains("Televizor")) {
                rdbTelevizor.isChecked = true
            }
            if (sharoitlari.contains("Katyol")) {
                rdbKatyol.isChecked = true
            }
            if (sharoitlari.contains("Titan(Suv isitgich)")) {
                rdbTitan.isChecked = true
            }

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
            val markerOptions = MarkerOptions()
            mMap = googleMap
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled
            mMap.uiSettings.isMapToolbarEnabled
            if (!requireArguments().getString("sarlavha").isNullOrEmpty()) {
                lat = requireArguments().getDouble("latitude")
                long = requireArguments().getDouble("longitude")
                markerOptions.position(LatLng(lat, long))
                markerOptions.title("$lat : $long")
                mMap.clear()
                mMap.addMarker(markerOptions)
                val myPosition = CameraPosition.Builder()
                    .target(LatLng(lat, long)).zoom(17f).bearing(90f).tilt(30f).build()
                googleMap.animateCamera(
                    CameraUpdateFactory.newCameraPosition(myPosition)
                )
                    binding.nextBack.visibility = View.VISIBLE
            }
            mMap.setOnMapClickListener { it ->
                binding.MapLoc.setBackgroundResource(R.color.colorPrimary)
                binding.MapLoc.setTextColor(resources.getColor(R.color.white))
                markerOptions.position(it)
                lat = it.latitude
                long = it.longitude
                markerOptions.title("$lat : $long")
                mMap.clear()
                mMap.addMarker(markerOptions)
                binding.MapLoc.setOnClickListener { view ->
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
            Toast.makeText(requireActivity(), qurilishTuri, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isCheckedSharoit(rdb: RadioButton) {
        if (rdb.isChecked) {
            if (!sharoitlari.contains(rdb.text.toString())) {
                sharoitlari.add(rdb.text.toString())
                Toast.makeText(requireActivity(), sharoitlari.toString(), Toast.LENGTH_SHORT).show()
            }
        } else {
            sharoitlari.remove(rdb.text.toString())
            Toast.makeText(requireActivity(), sharoitlari.toString(), Toast.LENGTH_SHORT).show()
        }

    }

    private fun spinnerSetSelected(argument: String?, sp: Spinner) {
        when (argument) {
            "Mualliflik loyixasi" -> {
                sp.setSelection(1)
            }
            "Evrota'mir" -> {
                sp.setSelection(2)
            }
            "O'rtacha" -> {
                sp.setSelection(3)
            }
            "Ta'mir talab" -> {
                sp.setSelection(4)
            }
            "Qora suvoq" -> {
                sp.setSelection(5)
            }
            "Tozalashdan avvalgi pardoz" -> {
                sp.setSelection(6)
            }
            "Ijaraga berish" -> {
                sp.setSelection(1)
            }
            "Sotish" -> {
                sp.setSelection(2)
            }
            "Ayirboshlash" -> {
                sp.setSelection(3)
            }
            "So'm" -> {
                sp.setSelection(0)
            }
            "$" -> {
                sp.setSelection(1)
            }
            "Ha" -> {
                sp.setSelection(0)
            }
            "Yo'q" -> {
                sp.setSelection(1)

            }
        }
    }

    private fun radiobuttonIsChecked(argument: String?) {
        when (argument) {
            "Panelli" -> {
                binding.rdbPanelli.isChecked = true
                binding.rdbGishtli.isChecked = false
            }
            "Blokli" -> {
                binding.rdbBlokli.isChecked = true
                binding.rdbGishtli.isChecked = false
            }
            "Monolitli" -> {
                binding.rdbMonolitli.isChecked = true
                binding.rdbGishtli.isChecked = false
            }
            "Yog'och" -> {
                binding.rdbYogoch.isChecked = true
                binding.rdbGishtli.isChecked = false
            }

        }
    }
}