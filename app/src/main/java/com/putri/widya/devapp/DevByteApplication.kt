package com.putri.widya.devapp

import android.app.Application
import timber.log.Timber

class DevByteApplication : Application() {

    // onCreate dipanggil sebelum layar pertama ditampilkan kepada pengguna, utas untuk menghindari
    // keterlambatan memulai aplikasi.
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
