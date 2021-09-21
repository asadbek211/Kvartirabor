package com.bizmiz.kvartirabor.ui.elon.Elonlar.sotishElonlar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.SotishElonlarHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class SotishElonlarViewModel(private val sotishElonlarHelper: SotishElonlarHelper) : ViewModel() {
    private val sotishList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val sotish: LiveData<Resource<ArrayList<ElonData>>>
        get() = sotishList

    fun getSotishData() {
        sotishList.value = Resource.loading()
        sotishElonlarHelper.getSotishData({
            sotishList.value = Resource.success(it)
        }, {
            sotishList.value = Resource.error(it)
        })
    }
}