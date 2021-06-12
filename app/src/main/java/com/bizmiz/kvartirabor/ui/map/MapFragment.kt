package com.bizmiz.kvartirabor.ui.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*

class MapFragment : Fragment() {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs: SharedPreferences = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {googleMap ->
            mMap = googleMap
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.setOnMapClickListener {
                val markerOptions = MarkerOptions()
                markerOptions.position(it)
                val  position1 = it.latitude
                val position2 = it.longitude
                editor.putFloat("position1", position1.toFloat())
                editor.putFloat("position2", position2.toFloat())
                editor.apply()
                markerOptions.title("$position1 : $position2")
                mMap.clear()
                mMap.addMarker(markerOptions)

            }
            val markerOptions = MarkerOptions()
            val pos1 = prefs.getFloat("position1",46.3434f)
            val pos2 = prefs.getFloat("position2",46.3434f)
            markerOptions.position(LatLng(pos1.toDouble(), pos2.toDouble()))
            mMap.addMarker(markerOptions)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(pos1.toDouble(), pos2.toDouble()), 15f))
        }
        MapLoc.setOnClickListener {
            val navController: NavController = Navigation.findNavController(requireActivity(),R.id.mainFragmentContener)
            navController.navigate(R.id.elonBerishFragment)
        }
    }
}