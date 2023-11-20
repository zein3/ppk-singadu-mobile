package com.polstat.singadu

import android.app.Application
import com.polstat.singadu.data.AppContainer
import com.polstat.singadu.data.DefaultAppContainer

class SingaduApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}