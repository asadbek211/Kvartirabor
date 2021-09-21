package com.bizmiz.kvartirabor.ui.elon.Elonlar.ayrboshlashElonlar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.AyrboshlashElonlarHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class AyrboshlashElonlarViewModel(private val ayrboshlashElonlarHelper: AyrboshlashElonlarHelper) :
    ViewModel() {
    private val ayriboshlashList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val ayriboshlash: LiveData<Resource<ArrayList<ElonData>>>
        get() = ayriboshlashList

    fun getAyirboshlashData() {
        ayriboshlashList.value = Resource.loading()
        ayrboshlashElonlarHelper.getAyirboshlashData({
            ayriboshlashList.value = Resource.success(it)
        }, {
            ayriboshlashList.value = Resource.error(it)
        })
    }
}