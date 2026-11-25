package com.example

import android.app.Application
import com.xui.cmp.di.initXuiKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class XuiApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    initXuiKoin {
      androidLogger(Level.INFO)
      androidContext(this@XuiApplication)
    }
  }
}
