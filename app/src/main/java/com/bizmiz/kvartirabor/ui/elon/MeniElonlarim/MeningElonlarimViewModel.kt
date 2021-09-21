package com.bizmiz.kvartirabor.ui.elon.MeniElonlarim

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.MeningElonlarimHelper
import com.bizmiz.kvartirabor.data.model.ElonlarimData

class MeningElonlarimViewModel(private val elonlarimHelper: MeningElonlarimHelper) : ViewModel() {
    private val elonlarimList: MutableLiveData<Resource<ArrayList<ElonlarimData>>> =
        MutableLiveData()
    private val deleteItemList: MutableLiveData<Resource<String>> =
        MutableLiveData()
    val elonlarim: LiveData<Resource<ArrayList<ElonlarimData>>>
        get() = elonlarimList
    val deleteItem: LiveData<Resource<String>>
        get() = deleteItemList

    fun getElonlarimData() {
        elonlarimList.value = Resource.loading()
        elonlarimHelper.getElonlarimData({
            elonlarimList.value = Resource.success(it)
        },
            {
                elonlarimList.value = Resource.error(it)
            }, {
                elonlarimList.value = Resource.check(it)
            })
    }

    fun deleteItem(data: ElonlarimData) {
        deleteItemList.value = Resource.loading()
        elonlarimHelper.deleteItem(data, {
            deleteItemList.value = Resource.success(it)
        }, {
            deleteItemList.value = Resource.error(it)
        })
    }
}