package com.eternal.aurora

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        MMKV.initialize(this)
        mmkv = MMKV.defaultMMKV()!!
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var mmkv: MMKV
    }


}