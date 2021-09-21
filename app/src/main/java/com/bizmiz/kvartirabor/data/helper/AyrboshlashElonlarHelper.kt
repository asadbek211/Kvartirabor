package com.bizmiz.kvartirabor.data.helper

import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.model.ElonData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.ArrayList

class AyrboshlashElonlarHelper(private val db: FirebaseFirestore) {
    fun getAyirboshlashData(
        onSuccess: (listElon: ArrayList<ElonData>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val listAyirboshlash: ArrayList<ElonData> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION)
            .orderBy("createdDate", Query.Direction.DESCENDING).get().addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(ElonData::class.java)
                    if (model != null && model.bolim == Constant.AYIRBOSHLASH) {
                        listAyirboshlash.add(model)
                    }
                }
                onSuccess.invoke(listAyirboshlash)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}