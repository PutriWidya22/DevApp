package com.putri.widya.devapp.domain

import com.putri.widya.devapp.util.smartTruncate

// data class DevByteVideo terdiri dari variabel title, description, url, updated, thumbnail dengan
// tipe data String
data class DevByteVideo(val title: String,
                        val description: String,
                        val url: String,
                        val updated: String,
                        val thumbnail: String) {


    // shortDescription digunakan untuk menampilkan deskripsi UI
    val shortDescription: String
        get() = description.smartTruncate(200)
}