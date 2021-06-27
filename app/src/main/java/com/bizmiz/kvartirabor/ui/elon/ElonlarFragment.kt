package com.bizmiz.kvartirabor.ui.elon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bizmiz.kvartirabor.Adapter.MyAdapter
import com.bizmiz.kvartirabor.MainActivity
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.data
import kotlinx.android.synthetic.main.fragment_elonlar.*

class ElonlarFragment : Fragment() {
   lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        var list:ArrayList<data> = arrayListOf()
        for (i in 20..30){
            list.add(data("Nukus shaxar $i mikrarayon","500 ming","+99890324${i}${i-1}"))

        }
        adapter.models = list
    }
}