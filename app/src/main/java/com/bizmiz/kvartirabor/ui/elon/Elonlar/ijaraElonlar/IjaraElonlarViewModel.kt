package com.bizmiz.kvartirabor.ui.elon.Elonlar.ijaraElonlar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.IjaraElonlarHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class IjaraElonlarViewModel(private val ijaraElonlarHelper: IjaraElonlarHelper) : ViewModel() {
    private val ijaraList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val ijara: LiveData<Resource<ArrayList<ElonData>>>
        get() = ijaraList

    fun getIjaraData() {
        ijaraList.value = Resource.loading()
        ijaraElonlarHelper.getIjaraData({
            ijaraList.value = Resource.success(it)
        },{
            ijaraList.value = Resource.error(it)
        })
    }
}