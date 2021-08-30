package com.bizmiz.kvartirabor.ui.elon.ElonFull

import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentElonFullBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.synnapps.carouselview.ImageListener
import java.text.SimpleDateFormat
import java.util.*
import com.google.android.gms.maps.model.CameraPosition




class ElonFullFragment : Fragment(R.layout.fragment_elon_full) {
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentElonFullBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentElonFullBinding.bind(view)

        (activity as MainActivity).visibility(true)
        binding.apply {
            titleRt.alpha = 0.0F
            xarita.setOnClickListener {
                mapView.visibility = View.VISIBLE
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
            umumiyMaydon.text = "${umumiyMaydon.text} ${requireArguments().getString("umumiyMaydon")} m²"
            yashashMaydon.text = "${yashashMaydon.text} ${requireArguments().getString("yashashMaydoni")} m²"
            oshxonaMaydon.text = "${oshxonaMaydon.text} ${requireArguments().getString("oshxonaMaydoni")} m²"
            qavati.text = "${qavati.text} ${requireArguments().getString("yashashQavati")}"
            uyQavatliligi.text = "${uyQavatliligi.text} ${requireArguments().getString("uyQavatliligi")}"
            uyTamiri.text = "${uyTamiri.text} ${requireArguments().getString("uyTamiri")}"
            mebel.text = "${mebel.text} ${requireArguments().getString("mebel")}"
            qurilishTuri.text = "${qurilishTuri.text} ${requireArguments().getString("qurilishTuri")}"
            val sharoit = requireArguments().getStringArrayList("sharoitlari")?.toString()
            val sgaroiti = sharoit!!.replace("[","").replace("]","")
            sharoitlari.text = "${sharoitlari.text} $sgaroiti"
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

    private fun map() {
        val lat = requireArguments().getDouble("latitude")
        val long = requireArguments().getDouble("longitude")

        mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled
            mMap.uiSettings.isMapToolbarEnabled
                val markerOptions = MarkerOptions()
                markerOptions.position(LatLng(lat,long))
                markerOptions.title("$lat : $long")
                mMap.addMarker(markerOptions)
            val myPosition = CameraPosition.Builder()
                .target(LatLng(lat,long)).zoom(17f).bearing(90f).tilt(30f).build()
            googleMap.animateCamera(
                CameraUpdateFactory.newCameraPosition(myPosition));
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                      val address = geoCoder.getFromLocation(lat,long, 1)
                     binding.area.text = address[0].subAdminArea
                     val respublika = address[0].adminArea
                     binding.subArea.text = respublika.replace("Respublikasi","").replace("Republic of","")
                    mMap.setOnMapClickListener {
                        binding.mapView.visibility = View.GONE
                        mMap.snapshot { bitmap ->
                            binding.xarita.setImageBitmap(bitmap)
                        }
                    }

                } catch (e: NumberFormatException) {
                    Toast.makeText(requireActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
                }



        }

//        val geoCoder = Geocoder(requireContext(), Locale.getDefault())
//        try {

////
//        } catch (e: NumberFormatException) {
//            Toast.makeText(requireActivity(), e.localizedMessage, Toast.LENGTH_SHORT).show()
//        }
    }

}