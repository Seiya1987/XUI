package com.example

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.xui.cmp.di.XuiComponentCatalog
import com.xui.cmp.di.XuiDialogController
import com.xui.cmp.di.XuiSkinManager
import com.xui.cmp.di.XuiTipController
import com.xui.cmp.platform.PlatformBridge
import com.xui.cmp.theme.XuiColorHelper
import com.xui.cmp.theme.XuiSkinPreset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("XUI KMP", appName)
  }

  @Test
  fun `koin container resolves decoupled xui dependencies`() {
    val koin = GlobalContext.get()
    val skinManager = koin.get<XuiSkinManager>()
    val tipController = koin.get<XuiTipController>()
    val dialogController = koin.get<XuiDialogController>()
    val catalog = koin.get<XuiComponentCatalog>()
    val platformBridge = koin.get<PlatformBridge>()

    assertNotNull(tipController)
    assertNotNull(dialogController)
    assertFalse(platformBridge.isIos)
    assertEquals("Android", platformBridge.platformName)
    assertEquals(17, catalog.getAllComponents().size)

    skinManager.selectSkin(XuiSkinPreset.EMERALD)
    assertEquals(XuiSkinPreset.EMERALD, skinManager.state.value.activeSkin)
    assertEquals(1, skinManager.state.value.skinSwitchCount)
  }

  @Test
  fun `xui color helper formats and interpolates accurately`() {
    assertEquals("#00A8E1", XuiColorHelper.toHexString(Color(0xFF00A8E1)))
    val mid = XuiColorHelper.computeColor(Color.Black, Color.White, 0.5f)
    assertEquals(0.5f, mid.red, 0.01f)
  }
}
