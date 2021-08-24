package com.bizmiz.kvartirabor.ui.auth.smsOtp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bizmiz.kvartirabor.data.Resource
import com.bizmiz.kvartirabor.data.ResourceState
import com.bizmiz.kvartirabor.data.helper.SmsHelper

class SmsViewModel(private val smsHelper: SmsHelper) : ViewModel() {
    private val smsData: MutableLiveData<Resource<String>> = MutableLiveData()
    val sms: LiveData<Resource<String>>
        get() = smsData

    fun smsCredential(phoneNumber:String) {
        smsData.value = Resource.loading()
        smsHelper.sentPhoneNumber(phoneNumber) {
            smsData.value = Resource.error(it)
        }
    }
}