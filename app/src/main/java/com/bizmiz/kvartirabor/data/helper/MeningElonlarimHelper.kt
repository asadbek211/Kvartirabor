package com.bizmiz.kvartirabor.data.helper

import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.model.ElonlarimData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MeningElonlarimHelper(private val mAuth: FirebaseAuth, private val db: FirebaseFirestore) {

    fun getElonlarimData(
        onSuccess: (listElon: ArrayList<ElonlarimData>) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (isCheck: String?) -> Unit
    ) {
        val list: ArrayList<ElonlarimData> = arrayListOf()

        db.collection(Constant.BASE_COLLECTION)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (mAuth.currentUser != null) {
                    db.collection(Constant.BASE_COLLECTION)
                        .orderBy("createdDate", Query.Direction.DESCENDING).get()
                        .addOnSuccessListener {
                            check.invoke("false")
                            it.documents.forEach { doc ->
                                val model = doc.toObject(ElonlarimData::class.java)
                                if (model != null && model.uid==mAuth.currentUser!!.uid) list.add(model)
                            }
                            onSuccess.invoke(list)
                        }.addOnFailureListener {
                            onFailure.invoke(it.localizedMessage)
                        }
                } else {
                    check.invoke("true")
                }

            }

    }
}