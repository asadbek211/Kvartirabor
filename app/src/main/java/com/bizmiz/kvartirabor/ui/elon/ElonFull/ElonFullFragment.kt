package com.bizmiz.kvartirabor.ui.elon.ElonFull

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.databinding.FragmentElonFullBinding
import com.bizmiz.kvartirabor.ui.MainActivity
import com.bumptech.glide.Glide
import com.synnapps.carouselview.ImageListener
import java.text.SimpleDateFormat

class ElonFullFragment : Fragment(R.layout.fragment_elon_full) {
    private lateinit var binding: FragmentElonFullBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentElonFullBinding.bind(view)

        (activity as MainActivity).visibility(true)
        binding.apply {
            titleRt.alpha = 0.0F
            val image = requireArguments().getStringArrayList("image")
            courserView.setImageListener(imageListener)
            courserView.pageCount = image!!.size
            courserView.setImageClickListener {
                scNested.visibility = View.GONE
                fullImageCl.visibility = View.VISIBLE
                fullCourserView.setImageListener(imageListener)
                fullCourserView.pageCount = image.size
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
            txtManzil.text = requireArguments().getString("manzil")
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

    }

    var imageListener =
        ImageListener { position, imageView ->
            val image = requireArguments().getStringArrayList("image")
            Glide.with(imageView).load(image!![position]).into(imageView)
        }

}