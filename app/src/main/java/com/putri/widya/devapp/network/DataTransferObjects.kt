package com.putri.widya.devapp.network

import com.putri.widya.devapp.domain.DevByteVideo
import com.squareup.moshi.JsonClass

// untuk menampilkan daftar video
@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

// sebagai adapter untuk menampilkan video dengan beberapa variabel.
@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?)


// Mengkonversi hasil Jaringan ke objek basis data.
fun NetworkVideoContainer.asDomainModel(): List<DevByteVideo> {
    return videos.map {
        DevByteVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}
