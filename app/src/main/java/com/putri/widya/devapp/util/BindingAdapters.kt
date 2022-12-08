package com.putri.widya.devapp.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

// untuk menyembunyikan data jika terjadinya error.
@BindingAdapter("isNetworkError", "playlist")
fun hideIfNetworkError(view: View, isNetWorkError: Boolean, playlist: Any?) {
    view.visibility = if (playlist != null) View.GONE else View.VISIBLE

    if(isNetWorkError) {
        view.visibility = View.GONE
    }
}

// Binding adapter digunakan untuk menampilkan gambar dari URL menggunakan Glide.
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    Glide.with(imageView.context).load(url).into(imageView)
}