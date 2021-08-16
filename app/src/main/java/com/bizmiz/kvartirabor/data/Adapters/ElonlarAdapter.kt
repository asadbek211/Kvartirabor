package com.bizmiz.kvartirabor.data.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bizmiz.kvartirabor.data.model.ElonData
import com.bizmiz.kvartirabor.databinding.ElonlarItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ElonlarAdapter(var models: ArrayList<ElonData>, var filterType: Boolean) :
    RecyclerView.Adapter<ElonlarAdapter.MyViewHolder>(),
    Filterable {
    var filterList: ArrayList<ElonData> = arrayListOf()

    init {
        filterList = models
    }

    inner class MyViewHolder(private val binding: ElonlarItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun funksiya(data: ElonData) {
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
                binding.txtData.text = String.format("%s", dateString)


            binding.layTouch.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Buning ustida hali ishlanyabdi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @ExperimentalStdlibApi
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = models
                } else {
                    val resultList = ArrayList<ElonData>()
                    for (row in models) {
                        if (filterType) {
                            if (row.manzil.lowercase(Locale.ROOT)
                                    .contains(charSearch.lowercase(Locale.ROOT))
                            ) {
                                resultList.add(row)
                            }
                        } else {
                            if (row.narxi.lowercase(Locale.ROOT)
                                    .contains(charSearch.lowercase(Locale.ROOT))
                            ) {
                                resultList.add(row)
                            }
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<ElonData>
                notifyDataSetChanged()
            }

        }
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