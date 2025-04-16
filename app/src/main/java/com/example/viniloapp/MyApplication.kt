package com.example.viniloapp

import android.app.Application
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getAppContext(): Application {
            return instance
        }
    }

    override fun onCreate() {
        instance = this
    }
}
