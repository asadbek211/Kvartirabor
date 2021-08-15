package com.bizmiz.kvartirabor.ui.elon

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bizmiz.kvartirabor.Adapter.ElonlarAdapter
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.ElonData
import com.bizmiz.kvartirabor.databinding.FragmentElonlarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class ElonlarFragment : Fragment(R.layout.fragment_elonlar) {
    private var check = true
    lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    lateinit var adapter: ElonlarAdapter
    lateinit var binding: FragmentElonlarBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentElonlarBinding.bind(view)
        setData()
        binding.etSearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.etSearch.clearFocus();
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }

        })
        binding.swipeContainer.setOnRefreshListener {
            setData()
        }

    }

    private fun setData() {
        val list: ArrayList<ElonData> = arrayListOf()
        mAuth = FirebaseAuth.getInstance()
        db.collection("elonlar").addSnapshotListener { value, error ->
            if (error != null) {
                Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_SHORT)
                    .show()
                return@addSnapshotListener
            }
            db.collection("elonlar").get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(ElonData::class.java)
                    if (model != null) list.add(model)
                }
                binding.swipeContainer.isRefreshing = false
                binding.loading.visibility = View.GONE
                adapter = ElonlarAdapter(list, true)
                binding.recView.adapter = adapter
                binding.imgFilter.setOnClickListener {
                    check = if (check) {
                        adapter = ElonlarAdapter(list, false)
                        adapter.notifyDataSetChanged()
                        binding.recView.adapter = adapter
                        Toast.makeText(
                            requireActivity(),
                            "narx bo'yicha saralash",
                            Toast.LENGTH_SHORT
                        ).show()
                        false
                    } else {
                        adapter = ElonlarAdapter(list, true)
                        adapter.notifyDataSetChanged()
                        binding.recView.adapter = adapter
                        Toast.makeText(
                            requireActivity(),
                            "manzil bo'yicha saralash",
                            Toast.LENGTH_SHORT
                        ).show()
                        true
                    }

                }
            }

        }
    }

}