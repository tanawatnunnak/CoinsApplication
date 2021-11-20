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

fun String.isSvg(): Boolean{
    return this.contains(".svg")
}
