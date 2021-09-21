package com.bizmiz.kvartirabor.data.Adapters

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.model.ElonData
import com.bizmiz.kvartirabor.databinding.ElonlarItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat

class SaralanganlarAdapter : RecyclerView.Adapter<SaralanganlarAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: ElonlarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val prefs: SharedPreferences = binding.root.context.getSharedPreferences(Constant.PREFS, MODE_PRIVATE)
        private val editor: SharedPreferences.Editor =prefs.edit()
        fun funksiya(data: ElonData) {
            binding.apply {
                txtSarlavha.text = data.sarlavha
                txtNarxi.text = data.narxi
                txtType.text = data.type

            }
            if (data.imageUrlList.isNotEmpty()) {
                val options: RequestOptions = RequestOptions()
                    .placeholder(R.drawable.elon_img)
                    .error(R.drawable.elon_img)
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
                    .load(R.drawable.elon_img)
                    .into(binding.image)
            }
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            val dateString = simpleDateFormat.format(data.createdDate)
            binding.txtData.text = String.format("%s", dateString)
            if (prefs.contains(data.id)){
                binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }else{
                binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite)
            }

            binding.layTouch.setOnClickListener {
                onClick.invoke(data)

            }
            binding.imgFavourite.setOnClickListener {
                if (prefs.contains(data.id)){
                    binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite)
                    saralash.invoke(true,data.id,editor)
                }else{
                    binding.imgFavourite.setImageResource(R.drawable.ic_baseline_favorite_24)
                    saralash.invoke(false,data.id,editor)
                }

            }
        }
    }

    var filterList: ArrayList<ElonData> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onClick: (data: ElonData) -> Unit = {}
    fun setOnClickListener(onClick: (data: ElonData) -> Unit) {
        this.onClick = onClick
    }

    var saralash: (key:Boolean,id: String, editor: SharedPreferences.Editor) -> Unit =
        { key:Boolean,id: String, editor: SharedPreferences.Editor -> }

    fun saralanganlarListener(saralash: (key:Boolean,id: String, editor: SharedPreferences.Editor) -> Unit) {
        this.saralash = saralash
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding =
            ElonlarItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.funksiya(filterList[position])
    }

    override fun getItemCount(): Int = filterList.size
}