package com.bizmiz.kvartirabor.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentMapBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment(R.layout.fragment_map) {
    lateinit var binding: FragmentMapBinding
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(true)
        binding = FragmentMapBinding.bind(view)
        val prefs: SharedPreferences =
            requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled
            mMap.uiSettings.isMapToolbarEnabled
            mMap.setOnMapClickListener {
                val markerOptions = MarkerOptions()
                markerOptions.position(it)
                val position1 = it.latitude
                val position2 = it.longitude
                editor.putFloat("position1", position1.toFloat())
                editor.putFloat("position2", position2.toFloat())
                editor.apply()
                markerOptions.title("$position1 : $position2")
                mMap.clear()
                mMap.addMarker(markerOptions)

                mMap.snapshot { bitmap ->

                    binding.avatar.setImageBitmap(bitmap)
                }
            }

        }
        binding.MapLoc.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
            navController.popBackStack()
        }
    }
}