package com.bizmiz.kvartirabor.ui.elon

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bizmiz.kvartirabor.Adapter.ElonlarAdapter
import com.bizmiz.kvartirabor.Adapter.MeniElonlarimAdapter
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.ElonData
import com.bizmiz.kvartirabor.data.ElonlarimData
import com.bizmiz.kvartirabor.databinding.FragmentMeningElonlarimBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MeningElonlarimFragment : Fragment(R.layout.fragment_mening_elonlarim) {
    lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    lateinit var adapter: MeniElonlarimAdapter
    lateinit var binding: FragmentMeningElonlarimBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).visibility(false)
        binding = FragmentMeningElonlarimBinding.bind(view)
        adapter = MeniElonlarimAdapter()
        binding.recView.adapter = adapter
        setData()
    }

    fun setData() {
        mAuth = FirebaseAuth.getInstance()
        val list: ArrayList<ElonlarimData> = arrayListOf()

        db.collection("elonlar")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                    return@addSnapshotListener
                }
                if (mAuth.currentUser != null) {
                    db.collection("elonlar").whereEqualTo("uid", mAuth.currentUser!!.uid).get()
                        .addOnSuccessListener {
                            binding.notReg.visibility = View.VISIBLE
                            binding.notReg.text = "Siz e'lon bermagansiz"
                            it.documents.forEach { doc ->
                                binding.notReg.visibility = View.GONE
                                val model = doc.toObject(ElonlarimData::class.java)
                                if (model != null) list.add(model)
                            }
                            adapter.models = list
                            binding.loading.visibility = View.GONE
                        }.addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                it.localizedMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    binding.loading.visibility = View.GONE
                    binding.notReg.visibility = View.VISIBLE
                }

            }

    }
}
