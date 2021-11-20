package com.tanawatnunnak.coinsapplication.extention

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest


fun View.setVisibility(isVisible: Boolean) {
    when (isVisible) {
        true -> visible()
        else -> gone()
    }
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun Fragment.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadSvg(url: String) {

    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(500)
        .data(url)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}


