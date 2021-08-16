package com.bizmiz.kvartirabor.ui.elon.Elonlar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.ElonlarHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class ElonlarViewModel(private val elonlarHelper: ElonlarHelper) : ViewModel() {
    private val elonlarList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val elon: LiveData<Resource<ArrayList<ElonData>>>
        get() = elonlarList

    fun getElonlarData() {
        elonlarList.value = Resource.loading()
        elonlarHelper.getElonlarData({
             elonlarList.value = Resource.success(it)
        },{
          elonlarList.value = Resource.error(it)
        })
    }

}