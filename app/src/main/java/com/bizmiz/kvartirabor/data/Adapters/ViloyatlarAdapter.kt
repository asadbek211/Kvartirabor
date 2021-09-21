package com.bizmiz.kvartirabor.data.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.data.vilovatlar
import com.bizmiz.kvartirabor.databinding.ViloyatlarItemBinding

class ViloyatlarAdapter : RecyclerView.Adapter<ViloyatlarAdapter.Myholder>() {
    var onClickItem: (viloyat: String) -> Unit = {}
    fun onClickItemListener(onClickItem: (viloyat: String) -> Unit){
        this.onClickItem = onClickItem
    }
    inner class Myholder(private val binding: ViloyatlarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateModel(position: Int) {
            binding.vilNomi.text = vilovatlar[position]
          binding.vilNomi.setOnClickListener {
              onClickItem.invoke(vilovatlar[position])
          }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
        val bolimBinding =
            ViloyatlarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Myholder(bolimBinding)
    }

    override fun onBindViewHolder(holder: Myholder, position: Int) {
        holder.populateModel(position)
    }

    override fun getItemCount(): Int = vilovatlar.size
}
