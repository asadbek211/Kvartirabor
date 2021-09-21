package com.bizmiz.kvartirabor.data.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.model.BolimModel
import com.bizmiz.kvartirabor.databinding.BolimlarItemBinding
import com.bumptech.glide.Glide

class BolimAdapter : RecyclerView.Adapter<BolimAdapter.Myholder>() {
    val image: ArrayList<Int> =
        arrayListOf(R.drawable.logotip_play, R.drawable.for_rent,R.drawable.house_sale,R.drawable.almashuv)

    inner class Myholder(private val binding: BolimlarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(bolimModel: BolimModel,position: Int) {
            binding.txtText.text = bolimModel.txtText
            Glide.with(itemView).load(image[position]).into(binding.bolimImg)
            binding.constLay.setOnClickListener {
                onclick.invoke(position)
            }
        }

    }

    var models: ArrayList<BolimModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
   var onclick:(position:Int)->Unit = {}
    fun onClickListener(onclick:(position:Int)->Unit){
        this.onclick = onclick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
        val bolimBinding =
            BolimlarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Myholder(bolimBinding)
    }

    override fun onBindViewHolder(holder: Myholder, position: Int) {
        holder.populateModel(models[position],position)
    }

    override fun getItemCount(): Int = models.size
}