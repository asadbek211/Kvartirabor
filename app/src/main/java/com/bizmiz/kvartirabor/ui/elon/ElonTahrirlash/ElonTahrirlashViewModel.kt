package com.bizmiz.kvartirabor.ui.elon.ElonTahrirlash

import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.helper.ElonTahrirlashHelper
import com.google.android.material.textfield.TextInputEditText

class ElonTahrirlashViewModel(private val elonTahrirlashHelper: ElonTahrirlashHelper) :
    ViewModel() {
    private val setList: MutableLiveData<Resource<String?>> = MutableLiveData()
    val elonTahrirList: LiveData<Resource<String?>>
        get() = setList

    fun setElonData(
        id:String?,
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
        longitude: Double,
        txtVil:TextView,
    ) {
        setList.value = Resource.loading()
        elonTahrirlashHelper.setElonData(
            id,
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
            txtVil,
            { succes ->
                setList.value = Resource.success(succes)
            },
            { failure ->
                setList.value = Resource.error(failure)
            },
            { check ->
                setList.value = Resource.check(check)
            })
    }

}