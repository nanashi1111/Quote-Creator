package com.klmobile.quotescreatorv2.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.klmobile.quotescreatorv2.R

fun Activity.copy(text: String) {
  val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
  val clipData = ClipData.newPlainText("text", text)
  clipboardManager.setPrimaryClip(clipData)
  Toast.makeText(this, getString(R.string.copied), Toast.LENGTH_SHORT).show()
}

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, resId, duration).show()
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
  Toast.makeText(this, message, duration).show()
}
