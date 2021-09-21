package com.bizmiz.kvartirabor.di

import com.bizmiz.kvartirabor.data.helper.*
import com.bizmiz.kvartirabor.ui.auth.smsOtp.SmsViewModel
import com.bizmiz.kvartirabor.ui.elon.ElonBerish.ElonBerishViewModel
import com.bizmiz.kvartirabor.ui.elon.ElonTahrirlash.ElonTahrirlashViewModel
import com.bizmiz.kvartirabor.ui.elon.Elonlar.ElonlarViewModel
import com.bizmiz.kvartirabor.ui.elon.Elonlar.ayrboshlashElonlar.AyrboshlashElonlarViewModel
import com.bizmiz.kvartirabor.ui.elon.Elonlar.barchaElonlar.BarchaElonlarViewModel
import com.bizmiz.kvartirabor.ui.elon.Elonlar.ijaraElonlar.IjaraElonlarViewModel
import com.bizmiz.kvartirabor.ui.elon.Elonlar.sotishElonlar.SotishElonlarViewModel
import com.bizmiz.kvartirabor.ui.elon.MeniElonlarim.MeningElonlarimViewModel
import com.bizmiz.kvartirabor.ui.favourite.SaralanganlarViewModel
import com.bizmiz.kvartirabor.ui.search.SearchViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { FirebaseStorage.getInstance() }
    single { FirebaseStorage.getInstance().reference }
    single { ElonlarHelper(get()) }
    single { SmsHelper(get(), get()) }
    single { MeningElonlarimHelper(get(), get(),get()) }
    single { ElonBerishHelper(get(), get()) }
    single { ElonTahrirlashHelper(get()) }
    single { SaralanganlarHelper(get()) }
    single { BarchaElonlarHelper(get()) }
    single { IjaraElonlarHelper(get()) }
    single { SotishElonlarHelper(get()) }
    single { AyrboshlashElonlarHelper(get()) }
    single { SearchHelper(get()) }
}

val viewModelModule = module {
    viewModel { ElonlarViewModel(get()) }
    viewModel { MeningElonlarimViewModel(get()) }
    viewModel { SmsViewModel(get()) }
    viewModel { ElonBerishViewModel(get()) }
    viewModel { ElonTahrirlashViewModel(get()) }
    viewModel { SaralanganlarViewModel(get()) }
    viewModel { BarchaElonlarViewModel(get()) }
    viewModel { IjaraElonlarViewModel(get()) }
    viewModel { SotishElonlarViewModel(get()) }
    viewModel { AyrboshlashElonlarViewModel(get()) }
    viewModel { SearchViewModel(get()) }
}