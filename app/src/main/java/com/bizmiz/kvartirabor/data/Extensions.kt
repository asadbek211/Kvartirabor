package com.bizmiz.kvartirabor.data

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.RadioButton
import com.google.android.material.textfield.TextInputEditText

fun View.showSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

fun TextInputEditText.textToString() = this.text.toString()

fun TextInputEditText.checkIsEmpty(): Boolean = text == null ||
        textToString() == "" ||
        textToString().equals("null", ignoreCase = true)

fun TextInputEditText.showError(error: String) {
    this.error = error
    this.showSoftKeyboard()

}

fun fiveCheckRadioButton(
    rdb1: RadioButton,
    rdb2: RadioButton,
    rdb3: RadioButton,
    rdb4: RadioButton,
    rdb5: RadioButton
) {
    rdb1.isChecked = true
    rdb2.isChecked = false
    rdb3.isChecked = false
    rdb4.isChecked = false
    rdb5.isChecked = false
}

fun twoCheckRadioButton(rdb1: RadioButton, rdb2: RadioButton) {
    rdb1.isChecked = true
    rdb2.isChecked = false
}

var vilovatlar: ArrayList<String> = arrayListOf(
    "Andijon viloyati",
    "Buxoro viloyati",
    "Jizzax viloyati",
    "Qoraqalpog'iston",
    "Qashqadaryo viloyati",
    "Navoiy viloyati",
    "Namangan viloyati",
    "Samarqand viloyati",
    "Surxondaryo viloyati",
    "Sirdaryo viloyati",
    "Toshkent viloyati",
    "Farg'ona viloyati",
    "Xorazm viloyati"
)
val listBolim: ArrayList<String> =
    arrayListOf("Barcha e'lonlar", "Ijaraga berish", "Sotish", "Ayirboshlash")
val pulBirlik: Array<String> = arrayOf("So'm", "$")
val pulBirlikSearch: Array<String> = arrayOf("None","So'm", "$")
val mebel: Array<String> = arrayOf("Ha", "Yo'q")
var sharoitlari:ArrayList<String> = arrayListOf()
var qurilishTuri = "G'ishtli"
val bolimlar: Array<String> =
    arrayOf("Bo'lim tanlang", "Ijaraga berish", "Sotish", "Ayirboshlash")
val tamiri: Array<String> = arrayOf(
    "Ta'miri",
    "Mualliflik loyixasi",
    "Evrota'mir",
    "O'rtacha",
    "Ta'mir talab",
    "Qora suvoq",
    "Tozalashdan avvalgi pardoz"
)

