package com.bizmiz.kvartirabor.ui.elon.Elonlar.barchaElonlar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.BarchaElonlarHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class BarchaElonlarViewModel(private val barchaElonlarHelper: BarchaElonlarHelper) : ViewModel() {
    private val barchaElonlarList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val barchaElon: LiveData<Resource<ArrayList<ElonData>>>
        get() = barchaElonlarList

    fun getBarchaElonlarData() {
        barchaElonlarList.value = Resource.loading()
        barchaElonlarHelper.getBarchaElonlarData({
            barchaElonlarList.value = Resource.success(it)
        }, {
            barchaElonlarList.value = Resource.error(it)
        })
    }
}