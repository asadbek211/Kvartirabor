package com.bizmiz.kvartirabor.ui.elon.ElonBerish

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.data.Adapters.ImageAdapter
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.checkIsEmpty
import com.bizmiz.kvartirabor.databinding.FragmentElonBerishBinding
import com.bizmiz.kvartirabor.data.showError
import com.bizmiz.kvartirabor.data.Constant
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.set

class ElonBerishFragment : Fragment(R.layout.fragment_elon_berish) {
    private var imgUrl: ArrayList<String> = arrayListOf()
    private lateinit var adapter: ImageAdapter
    private val db = FirebaseFirestore.getInstance()
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
        binding.imageRecView.adapter = adapter
        binding.exit.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navController.popBackStack()
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        val prefs: SharedPreferences =
            requireActivity().getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE)
        if (mAuth.currentUser != null) {
            binding.etTel.setText(mAuth.currentUser?.phoneNumber)
        }

        binding.qoshimchaMalumot.setOnClickListener {

            if (qoshimcha) {
                Toast.makeText(
                    requireContext(),
                    "Kechirasiz bu bo'lim hali tayyor emas",
                    Toast.LENGTH_SHORT
                ).show()
                binding.AsosiyConsLayout.visibility = View.VISIBLE
                qoshimcha = false
            } else {
                binding.AsosiyConsLayout.visibility = View.GONE
                qoshimcha = true
            }
        }
        binding.rdbSotiladi.setOnClickListener {
            radioButton(binding.rdbIjaraBerish)
            binding.lay2.visibility = View.GONE
            binding.lay3.visibility = View.GONE
            binding.lay5.visibility = View.GONE
            binding.lay7.visibility = View.GONE
            binding.lay9.visibility = View.GONE
            binding.consa.visibility = View.GONE
            binding.textInputLayout6.visibility = View.VISIBLE
        }
        binding.rdbIjaraBerish.setOnClickListener {
            radioButton(binding.rdbSotiladi)
            binding.lay2.visibility = View.VISIBLE
            binding.lay3.visibility = View.VISIBLE
            binding.lay5.visibility = View.VISIBLE
            binding.lay7.visibility = View.VISIBLE
            binding.lay9.visibility = View.VISIBLE
            binding.consa.visibility = View.VISIBLE
            binding.textInputLayout6.visibility = View.GONE
        }
        binding.rdbKopQavat.setOnClickListener {
            radioButton(binding.rdbYerJoy)
        }
        binding.rdbYerJoy.setOnClickListener {
            radioButton(binding.rdbKopQavat)
        }
        binding.rdbYakkaTolash.setOnClickListener {
            radioButton(binding.rdbUmumiyTolash)
        }
        binding.rdbUmumiyTolash.setOnClickListener {
            radioButton(binding.rdbYakkaTolash)
        }

        binding.rdbDoimiy.setOnClickListener {
            radioButton1(binding.rdbKelishimli, binding.rdbKunlik)
        }
        binding.rdbKelishimli.setOnClickListener {
            radioButton1(binding.rdbDoimiy, binding.rdbKunlik)
        }
        binding.rdbKunlik.setOnClickListener {
            radioButton1(binding.rdbDoimiy, binding.rdbKelishimli)
        }
        binding.rdbNarxIchida.setOnClickListener {
            radioButton1(binding.rdbNarxTashqari, binding.rdbKelishiladi)
        }
        binding.rdbNarxTashqari.setOnClickListener {
            radioButton1(binding.rdbNarxIchida, binding.rdbKelishiladi)
        }
        binding.rdbKelishiladi.setOnClickListener {
            radioButton1(binding.rdbNarxTashqari, binding.rdbNarxIchida)
        }
        binding.pulBirligi.adapter = adapter(pulBirlik)
        binding.xonalarSoni.adapter = adapter(xonaSoni)
        binding.yangiIjarachilarSoni.adapter = adapter(xonaSoni)
        binding.ijaradagilarSoni.adapter = adapter(ijarachiSoni)
        binding.btnMap.setOnClickListener {
            getLastLocation()

        }
        binding.ImageAdd.setOnClickListener {
            adapter.models.clear()
            picImageIntent()
        }
        binding.rasmlar.setOnClickListener {
            adapter.models.clear()
            picImageIntent()
        }
        binding.elonJoylash.setOnClickListener {
            val uuid = UUID.randomUUID().toString()
            if (validate()) {
                binding.loading.visibility = View.VISIBLE
                val fayl = binding.rdbKopQavat.isChecked
                val fayl2 = binding.rdbYerJoy.isChecked

                if (adapter.models.isNullOrEmpty()) {
                    setData(imgUrl, uuid, prefs)
                }
                for (i in 0 until adapter.models.size) {
                    val storeRef = FirebaseStorage.getInstance().reference.child("${uuid}/image$i")
                    storeRef.putFile(adapter.models[i]!!)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                                    imgUrl.add(uri.toString())
                                    //map
                                    if (i == adapter.models.size - 1) {
                                        setData(imgUrl, uuid, prefs)
                                    }
                                    //map
                                }
                            }

                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }

                }
            }


        }

    }

    private fun setData(imgUrl: ArrayList<String>, uuid: String, prefs: SharedPreferences) {

        val map: MutableMap<String, Any?> = mutableMapOf()
        map["id"] = uuid
        map["imageUrlList"] = imgUrl
        map["uid"] = mAuth.currentUser?.uid.toString()
        map["manzil"] = binding.etManzil.text.toString()
        map["telefon_raqam"] = binding.etTel.text.toString()
        map["narxi"] = binding.etNarx.text.toString()
        map["createdDate"] = System.currentTimeMillis()
        map["type"] = binding.pulBirligi.selectedItem
        map["latitude"] = prefs.getFloat("position1", 46.3434f)
        map["longitude"] = prefs.getFloat("position2", 46.3434f)
        db.collection(Constant.BASE_COLLECTION).document(map["id"].toString()).set(map)
            .addOnSuccessListener {
                binding.loading.visibility = View.VISIBLE

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

    private fun validate(): Boolean {
        return when {
            binding.etManzil.checkIsEmpty() -> {
                binding.etManzil.showError("Field Required")
                false
            }
            binding.etTel.checkIsEmpty() -> {
                binding.etTel.showError("Field Required")
                false
            }
            binding.etNarx.checkIsEmpty() -> {
                binding.etNarx.showError("Field Required")
                false
            }
            else -> true

        }
    }

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
}