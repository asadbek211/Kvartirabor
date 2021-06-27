package com.bizmiz.kvartirabor.ui.elon

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.ContainerActivity
import com.bizmiz.kvartirabor.R
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_elon_berish.*

class ElonBerishFragment : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private var qoshimcha = true
    private val mAuth = FirebaseAuth.getInstance()
    private val pulBirlik: Array<String> = arrayOf("So'm", "$")
    private val xonaSoni: Array<String> = arrayOf("1", "2", "3", "4", "5", "5+")
    private val ijarachiSoni: Array<String> = arrayOf("0", "1", "2", "3", "4", "5", "5+")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elon_berish, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val prefs: SharedPreferences =
            requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val pos1 = prefs.getFloat("position1", 46.3434f)
        val pos2 = prefs.getFloat("position2", 46.3434f)
        if (mAuth.currentUser != null) {
            et_tel.setText(mAuth.currentUser?.phoneNumber)
        }

        qoshimchaMalumot.setOnClickListener {
            if (qoshimcha) {
                AsosiyConsLayout.visibility = View.VISIBLE
                qoshimcha = false
            } else {
                AsosiyConsLayout.visibility = View.GONE
                qoshimcha = true
            }
        }
        rdbSotiladi.setOnClickListener {
            radioButton(rdbIjaraBerish)
            lay2.visibility = View.GONE
            lay3.visibility = View.GONE
            lay5.visibility = View.GONE
            lay7.visibility = View.GONE
            lay9.visibility = View.GONE
            consa.visibility = View.GONE
            textInputLayout6.visibility = View.VISIBLE
        }
        rdbIjaraBerish.setOnClickListener {
            radioButton(rdbSotiladi)
            lay2.visibility = View.VISIBLE
            lay3.visibility = View.VISIBLE
            lay5.visibility = View.VISIBLE
            lay7.visibility = View.VISIBLE
            lay9.visibility = View.VISIBLE
            consa.visibility = View.VISIBLE
            textInputLayout6.visibility = View.GONE
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
            radioButton1(rdbKelishimli, rdbKunlik)
        }
        rdbKelishimli.setOnClickListener {
            radioButton1(rdbDoimiy, rdbKunlik)
        }
        rdbKunlik.setOnClickListener {
            radioButton1(rdbDoimiy, rdbKelishimli)
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
        btn_map.setOnClickListener {
            getLastLocation()
            val intent = Intent(requireContext(), ContainerActivity::class.java)
            intent.putExtra("num",2)
            startActivity(intent)


        }
        elonJoylash.setOnClickListener {
            val fayl = rdbKopQavat.isChecked
            val fayl2 = rdbYerJoy.isChecked
            Toast.makeText(
                requireContext(), "Ko'p qavatli: $fayl\nYer joy: $fayl2\n" +
                        "Manzil: ${et_manzil.text}\nNomer: ${et_tel.text}", Toast.LENGTH_SHORT
            ).show()
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

    fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager
}