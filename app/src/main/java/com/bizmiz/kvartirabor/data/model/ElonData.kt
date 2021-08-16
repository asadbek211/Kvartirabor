package com.bizmiz.kvartirabor.data.model

data class ElonData(
     val id:String="",
     val uid:String="",
     val imageUrlList:ArrayList<String> = arrayListOf(),
     val manzil:String="",
     val createdDate: Long = 0,
     val telefon_raqam:String="",
     val narxi:String="",
     val type:String="",
     val latitude:Float= 0f,
     val longitude:Float= 0f
)