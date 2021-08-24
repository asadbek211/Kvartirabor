package com.bizmiz.kvartirabor.ui.elon.ElonBerish

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Adapters.ImageAdapter
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.ElonBerishHelper
import com.google.android.material.textfield.TextInputEditText

class ElonBerishViewModel(private val elonBerishHelper: ElonBerishHelper) : ViewModel() {
    private val setElonList: MutableLiveData<Resource<String?>> = MutableLiveData()
    val elonList: LiveData<Resource<String?>>
        get() = setElonList

    fun setElonData(
        adapter: ImageAdapter,
        sarlavha: TextInputEditText,
        manzil: TextInputEditText,
        telNomer: TextInputEditText,
        narx: TextInputEditText,
        type: String,
        prefs: SharedPreferences
    ) {
        setElonList.value = Resource.loading()
        elonBerishHelper.setElonData(adapter,sarlavha, manzil, telNomer, narx, type, prefs, {succes->
          setElonList.value = Resource.success(succes)
        }, {failure->
          setElonList.value = Resource.error(failure)
        }, {check->
           setElonList.value = Resource.check(check)
        })
    }

}