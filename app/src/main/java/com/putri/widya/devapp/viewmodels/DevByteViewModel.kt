package com.putri.widya.devapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.putri.widya.devapp.domain.DevByteVideo
import com.putri.widya.devapp.network.DevByteNetwork
import com.putri.widya.devapp.network.asDomainModel
import kotlinx.coroutines.*
import java.io.IOException

class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    // Daftar putar video yang dapat ditampilkan di layar. Tampilan harus menggunakan ini untuk
    // mendapatkan akses ke datanya.
    private val _playlist = MutableLiveData<List<DevByteVideo>>()
    val playlist: LiveData<List<DevByteVideo>>
        get() = _playlist

    // _eventNetworkError disetel untuk kesalahan jaringan. Tampilan harus menggunakan ini untuk
    // mendapatkan akses ke datanya.
    private var _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    // untuk menampilkan pesan kesalahan. Tampilan harus menggunakan ini untuk mendapatkan akses
    // ke datanya.
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    // init{} langsung dipanggil saat ViewModel ini dibuat.
    init {
        refreshDataFromNetwork()
    }

    // Segarkan data dari jaringan dan sebarkan melalui LiveData. Gunakan peluncuran coroutine
    // untuk melakukannya.
    // Tampilkan pesan kesalahan Toast dan sembunyikan bilah progres.
    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
             val playlist = DevByteNetwork.devbytes.getPlaylist()
            _playlist.postValue(playlist.asDomainModel())

            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false

        } catch (networkError: IOException) {
            // Show a Toast error message and hide the progress bar.
            _eventNetworkError.value = true
        }
    }


     // meResets error jaringan.
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    // untuk membuat DevByteViewModel dengan parameter.
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
