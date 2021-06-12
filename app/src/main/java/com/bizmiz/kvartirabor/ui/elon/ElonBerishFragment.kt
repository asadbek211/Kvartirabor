package com.bizmiz.kvartirabor.ui.elon

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import kotlinx.android.synthetic.main.fragment_elon_berish.*

class ElonBerishFragment : Fragment() {
    private val pulBirlik: Array<String> = arrayOf("So'm","$")
    private val xonaSoni: Array<String> = arrayOf("1","2","3","4","5","5+")
    private val ijarachiSoni: Array<String> = arrayOf("0","1","2","3","4","5","5+")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elon_berish, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs: SharedPreferences = requireActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        val pos1 = prefs.getFloat("position1",46.3434f)
        val pos2 = prefs.getFloat("position2",46.3434f)
        loc1.text = pos1.toString()
        loc2.text = pos2.toString()
        pulBirligi.adapter = adapter(pulBirlik)
        xonalarSoni.adapter = adapter(xonaSoni)
        yangiIjarachilarSoni.adapter = adapter(xonaSoni)
        ijaradagilarSoni.adapter = adapter(ijarachiSoni)
        btn_map.setOnClickListener {
            val navController: NavController = Navigation.findNavController(requireActivity(),R.id.appFragmentContener)
            navController.navigate(R.id.mapFragment)
        }
    }
    private fun adapter(ItemList:Array<String>): SpinnerAdapter? {
        val adapter = ArrayAdapter(requireActivity(), R.layout.spinner_item, ItemList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        return adapter
    }
}