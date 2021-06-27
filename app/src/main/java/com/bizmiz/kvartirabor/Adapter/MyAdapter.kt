package com.bizmiz.kvartirabor.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.data
import kotlinx.android.synthetic.main.elonlar_item.view.*

class MyAdapter:RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        fun funksiya(data: data){
            itemView.txt_manzil.text = data.manzil
            itemView.txt_narxi.text = data.narxi
            itemView.txt_nomer.text = data.nomer
        }
    }
     var models:ArrayList<data> = arrayListOf()
     set(value) {
         field = value
         notifyDataSetChanged()
     }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
     val view = LayoutInflater.from(parent.context).inflate(R.layout.elonlar_item,parent,false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      holder.funksiya(models[position])
    }

    override fun getItemCount(): Int = models.size
}