package com.xui.cmp.platform

import androidx.compose.runtime.Composable
import org.koin.core.module.Module

enum class XuiHapticType {
  LIGHT_IMPACT,
  MEDIUM_IMPACT,
  SELECTION,
  SUCCESS,
  ERROR,
}

interface PlatformBridge {
  val platformName: String
  val platformTag: String
  val osDetail: String
  val architectureNote: String
  val isIos: Boolean

  fun triggerHaptic(type: XuiHapticType)

  fun copyToClipboard(label: String, text: String): Boolean
}

@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)

expect fun platformKoinModule(): Module
