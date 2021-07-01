package com.bizmiz.kvartirabor.ui.elon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bizmiz.kvartirabor.Adapter.MyAdapter
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.ElonData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_elonlar.*

class ElonlarFragment : Fragment() {
    lateinit var mAuth: FirebaseAuth
    private  val db = FirebaseFirestore.getInstance()
   lateinit var adapter: MyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_elonlar, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyAdapter()
        recView.adapter = adapter
        setData()
    }
    fun setData(){
        val list:MutableList<ElonData> = mutableListOf()
        mAuth = FirebaseAuth.getInstance()
       db.collection("elonlar").addSnapshotListener { value, error ->
           if (error !=null){
               Toast.makeText(requireContext(), error.message.toString(), Toast.LENGTH_SHORT).show()
           return@addSnapshotListener
           }
           db.collection("elonlar").get().addOnSuccessListener {
               it.documents.forEach { doc ->
                   val model = doc.toObject(ElonData::class.java)
                   if (model!=null) list.add(model)
               }
               adapter.models = list
               if (loading!=null){
               loading.visibility = View.GONE}
           }

       }

    }
}