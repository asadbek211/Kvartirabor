package com.bizmiz.kvartirabor.data.helper

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.bizmiz.kvartirabor.data.Adapters.ImageAdapter
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.ResourceState
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
        manzil: TextInputEditText,
        telNomer: TextInputEditText,
        narx: TextInputEditText,
        type: String,
        prefs: SharedPreferences,
        onSuccess: (succes: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {
        val imgUrl: ArrayList<String> = arrayListOf()
        val uuid = UUID.randomUUID().toString()
        if (validate(sarlavha, manzil, telNomer, narx)) {
            check.invoke("validate")
            if (adapter.models.isNullOrEmpty()) {
                setData(
                    sarlavha.text.toString(),
                    manzil.text.toString(),
                    telNomer.text.toString(),
                    narx.text.toString(),
                    type,
                    imgUrl,
                    uuid,
                    prefs,
                    onSuccess,
                    onFailure,
                    check
                )
            }
            for (i in 0 until adapter.models.size) {
                val storeRef = FirebaseStorage.getInstance().reference.child("${uuid}/image$i")
                storeRef.putFile(adapter.models[i]!!)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            it.result.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                                imgUrl.add(uri.toString())
                                //map
                                if (i == adapter.models.size - 1) {
                                    setData(
                                        sarlavha.text.toString(),
                                        manzil.text.toString(),
                                        telNomer.text.toString(),
                                        narx.text.toString(),
                                        type,
                                        imgUrl,
                                        uuid,
                                        prefs,
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
        manzil: String,
        telNomer: String,
        narx: String,
        type: String,
        imgUrl: ArrayList<String>,
        uuid: String,
        prefs: SharedPreferences,
        onSuccess: (succes: String?) -> Unit,
        onFailure: (msg: String?) -> Unit,
        check: (msg: String?) -> Unit
    ) {

        val map: MutableMap<String, Any?> = mutableMapOf()
        map["id"] = uuid
        map["imageUrlList"] = imgUrl
        map["uid"] = mAuth.currentUser?.uid.toString()
        map["sarlavha"] = sarlavha
        map["manzil"] = manzil
        map["telefon_raqam"] = telNomer
        map["narxi"] = narx
        map["createdDate"] = System.currentTimeMillis()
        map["type"] = type
        map["latitude"] = prefs.getFloat("position1", 46.3434f)
        map["longitude"] = prefs.getFloat("position2", 46.3434f)
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
        sarlavha: TextInputEditText,
        manzil: TextInputEditText,
        telNomer: TextInputEditText,
        narx: TextInputEditText
    ): Boolean {
        return when {
            sarlavha.checkIsEmpty() -> {
                sarlavha.showError("Field Required")
                false
            }
            manzil.checkIsEmpty() -> {
                manzil.showError("Field Required")
                false
            }
            telNomer.checkIsEmpty() -> {
                telNomer.showError("Field Required")
                false
            }
            narx.checkIsEmpty() -> {
                narx.showError("Field Required")
                false
            }
            else -> true

        }
    }
}