package com.example.myapplication;

import android.app.Application
import android.content.Context

public class MainApplication : Application() {

    init {
        instance = this
    }

    /* Singleton */
    companion object {
        private var instance: MainApplication? = null

        fun getApplicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}
