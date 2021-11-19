package com.tanawatnunnak.coinsapplication.extention

import android.os.Build
import android.text.Html
import android.text.Spanned

fun String.htmlToString(): Spanned {
    val description = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(this)
    }
    return description
}

fun String.isFirstUpperCase(): Boolean {
    var firstUpperCase = false
    if (this.isNotEmpty()) {
        if (this[0].isUpperCase()) {
            firstUpperCase = true
        }
    }
    return firstUpperCase
}

/*fun String.isFirstLowCase() {
    if (length > 0) {

    }
}*/

fun String.isAllUpperCase(): Boolean {
    var allUpperCase = false
    if (this.isNotEmpty()) {
        val charList = this.toCharArray()
        run loop@{
            charList.forEach {
                if (it.isUpperCase()) {
                    allUpperCase = true
                    return@loop
                }
            }
        }
    }
    return allUpperCase
}