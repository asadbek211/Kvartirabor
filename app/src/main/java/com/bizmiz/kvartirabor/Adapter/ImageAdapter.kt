package com.bizmiz.kvartirabor.Adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.image_item.view.*

class ImageAdapter:RecyclerView.Adapter<ImageAdapter.Myholder>() {
    inner class Myholder(itemView:View):RecyclerView.ViewHolder(itemView) {
       fun populateModel(position: Int){
           Glide.with(itemView).load(models[position]).into(itemView.image)
       }
    }
    var models:ArrayList<Uri?> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item,parent,false)
        return Myholder(view)
    }

    override fun onBindViewHolder(holder: Myholder, position: Int) {
        holder.populateModel(position)
    }

    override fun getItemCount(): Int = models.size
}