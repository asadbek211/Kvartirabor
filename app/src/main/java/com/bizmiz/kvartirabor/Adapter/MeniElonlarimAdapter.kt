package com.bizmiz.kvartirabor.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.data.ElonlarimData
import com.bizmiz.kvartirabor.databinding.MeniElonlarimItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat

class MeniElonlarimAdapter() :
    RecyclerView.Adapter<MeniElonlarimAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: MeniElonlarimItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun funksiya(data: ElonlarimData,position: Int) {
            binding.apply {
                txtManzil.text = data.manzil
                txtNarxi.text = data.narxi
                txtType.text = data.type
            }
            if (data.imageUrlList.isNotEmpty()) {
                val options: RequestOptions = RequestOptions()
                    .placeholder(com.bizmiz.kvartirabor.R.drawable.elon_img)
                    .error(com.bizmiz.kvartirabor.R.drawable.elon_img)
                data.imageUrlList.forEach {
                    if (it.contains("image0")) {
                        val firstImageUrl = it
                        Glide.with(itemView.context)
                            .load(firstImageUrl)
                            .apply(options)
                            .into(binding.image)
                    }
                }
            } else {
                Glide.with(itemView.context)
                    .load(com.bizmiz.kvartirabor.R.drawable.elon_img)
                    .into(binding.image)
            }
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateString = simpleDateFormat.format(data.createdDate)
            binding.txtData.text = String.format("Date: %s", dateString)


            binding.layTouch.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Buning ustida hali ishlanyabdi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.cardDelete.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "$position - elon o'chirildi",
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.cardEdit.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "$position - elon tahrirlanyabdi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    var models: MutableList<ElonlarimData> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            MeniElonlarimItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.funksiya(models[position],position)
    }

    override fun getItemCount(): Int = models.size
}