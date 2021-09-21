package com.bizmiz.kvartirabor.data.helper

import android.content.SharedPreferences
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.model.ElonData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SaralanganlarHelper(
    private val db: FirebaseFirestore
) {

    fun getSaralanganlarData(
        prefs:SharedPreferences,
        onSuccess: (listElon: ArrayList<ElonData>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val list: ArrayList<ElonData> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION)
            .orderBy("createdDate", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(ElonData::class.java)
                    if (model != null && prefs.contains(model.id)) list.add(
                        model
                    )
                }
                onSuccess.invoke(list)
            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}