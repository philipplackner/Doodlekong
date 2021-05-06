package com.plcoding.doodlekong.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService

fun Activity.hideKeyboard(root: View) {
    val windowToken = root.windowToken
    val imm = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    windowToken?.let {
        imm.hideSoftInputFromWindow(it, 0)
    } ?: kotlin.run {
        try {
            val keyboardHeight = InputMethodManager::class.java
                .getMethod("getInputMethodWindowVisibleHeight")
                .invoke(imm) as Int
            if(keyboardHeight > 0) {
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch(e: Exception) {
            e.printStackTrace()
        }
    }
}