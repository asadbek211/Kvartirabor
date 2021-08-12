package com.bizmiz.kvartirabor.data

data class ElonData(
     val id:String="",
     val uid:String="",
     val imageUrlList:ArrayList<String> = arrayListOf(),
     val manzil:String="",
     val telefon_raqam:String="",
     val narxi:String="",
     val type:String="",
     val latitude:Float= 0f,
     val longitude:Float= 0f
)