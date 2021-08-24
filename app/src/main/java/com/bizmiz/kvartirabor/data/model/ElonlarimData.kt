package com.bizmiz.kvartirabor.data.model

data class ElonlarimData(
    val id:String="",
    val uid:String="",
    val imageUrlList:ArrayList<String> = arrayListOf(),
    val sarlavha:String="",
    val manzil:String="",
    val createdDate: Long = 0,
    val telefon_raqam:String="",
    val narxi:String="",
    val type:String="",
    val latitude:Float= 0f,
    val longitude:Float= 0f
)
