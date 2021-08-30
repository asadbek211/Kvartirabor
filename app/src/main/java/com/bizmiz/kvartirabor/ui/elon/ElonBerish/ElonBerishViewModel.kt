package com.bizmiz.kvartirabor.ui.elon.ElonBerish

import android.widget.Spinner
import android.widget.TextView
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
        bolim: Spinner,
        uyQavatliligi: TextInputEditText,
        umumiyMaydon: TextInputEditText,
        oshxonaMaydoni: TextInputEditText,
        uyTamiri: Spinner,
        yashashMaydoni: TextInputEditText,
        narxi: TextInputEditText,
        yashashQavati: TextInputEditText,
        xonaSoni: TextInputEditText,
        tavsif: TextInputEditText,
        joyNomi: TextView,
        telRaqam: TextInputEditText,
        type: Spinner,
        mebel: Spinner,
        kelishuv: Spinner,
        sharoitlari: ArrayList<String>,
        qurilishTuri: String,
        latitude: Double,
        longitude: Double
    ) {
        setElonList.value = Resource.loading()
        elonBerishHelper.setElonData(
            adapter,
            sarlavha,
            bolim,
            uyQavatliligi,
            umumiyMaydon,
            oshxonaMaydoni,
            uyTamiri,
            yashashMaydoni,
            narxi,
            yashashQavati,
            xonaSoni,
            tavsif,
            joyNomi,
            telRaqam,
            type,
            mebel,
            kelishuv,
            sharoitlari,
            qurilishTuri,
            latitude,
            longitude,
            { succes ->
                setElonList.value = Resource.success(succes)
            },
            { failure ->
                setElonList.value = Resource.error(failure)
            },
            { check ->
                setElonList.value = Resource.check(check)
            })
    }

}