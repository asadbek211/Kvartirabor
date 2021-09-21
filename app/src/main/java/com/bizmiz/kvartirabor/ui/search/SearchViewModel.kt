package com.bizmiz.kvartirabor.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.SearchHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class SearchViewModel(private val searchHelper: SearchHelper) : ViewModel() {
    private val searchList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val search: LiveData<Resource<ArrayList<ElonData>>>
        get() = searchList

    fun getSearchData(
        key: Boolean,
        query: String,
        bolim: String,
        hudud: String,
        valyuta: String,
        minSum: Int,
        maxSum: Int
    ) {
        searchList.value = Resource.loading()
        searchHelper.getSearchData(key,query,bolim, hudud, valyuta, minSum, maxSum, {

            searchList.value = Resource.success(it)
        }, {
            searchList.value = Resource.error(it)
        })
    }
}