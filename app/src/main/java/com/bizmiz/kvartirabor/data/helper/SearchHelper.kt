package com.bizmiz.kvartirabor.data.helper

import android.util.Log
import com.bizmiz.kvartirabor.data.Constant
import com.bizmiz.kvartirabor.data.model.ElonData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchHelper(
    private val db: FirebaseFirestore
) {

    fun getSearchData(
        key: Boolean,
        query: String,
        bolim: String,
        hudud: String,
        valyuta: String,
        minSum: Int,
        maxSum: Int,
        onSuccess: (listElon: ArrayList<ElonData>) -> Unit,
        onFailure: (msg: String?) -> Unit
    ) {
        Log.d("getData", "$key , $query , $bolim , $hudud , $valyuta , $minSum , $maxSum")
        val queryList: ArrayList<ElonData> = arrayListOf()
        db.collection(Constant.BASE_COLLECTION)
            .orderBy("createdDate", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                it.documents.forEach { doc ->
                    val model = doc.toObject(ElonData::class.java)
                    if (model != null && query.isNotEmpty()) {
                        var list: ElonData? = null
                        if (key) {
                            if (model.sarlavha.contains(query)) {
                                list = model
                            }

                        } else {
                            if (model.tavsif.contains(query)) {
                                list = model
                            }
                        }
                        var listBolim: ElonData? = null
                        if (bolim != Constant.DEFAULT_BOLIM) {
                            if (list?.bolim == bolim) {
                                listBolim = list
                            }
                        } else {
                            listBolim = list
                        }
                        var listHudud: ElonData? = null
                        if (hudud != Constant.DEFAULT_ITEM) {
                            if (listBolim?.viloyat == hudud) {
                                listHudud = listBolim
                            }
                        } else {
                            listHudud = listBolim
                        }
                        var listValyuta: ElonData? = null
                        if (valyuta != Constant.DEFAULT_VALYUTA) {
                            if (listHudud?.type == valyuta) {
                                listValyuta = listHudud
                            }
                        } else {
                            listValyuta = listHudud
                        }
                        var listMinMaxSum: ElonData? = null
                        if (listValyuta!=null){
                            if (minSum!=0 && maxSum !=0 ) {
                                if (listValyuta.narxi.toInt() in minSum..maxSum) {
                                    listMinMaxSum = listValyuta
                                }
                            } else {
                                listMinMaxSum = listValyuta
                            }
                        }
                        Log.d("getList", listMinMaxSum.toString())
                        if (listMinMaxSum != null) {
                            queryList.add(listMinMaxSum)
                        }
                    }
                }
                onSuccess.invoke(queryList)

            }.addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}