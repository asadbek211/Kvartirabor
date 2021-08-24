package com.bizmiz.kvartirabor.di

import com.bizmiz.kvartirabor.data.helper.ElonBerishHelper
import com.bizmiz.kvartirabor.data.helper.ElonlarHelper
import com.bizmiz.kvartirabor.data.helper.MeningElonlarimHelper
import com.bizmiz.kvartirabor.data.helper.SmsHelper
import com.bizmiz.kvartirabor.ui.auth.smsOtp.SmsViewModel
import com.bizmiz.kvartirabor.ui.elon.ElonBerish.ElonBerishViewModel
import com.bizmiz.kvartirabor.ui.elon.Elonlar.ElonlarViewModel
import com.bizmiz.kvartirabor.ui.elon.MeniElonlarim.MeningElonlarimViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { ElonlarHelper(get()) }
    single { SmsHelper(get(), get()) }
    single { MeningElonlarimHelper(get(), get()) }
    single { ElonBerishHelper(get(), get()) }
}

val viewModelModule = module {
    viewModel { ElonlarViewModel(get()) }
    viewModel { MeningElonlarimViewModel(get()) }
    viewModel { SmsViewModel(get()) }
    viewModel { ElonBerishViewModel(get()) }
}