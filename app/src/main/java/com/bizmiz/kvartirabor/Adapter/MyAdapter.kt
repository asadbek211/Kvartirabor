package com.bizmiz.kvartirabor.Adapter

import android.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.data.ElonData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.elonlar_item.view.*


class MyAdapter : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun funksiya(data: ElonData) {
            itemView.txt_manzil.text = data.manzil
            itemView.txt_nomer.text = data.telefon_raqam
            itemView.txt_narxi.text = data.narxi
            itemView.txt_type.text = data.type
            itemView.txt_lat.text = data.latitude.toString()
            itemView.txt_long.text = data.longitude.toString()
            if (data.imageUrlList.isNotEmpty()){
                val options: RequestOptions = RequestOptions()
                    .placeholder(com.bizmiz.kvartirabor.R.drawable.net_url_img)
                    .error(com.bizmiz.kvartirabor.R.drawable.net_url_img)
                data.imageUrlList.forEach {
                    if (it.contains("image0")){
                        val firstImageUrl = it
                        Glide.with(itemView.context)
                            .load(firstImageUrl)
                            .apply(options)
                            .into(itemView.image)
                    }
                }
            }else{
                Glide.with(itemView.context)
                    .load(com.bizmiz.kvartirabor.R.drawable.net_url_img)
                    .into(itemView.image)
            }

            itemView.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Buning ustida hali ishlanyabdi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    var models: MutableList<ElonData> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.bizmiz.kvartirabor.R.layout.elonlar_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.funksiya(models[position])
    }

    override fun getItemCount(): Int = models.size
}