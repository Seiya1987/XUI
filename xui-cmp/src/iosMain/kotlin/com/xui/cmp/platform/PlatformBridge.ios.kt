package com.xui.cmp.platform

import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.UIKit.UIDevice
import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle
import platform.UIKit.UINotificationFeedbackGenerator
import platform.UIKit.UINotificationFeedbackType
import platform.UIKit.UIPasteboard
import platform.UIKit.UISelectionFeedbackGenerator

class IosPlatformBridge : PlatformBridge {
  override val platformName: String = "iOS"
  override val platformTag: String = "iosMain"
  override val osDetail: String =
    "${UIDevice.currentDevice.systemName} ${UIDevice.currentDevice.systemVersion}"
  override val architectureNote: String =
    "Skiko Metal Renderer + ComposeUIViewController + UIKit Haptics"
  override val isIos: Boolean = true

  override fun triggerHaptic(type: XuiHapticType) {
    when (type) {
      XuiHapticType.LIGHT_IMPACT -> {
        val generator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleLight)
        generator.prepare()
        generator.impactOccurred()
      }
      XuiHapticType.MEDIUM_IMPACT -> {
        val generator = UIImpactFeedbackGenerator(UIImpactFeedbackStyle.UIImpactFeedbackStyleMedium)
        generator.prepare()
        generator.impactOccurred()
      }
      XuiHapticType.SELECTION -> {
        val generator = UISelectionFeedbackGenerator()
        generator.prepare()
        generator.selectionChanged()
      }
      XuiHapticType.SUCCESS -> {
        val generator = UINotificationFeedbackGenerator()
        generator.prepare()
        generator.notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeSuccess)
      }
      XuiHapticType.ERROR -> {
        val generator = UINotificationFeedbackGenerator()
        generator.prepare()
        generator.notificationOccurred(UINotificationFeedbackType.UINotificationFeedbackTypeError)
      }
    }
  }

  override fun copyToClipboard(label: String, text: String): Boolean {
    UIPasteboard.generalPasteboard.string = text
    return true
  }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
  // Handled by UIKit / ComposeUIViewController navigation gesture on iOS
}

actual fun platformKoinModule(): Module = module {
  single<PlatformBridge> { IosPlatformBridge() }
}
