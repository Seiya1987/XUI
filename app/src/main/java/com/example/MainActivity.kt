package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ui.theme.Typography
import com.xui.cmp.di.initXuiKoin
import com.xui.cmp.showcase.XuiCmpRootApp
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initXuiKoin {
      androidContext(applicationContext)
    }
    enableEdgeToEdge()
    setContent {
      XuiCmpRootApp(typography = Typography)
    }
  }
}
