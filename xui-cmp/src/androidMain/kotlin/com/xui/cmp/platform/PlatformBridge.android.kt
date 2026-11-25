package com.xui.cmp.platform

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.koin.dsl.module

class AndroidPlatformBridge(private val context: Context) : PlatformBridge {
  override val platformName: String = "Android"
  override val platformTag: String = "androidMain"
  override val osDetail: String = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
  override val architectureNote: String =
    "Jetpack Compose Runtime + Koin androidContext() + EdgeToEdge WindowInsets"
  override val isIos: Boolean = false

  override fun triggerHaptic(type: XuiHapticType) {
    try {
      val vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
          val manager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
          manager?.defaultVibrator
        } else {
          @Suppress("DEPRECATION")
          context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        } ?: return

      if (!vibrator.hasVibrator()) return

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val effectId =
          when (type) {
            XuiHapticType.LIGHT_IMPACT -> VibrationEffect.EFFECT_TICK
            XuiHapticType.MEDIUM_IMPACT -> VibrationEffect.EFFECT_CLICK
            XuiHapticType.SELECTION -> VibrationEffect.EFFECT_TICK
            XuiHapticType.SUCCESS -> VibrationEffect.EFFECT_DOUBLE_CLICK
            XuiHapticType.ERROR -> VibrationEffect.EFFECT_HEAVY_CLICK
          }
        vibrator.vibrate(VibrationEffect.createPredefined(effectId))
      } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val duration =
          when (type) {
            XuiHapticType.LIGHT_IMPACT,
            XuiHapticType.SELECTION -> 14L
            XuiHapticType.MEDIUM_IMPACT -> 24L
            XuiHapticType.SUCCESS -> 32L
            XuiHapticType.ERROR -> 45L
          }
        vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
      }
    } catch (_: SecurityException) {
      // Ignore if vibrator permission is unavailable in restricted environments
    } catch (_: Throwable) {
      // Ignore unsupported vibrator hardware errors
    }
  }

  override fun copyToClipboard(label: String, text: String): Boolean {
    return try {
      val clipboard =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager ?: return false
      clipboard.setPrimaryClip(ClipData.newPlainText(label, text))
      true
    } catch (_: Throwable) {
      false
    }
  }
}

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
  BackHandler(enabled = enabled, onBack = onBack)
}

actual fun platformKoinModule(): Module = module {
  single<PlatformBridge> { AndroidPlatformBridge(get()) }
}
