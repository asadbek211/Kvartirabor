package com.bizmiz.kvartirabor.ui.favourite

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.SaralanganlarHelper
import com.bizmiz.kvartirabor.data.model.ElonData

class SaralanganlarViewModel(private val saralanganlarHelper: SaralanganlarHelper) :
    ViewModel() {
    private val saralanganList: MutableLiveData<Resource<ArrayList<ElonData>>> = MutableLiveData()
    val saralanganlar: LiveData<Resource<ArrayList<ElonData>>>
        get() = saralanganList

    fun setSaralanganData(
        prefs:SharedPreferences
    ) {
        saralanganList.value = Resource.loading()
        saralanganlarHelper.getSaralanganlarData(prefs,
            { succes ->
                saralanganList.value = Resource.success(succes)
            },
            { failure ->
                saralanganList.value = Resource.error(failure)
            })
    }

}