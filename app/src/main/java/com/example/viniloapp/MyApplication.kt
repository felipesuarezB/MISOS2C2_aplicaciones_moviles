package com.example.viniloapp

import android.app.Application

class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getAppContext(): Application {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
} 