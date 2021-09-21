package com.bizmiz.kvartirabor.data.helper

import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.model.ElonlarimData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference

class MeningElonlarimHelper(
    private val mAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storageRef: StorageReference
) {

    fun getElonlarimData(
        onSuccess: (listElon: ArrayList<ElonlarimData>) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (isCheck: String?) -> Unit
    ) {
        val list: ArrayList<ElonlarimData> = arrayListOf()
        if (mAuth.currentUser != null) {
            db.collection(Constant.BASE_COLLECTION)
                .orderBy("createdDate", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    check.invoke("false")
                    it.documents.forEach { doc ->
                        val model = doc.toObject(ElonlarimData::class.java)
                        if (model != null && model.uid == mAuth.currentUser!!.uid) list.add(
                            model
                        )
                    }
                    onSuccess.invoke(list)
                }.addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
        } else {
            check.invoke("true")
        }
    }

    fun deleteItem(
        data: ElonlarimData,
        onSuccess: (onSuccess: String) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        val id = data.id
        val imgUrl = data.imageUrlList
        db.collection(Constant.BASE_COLLECTION).document(id).delete().addOnSuccessListener {
            for (i in 0 until imgUrl.size) {
                storageRef.child("$id/image$i").delete().addOnSuccessListener {
                    if (i == imgUrl.size - 1) {
                        onSuccess.invoke("E'loningiz o'chirildi")
                    }
                }.addOnFailureListener {
                    onFailure.invoke(it.localizedMessage)
                }
            }
        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }


}