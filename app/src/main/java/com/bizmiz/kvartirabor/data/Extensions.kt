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
fun threeCheckRadioButton(rdb1:RadioButton,rdb2:RadioButton,rdb3:RadioButton){
    rdb1.isChecked = true
    rdb2.isChecked = false
    rdb3.isChecked = false

}
fun twoCheckRadioButton(rdb1:RadioButton,rdb2:RadioButton){
    rdb1.isChecked = true
    rdb2.isChecked = false

}
fun radioButtonOnClick(rdb:RadioButton){
    rdb.isChecked = !(rdb.isChecked)

}
