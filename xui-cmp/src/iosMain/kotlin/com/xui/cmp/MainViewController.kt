package com.xui.cmp

import androidx.compose.ui.window.ComposeUIViewController
import com.xui.cmp.di.initXuiKoin
import com.xui.cmp.showcase.XuiCmpRootApp
import platform.UIKit.UIViewController

@Suppress("unused")
fun MainViewController(): UIViewController {
  initXuiKoin()
  return ComposeUIViewController {
    XuiCmpRootApp()
  }
}
