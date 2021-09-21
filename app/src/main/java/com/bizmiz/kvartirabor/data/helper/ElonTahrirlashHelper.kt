package com.bizmiz.kvartirabor.data.helper

import android.widget.Spinner
import android.widget.TextView
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.checkIsEmpty
import com.bizmiz.kvartirabor.data.showError
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ElonTahrirlashHelper(private val db: FirebaseFirestore) {
    fun setElonData(
        id: String?,
        sarlavha: TextInputEditText,
        bolim: Spinner,
        uyQavatliligi: TextInputEditText,
        umumiyMaydon: TextInputEditText,
        oshxonaMaydoni: TextInputEditText,
        uyTamiri: Spinner,
        yashashMaydoni: TextInputEditText,
        narxi: TextInputEditText,
        yashashQavati: TextInputEditText,
        xonaSoni: TextInputEditText,
        tavsif: TextInputEditText,
        joyNomi: TextView,
        telRaqam: TextInputEditText,
        type: Spinner,
        mebel: Spinner,
        kelishuv: Spinner,
        sharoitlari: ArrayList<String>,
        qurilishTuri: String,
        latitude: Double,
        longitude: Double,
        txtVil: TextView,
        onSuccess: (succes: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        if (validate(
                sarlavha,
                bolim,
                uyQavatliligi,
                uyTamiri,
                narxi,
                yashashQavati,
                xonaSoni,
                tavsif,
                joyNomi,
                telRaqam,
                txtVil,
                latitude,
                onFailure
            )
        ) {
            check.invoke("validate")
            setData(
                id,
                sarlavha.text.toString(),
                bolim.selectedItem.toString(),
                uyQavatliligi.text.toString(),
                umumiyMaydon.text.toString(),
                oshxonaMaydoni.text.toString(),
                uyTamiri.selectedItem.toString(),
                yashashMaydoni.text.toString(),
                narxi.text.toString(),
                yashashQavati.text.toString(),
                xonaSoni.text.toString(),
                tavsif.text.toString(),
                telRaqam.text.toString(),
                type.selectedItem.toString(),
                mebel.selectedItem.toString(),
                kelishuv.selectedItem.toString(),
                sharoitlari,
                qurilishTuri,
                latitude,
                longitude,
                txtVil.text.toString(),
                onSuccess,
                onFailure,
                check
            )
        }
    }

    private fun setData(
        id: String?,
        sarlavha: String,
        bolim: String,
        uyQavatliligi: String,
        umumiyMaydon: String,
        oshxonaMaydoni: String,
        uyTamiri: String,
        yashashMaydoni: String,
        narxi: String,
        yashashQavati: String,
        xonaSoni: String,
        tavsif: String,
        telRaqam: String,
        type: String,
        mebel: String,
        kelishuv: String,
        sharoitlari: ArrayList<String>,
        qurilishTuri: String,
        latitude: Double,
        longitude: Double,
        txtVil: String,
        onSuccess: (succes: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        val map: MutableMap<String, Any?> = mutableMapOf()
        map["sarlavha"] = sarlavha
        map["bolim"] = bolim
        map["uyQavatliligi"] = uyQavatliligi
        map["umumiyMaydon"] = umumiyMaydon
        map["oshxonaMaydoni"] = oshxonaMaydoni
        map["uyTamiri"] = uyTamiri
        map["yashashMaydoni"] = yashashMaydoni
        map["narxi"] = narxi
        map["yashashQavati"] = yashashQavati
        map["xonaSoni"] = xonaSoni
        map["tavsif"] = tavsif
        map["telRaqam"] = telRaqam
        map["type"] = type
        map["mebel"] = mebel
        map["kelishuv"] = kelishuv
        map["sharoitlari"] = sharoitlari
        map["qurilishTuri"] = qurilishTuri
        map["latitude"] = latitude
        map["longitude"] = longitude
        map["viloyat"] = txtVil
        if (!id.isNullOrEmpty()) {
            db.collection(Constant.BASE_COLLECTION).document(id).update(map)
                .addOnSuccessListener {
                    onSuccess.invoke("updateSuccess")

                }
                .addOnFailureListener { e ->
                    onFailure.invoke(e.localizedMessage)
                }
            check.invoke("update")
        }
    }

    private fun validate(
        sarlavha: TextInputEditText,
        bolim: Spinner,
        uyQavatliligi: TextInputEditText,
        uyTamiri: Spinner,
        narxi: TextInputEditText,
        yashashQavati: TextInputEditText,
        xonaSoni: TextInputEditText,
        tavsif: TextInputEditText,
        joyNomi: TextView,
        telRaqam: TextInputEditText,
        txtVil: TextView,
        latitude: Double,
        onFailure: (msg: String?) -> Unit
    ): Boolean {
        return when {
            sarlavha.checkIsEmpty() -> {
                sarlavha.showError("To'ldirish majburiy")
                false
            }
            bolim.selectedItemPosition == 0 -> {
                bolim.setBackgroundResource(R.drawable.shape_stroke_error)
                onFailure.invoke("Bo'lim tanlang")
                false
            }
            uyQavatliligi.checkIsEmpty() -> {
                uyQavatliligi.showError("To'ldirish majburiy")
                false
            }
            uyTamiri.selectedItemPosition == 0 -> {
                uyTamiri.setBackgroundResource(R.drawable.shape_stroke_error)
                onFailure.invoke("Uy tamirini tanlang")
                false
            }
            narxi.checkIsEmpty() -> {
                narxi.showError("To'ldirish majburiy")
                false
            }
            yashashQavati.checkIsEmpty() -> {
                yashashQavati.showError("To'ldirish majburiy")
                false
            }
            xonaSoni.checkIsEmpty() -> {
                xonaSoni.showError("To'ldirish majburiy")
                false
            }
            tavsif.checkIsEmpty() -> {
                tavsif.showError("To'ldirish majburiy")
                false
            }
            txtVil.text=="Kvartira manzili" -> {
                txtVil.setBackgroundResource(R.drawable.shape_stroke_error)
                onFailure.invoke("Manzilni tanlang")
                false
            }
            latitude ==0.0 -> {
                joyNomi.setBackgroundResource(R.drawable.shape_stroke_error)
                onFailure.invoke("Joylashuvni tanlang")
                false
            }
            telRaqam.checkIsEmpty() -> {
                telRaqam.showError("To'ldirish majburiy")
                false
            }

            else -> true

        }
    }
}