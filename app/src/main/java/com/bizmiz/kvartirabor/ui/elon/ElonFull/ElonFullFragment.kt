package com.bizmiz.kvartirabor.ui.elon.ElonFull

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Adapters.ElonlarAdapter
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.databinding.FragmentElonFullBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bizmiz.kvartirabor.ui.elon.Elonlar.ElonlarViewModel
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.synnapps.carouselview.ImageListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat

class ElonFullFragment : Fragment(R.layout.fragment_elon_full) {
    private val viewModel: ElonlarViewModel by viewModel()
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var elonlarAdapter: ElonlarAdapter
    private lateinit var binding: FragmentElonFullBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        binding = FragmentElonFullBinding.bind(view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(),
                R.color.splashColor
            )
        }else{
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.marshmallow)
        }
        val id = requireArguments().getString("id")
         val prefs: SharedPreferences = binding.root.context.getSharedPreferences(Constant.PREFS, MODE_PRIVATE)
         val editor: SharedPreferences.Editor =prefs.edit()
        if (prefs.contains(id)) {
            binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite)
        }
        binding.imgFavourite.setOnClickListener {
            if (prefs.contains(id)) {
                binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite)
                editor.remove(id).apply()
            } else {
                binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                editor.putBoolean(id,true).apply()
            }
        }
        (activity as MainActivity).visibility(true)
        binding.apply {
            titleRt.alpha = 0.0F
            xarita.setOnClickListener {
                mapView.visibility = View.VISIBLE
                mapClose.visibility = View.VISIBLE
            }
            mapClose.setOnClickListener {
                mapView.visibility = View.GONE
                mapClose.visibility = View.GONE
            }
            val imageUrl = requireArguments().getStringArrayList("imageUrlList")
            courserView.setImageListener(imageListener)
            courserView.pageCount = imageUrl!!.size
            courserView.setImageClickListener {
                scNested.visibility = View.GONE
                fullImageCl.visibility = View.VISIBLE
                fullCourserView.setImageListener(imageListener)
                fullCourserView.pageCount = imageUrl.size
                (activity as MainActivity).check = true
            }
            fullCourserView.setImageClickListener {
                scNested.visibility = View.VISIBLE
                fullImageCl.visibility = View.GONE
                (activity as MainActivity).check = false
            }

            exit.setOnClickListener {
                val navController: NavController =
                    Navigation.findNavController(requireActivity(), R.id.mainFragmentContener)
                navController.popBackStack()
            }
            val createData = requireArguments().getLong("createdDate")
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateString = simpleDateFormat.format(createData)
            txtCreateData.text = String.format("%s", dateString)
            txtSarlavha.text = requireArguments().getString("sarlavha")
            txtTavsif.text = requireArguments().getString("tavsif")
            xonaSoni.text = "${xonaSoni.text} ${requireArguments().getString("xonaSoni")}"
            if (!requireArguments().getString("umumiyMaydon").isNullOrEmpty()) {
                umumiyMaydon.visibility = View.VISIBLE
                umumiyMaydon.text =
                    "${umumiyMaydon.text} ${requireArguments().getString("umumiyMaydon")} m²"
            }
            if (!requireArguments().getString("yashashMaydoni").isNullOrEmpty()) {
                yashashMaydon.visibility = View.VISIBLE
                yashashMaydon.text =
                    "${yashashMaydon.text} ${requireArguments().getString("yashashMaydoni")} m²"
            }
            if (!requireArguments().getString("oshxonaMaydoni").isNullOrEmpty()) {
                oshxonaMaydon.visibility = View.VISIBLE
                oshxonaMaydon.text =
                    "${oshxonaMaydon.text} ${requireArguments().getString("oshxonaMaydoni")} m²"
            }
            qavati.text = "${qavati.text} ${requireArguments().getString("yashashQavati")}"
            uyQavatliligi.text =
                "${uyQavatliligi.text} ${requireArguments().getString("uyQavatliligi")}"
            uyTamiri.text = "${uyTamiri.text} ${requireArguments().getString("uyTamiri")}"
            mebel.text = "${mebel.text} ${requireArguments().getString("mebel")}"
            qurilishTuri.text =
                "${qurilishTuri.text} ${requireArguments().getString("qurilishTuri")}"
            telQilish.setOnClickListener {
                val phone = telNumber.text.toString()
                val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
                startActivity(intent)
            }
            val sharoit = requireArguments().getStringArrayList("sharoitlari")
            if (sharoit!!.isNotEmpty()) {
                sharoitlari.visibility = View.VISIBLE
                val sharoiti = sharoit.toString().replace("[", "").replace("]", "")
                sharoitlari.text = "${sharoitlari.text} $sharoiti"
            }
            telNumber.text = requireArguments().getString("telRaqam")
            val kelishuvi = requireArguments().getString("kelishuv")
            if (kelishuvi!!.contains("Ha")) txtKelishish.text = "Kelishiladi"
            else txtKelishish.text = "Kelishilmaydi"
            txtSumma.text =
                "${requireArguments().getString("narxi")} ${requireArguments().getString("type")}"
            scNested.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                if (scrollY > oldScrollY) {
                    for (i in oldScrollY..scrollY step 10) {
                        if (i <= 100) {
                            val float = (i / 100F)
                            titleRt.alpha = float
                        }
                    }
                }
                if (scrollY < oldScrollY) {
                    for (i in scrollY..oldScrollY step 10) {
                        if (i <= 100) {
                            val float = (i / 100F)
                            titleRt.alpha = float
                        }
                    }
                }
                if (scrollY == 0) {
                    titleRt.alpha = 0.0F
                }
            })
        }
        map()
    }
    var imageListener =
        ImageListener { position, imageView ->
            val image = requireArguments().getStringArrayList("imageUrlList")
            Glide.with(imageView).load(image!![position]).into(imageView)
        }
    private fun setObservers() {
        viewModel.elon.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                ResourceState.SUCCESS -> {
                    elonlarAdapter = ElonlarAdapter()
                    elonlarAdapter.models = it.data!!
                }
                ResourceState.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
    private fun map() {
        val lat = requireArguments().getDouble("latitude")
        val long = requireArguments().getDouble("longitude")
        val viloyat = requireArguments().getString("viloyat")
        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.uiSettings.isZoomControlsEnabled
            binding.area.text = viloyat
            val markerOptions = MarkerOptions()
            markerOptions.position(LatLng(lat, long))
            markerOptions.title("$viloyat")
            mMap.addMarker(markerOptions)
            val myPosition = CameraPosition.Builder()
                .target(LatLng(lat, long)).zoom(17f).bearing(90f).tilt(30f).build()
            googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(myPosition)
            )
        }
    }

}