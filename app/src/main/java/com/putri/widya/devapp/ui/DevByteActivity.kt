package com.putri.widya.devapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.putri.widya.devapp.R

// membuat class DevByteActivity untuk mengakses atau menampilkan layout activity_dev_byte_viewer
class DevByteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_byte_viewer)
    }
}
