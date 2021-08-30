package com.bizmiz.kvartirabor.data.model

import java.util.*

data class ElonData(
    val id: String = "",//
    val uid: String = "",//
    val sarlavha: String = "",//
    val bolim: String = "",//
    val uyQavatliligi: String = "",//
    val umumiyMaydon: String = "",//
    val oshxonaMaydoni: String = "",//
    val uyTamiri: String = "",//
    val yashashMaydoni: String = "",//
    val narxi: String = "",//
    val yashashQavati: String = "",//
    val xonaSoni: String = "",//
    val tavsif: String = "",//
    val telRaqam: String = "",
    val type: String = "",//
    val mebel: String = "",//
    val kelishuv: String = "",//
    val sharoitlari: ArrayList<String> = arrayListOf(),
    val qurilishTuri: String = "",//
    val imageUrlList: ArrayList<String> = arrayListOf(),//
    val uuid: String = "",//
    val createdDate:Long = 0,//
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)