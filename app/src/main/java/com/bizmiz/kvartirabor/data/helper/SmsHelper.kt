package com.bizmiz.kvartirabor.data.helper

import com.bizmiz.kvartirabor.data.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SmsHelper(
    private val mAuth: FirebaseAuth, private val db: FirebaseFirestore
) {
    fun sentPhoneNumber(
        phoneNumber: String,
        onFailure: (msg: String?) -> Unit,
    ) {
        db.collection(Constant.USERS).document(mAuth.currentUser?.uid!!).get()
            .addOnCompleteListener {
                if (it.isSuccessful && !it.result?.exists()!!) {
                    val map: MutableMap<String, Any?> = mutableMapOf()
                    map[Constant.PHONE_NUMBER] = phoneNumber
                    db.collection(Constant.USERS)
                        .document(mAuth.currentUser?.uid!!.toString())
                        .set(map)
                        .addOnFailureListener { e ->
                          onFailure.invoke(e.localizedMessage)
                        }
                }
            }
    }

}