package com.bizmiz.kvartirabor.data.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.data.model.ElonlarimData
import com.bizmiz.kvartirabor.databinding.MeniElonlarimItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat

class MeniElonlarimAdapter :
    RecyclerView.Adapter<MeniElonlarimAdapter.MyViewHolder>() {

    inner class MyViewHolder(private val binding: MeniElonlarimItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun funksiya(data: ElonlarimData, position: Int) {
            binding.apply {
                txtSarlavha.text = data.sarlavha
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
            binding.txtData.text = String.format("%s", dateString)


            binding.layTouch.setOnClickListener {
              onClick.invoke(data)
            }
            binding.cardDelete.setOnClickListener {
                delete.invoke(data)
            }
            binding.cardEdit.setOnClickListener {
                update.invoke(data)
            }
        }
    }
      var delete:(data:ElonlarimData)->Unit = {}
    fun itemDelete(delete:(data:ElonlarimData)->Unit){
        this.delete = delete
    }
    var update:(data:ElonlarimData)->Unit = {}
    fun itemUpdate(update:(data:ElonlarimData)->Unit){
        this.update = update
    }
    var onClick:(data:ElonlarimData)->Unit = {}
    fun setOnClickListener(onClick:(data:ElonlarimData)->Unit){
        this.onClick = onClick
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