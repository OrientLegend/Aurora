package com.eternal.aurora

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.eternal.aurora.logic.model.Photo
import com.eternal.aurora.ui.utils.DatabaseUtil
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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