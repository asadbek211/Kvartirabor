package com.bizmiz.kvartirabor.data.helper

import android.widget.Spinner
import android.widget.TextView
import com.bizmiz.kvartirabor.R
import com.bizmiz.kvartirabor.data.Adapters.ImageAdapter
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.checkIsEmpty
import com.bizmiz.kvartirabor.data.showError
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ElonBerishHelper(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) {
    fun setElonData(
        adapter: ImageAdapter,
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
        onSuccess: (succes: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        val imgUrl: ArrayList<String> = arrayListOf()
        val uuid = UUID.randomUUID().toString()
        if (validate(
                adapter,
                sarlavha,
                bolim,
                uyQavatliligi,
                umumiyMaydon,
                oshxonaMaydoni,
                uyTamiri,
                yashashMaydoni,
                narxi,
                yashashQavati,
                xonaSoni,
                tavsif,
                joyNomi,
                telRaqam,
                onFailure
            )
        ) {
            check.invoke("validate")
            for (i in 0 until adapter.models.size) {
                val storeRef = FirebaseStorage.getInstance().reference.child("${uuid}/image$i")
                storeRef.putFile(adapter.models[i]!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                                imgUrl.add(uri.toString())
                                //map
                                if (imgUrl.size == adapter.models.size) {
                                    setData(
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
                                        imgUrl,
                                        uuid,
                                        latitude,
                                        longitude,
                                        onSuccess,
                                        onFailure,
                                        check
                                    )
                                }
                                //map
                            }
                        }

                    }
                    .addOnFailureListener {
                        onFailure.invoke(it.localizedMessage)
                    }
            }
        }
    }

    private fun setData(
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
        imgUrl: ArrayList<String>,
        uuid: String,
        latitude: Double,
        longitude: Double,
        onSuccess: (succes: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {

        val map: MutableMap<String, Any?> = mutableMapOf()
        map["id"] = uuid
        map["imageUrlList"] = imgUrl
        map["uid"] = mAuth.currentUser?.uid.toString()
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
        map["createdDate"] = System.currentTimeMillis()
        map["latitude"] = latitude
        map["longitude"] = longitude
        db.collection(Constant.BASE_COLLECTION).document(map["id"].toString()).set(map)
            .addOnSuccessListener {
                onSuccess.invoke("success")

            }
            .addOnFailureListener { e ->
                onFailure.invoke(e.localizedMessage)
            }
        check.invoke("check")
    }

    private fun validate(
        adapter: ImageAdapter,
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
        onFailure: (msg: String?) -> Unit
    ): Boolean {
        return when {
            adapter.models.isNullOrEmpty() -> {
                onFailure.invoke("E'loningizga rasm qo'shing")
                false
            }
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
            umumiyMaydon.checkIsEmpty() -> {
                umumiyMaydon.showError("To'ldirish majburiy")
                false
            }
            oshxonaMaydoni.checkIsEmpty() -> {
                oshxonaMaydoni.showError("To'ldirish majburiy")
                false
            }
            uyTamiri.selectedItemPosition == 0 -> {
                uyTamiri.setBackgroundResource(R.drawable.shape_stroke_error)
                onFailure.invoke("Uy tamirini tanlang")
                false
            }
            yashashMaydoni.checkIsEmpty() -> {
                yashashMaydoni.showError("To'ldirish majburiy")
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
            joyNomi.text == "Joylashish manzili" -> {
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