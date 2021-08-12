package com.bizmiz.kvartirabor.ui.elon

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
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
import com.bizmiz.kvartirabor.Adapter.ImageAdapter
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.checkIsEmpty
import com.bizmiz.kvartirabor.showError
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_elon_berish.*
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.MutableMap
import kotlin.collections.mutableMapOf
import kotlin.collections.set

class ElonBerishFragment : Fragment() {
    private   var imgUrl:ArrayList<String> = arrayListOf()
    private lateinit var adapter:ImageAdapter
    private val db = FirebaseFirestore.getInstance()
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
        adapter = ImageAdapter()
        imageRecView.adapter = adapter
        exit.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navController.popBackStack()
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val prefs: SharedPreferences =
            requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        if (mAuth.currentUser != null) {
            et_tel.setText(mAuth.currentUser?.phoneNumber)
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
           val  uuid = UUID.randomUUID().toString()
            if (validate()) {
                if (loading != null) {
                    loading.visibility = View.VISIBLE
                }
                val fayl = rdbKopQavat.isChecked
                val fayl2 = rdbYerJoy.isChecked
                for (i in 0 until adapter.models.size){
                    val storeRef = FirebaseStorage.getInstance().reference.child("${uuid}/image$i")
                    storeRef.putFile(adapter.models[i]!!)
                        .addOnCompleteListener {
                           if (it.isSuccessful){
                               it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener {uri->
                                   imgUrl.add(uri.toString())
                               }
                           }

                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                }
                if (imgUrl.size == adapter.models.size || adapter.models.size ==0 ){
                    Log.d("image",imgUrl.toString())
                    val map: MutableMap<String, Any?> = mutableMapOf()
                    map["id"] = uuid
                    map["imageUrlList"] = imgUrl
                    map["uid"] = mAuth.currentUser?.uid.toString()
                    map["manzil"] = et_manzil.text.toString()
                    map["telefon_raqam"] = et_tel.text.toString()
                    map["narxi"] = etNarx.text.toString()
                    map["type"] = pulBirligi.selectedItem
                    map["latitude"] = prefs.getFloat("position1", 46.3434f)
                    map["longitude"] = prefs.getFloat("position2", 46.3434f)
                    db.collection("elonlar").document(map["id"].toString()).set(map)
                        .addOnSuccessListener {
                            if (loading != null) {
                                loading.visibility = View.VISIBLE
                            }

                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_SHORT)
                                .show()
                        }
                    Toast.makeText(requireActivity(), "E'lon berildi", Toast.LENGTH_SHORT)
                        .show()
                    val navController: NavController = Navigation.findNavController(
                        requireActivity(),
                        R.id.mainFragmentContener
                    )
                    navController.popBackStack()
                }


            }


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

    private fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager

    private fun validate(): Boolean {
        return when {
            et_manzil.checkIsEmpty() -> {
                et_manzil.showError("Field Required")
                false
            }
            et_tel.checkIsEmpty() -> {
                et_tel.showError("Field Required")
                false
            }
            etNarx.checkIsEmpty() -> {
                etNarx.showError("Field Required")
                false
            }
            else -> true

        }
    }
    private fun picImageIntent(){
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Select image(s)"),1)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode==1){
            if (resultCode== Activity.RESULT_OK){
                if (data!!.clipData != null){
                    val image: ArrayList<Uri?> = arrayListOf()
                    val cout = data.clipData!!.itemCount
                    for (i in 0 until cout ){
                        val imageUrl = data.clipData!!.getItemAt(i).uri
                        image.add(imageUrl)
                    }
                    adapter.models.clear()
                    adapter.notifyDataSetChanged()
                    adapter.models = image
                    ImageAdd.visibility = View.GONE
                    ImageLinear.visibility = View.VISIBLE
                }
                else{
                    val image: ArrayList<Uri?> = arrayListOf()
                    val imageUrl = data.data
                    ImageAdd.visibility = View.GONE
                    ImageLinear.visibility = View.VISIBLE
                    image.add(imageUrl)
                    adapter.models.clear()
                    adapter.notifyDataSetChanged()
                    adapter.models = image

                }
            }
        }
    }
}