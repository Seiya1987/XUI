package com.xui.cmp.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import com.xui.cmp.tokens.XuiColors
import com.xui.cmp.tokens.XuiDensityPreset
import com.xui.cmp.tokens.XuiDimenTemplate
import com.xui.cmp.tokens.XuiDimens
import com.xui.cmp.tokens.XuiMotionTokens
import com.xui.cmp.tokens.XuiPaletteTemplate
import com.xui.cmp.tokens.XuiShapeTokens
import com.xui.cmp.tokens.XuiSkinPreset
import com.xui.cmp.tokens.XuiStringResolver
import com.xui.cmp.tokens.XuiStringTemplateDefaults
import com.xui.cmp.tokens.XuiStrings
import com.xui.cmp.tokens.XuiTextSizes
import com.xui.cmp.tokens.XuiThemeMode

typealias XuiThemeMode = com.xui.cmp.tokens.XuiThemeMode
typealias XuiDensityPreset = com.xui.cmp.tokens.XuiDensityPreset
typealias XuiSkinPreset = com.xui.cmp.tokens.XuiSkinPreset
typealias XuiColors = com.xui.cmp.tokens.XuiColors
typealias XuiDimens = com.xui.cmp.tokens.XuiDimens
typealias XuiColorHelper = com.xui.cmp.tokens.XuiColorHelper

private val defaultColors = XuiPaletteTemplate.buildColors(XuiSkinPreset.AZURE, isDark = false)
private val defaultDimens = XuiDimenTemplate.buildDimens(XuiDensityPreset.STANDARD, roundedCards = true)
private val defaultShapes = XuiDimenTemplate.buildShapes(defaultDimens)
private val defaultTextSizes = XuiDimenTemplate.buildTextSizes(XuiDensityPreset.STANDARD)
private val defaultMotion = XuiMotionTokens()
private val defaultStrings by lazy {
  XuiStringResolver.resolve(
    brandToken = XuiStringTemplateDefaults.DEFAULT_BRAND_TOKEN,
    rules = XuiStringTemplateDefaults.defaultRules(),
  )
}

val LocalXuiColors = staticCompositionLocalOf { defaultColors }
val LocalXuiDimens = staticCompositionLocalOf { defaultDimens }
val LocalXuiShapes = staticCompositionLocalOf { defaultShapes }
val LocalXuiTextSizes = staticCompositionLocalOf { defaultTextSizes }
val LocalXuiMotion = staticCompositionLocalOf { defaultMotion }
val LocalXuiStrings = staticCompositionLocalOf { defaultStrings }
val LocalXuiSkinPreset = staticCompositionLocalOf { XuiSkinPreset.AZURE }
val LocalXuiDensityPreset = staticCompositionLocalOf { XuiDensityPreset.STANDARD }

fun xuiLightColors(skin: XuiSkinPreset): XuiColors =
  XuiPaletteTemplate.buildColors(skin = skin, isDark = false)

fun xuiDarkColors(skin: XuiSkinPreset): XuiColors =
  XuiPaletteTemplate.buildColors(skin = skin, isDark = true)

object XuiThemeTokens {
  val colors: XuiColors
    @Composable get() = LocalXuiColors.current

  val dimens: XuiDimens
    @Composable get() = LocalXuiDimens.current

  val shapes: XuiShapeTokens
    @Composable get() = LocalXuiShapes.current

  val textSizes: XuiTextSizes
    @Composable get() = LocalXuiTextSizes.current

  val motion: XuiMotionTokens
    @Composable get() = LocalXuiMotion.current

  val strings: XuiStrings
    @Composable get() = LocalXuiStrings.current

  val skin: XuiSkinPreset
    @Composable get() = LocalXuiSkinPreset.current

  val density: XuiDensityPreset
    @Composable get() = LocalXuiDensityPreset.current
}

@Composable
fun XuiTheme(
  skinPreset: XuiSkinPreset = XuiSkinPreset.AZURE,
  themeMode: XuiThemeMode = XuiThemeMode.SYSTEM,
  densityPreset: XuiDensityPreset = XuiDensityPreset.STANDARD,
  roundedCards: Boolean = true,
  strings: XuiStrings = LocalXuiStrings.current,
  motionTokens: XuiMotionTokens = defaultMotion,
  typography: Typography = MaterialTheme.typography,
  content: @Composable () -> Unit,
) {
  val systemDark = isSystemInDarkTheme()
  val isDark =
    when (themeMode) {
      XuiThemeMode.SYSTEM -> systemDark
      XuiThemeMode.LIGHT -> false
      XuiThemeMode.DARK -> true
    }

  val xuiColors = remember(skinPreset, isDark) {
    XuiPaletteTemplate.buildColors(skin = skinPreset, isDark = isDark)
  }
  val dimens = remember(densityPreset, roundedCards) {
    XuiDimenTemplate.buildDimens(density = densityPreset, roundedCards = roundedCards)
  }
  val shapes = remember(dimens) { XuiDimenTemplate.buildShapes(dimens) }
  val textSizes = remember(densityPreset) { XuiDimenTemplate.buildTextSizes(densityPreset) }

  val m3ColorScheme =
    if (isDark) {
      darkColorScheme(
        primary = xuiColors.brandPrimary,
        onPrimary = xuiColors.onBrandPrimary,
        primaryContainer = xuiColors.brandContainer,
        onPrimaryContainer = xuiColors.titlePrimary,
        secondary = xuiColors.secondaryAccent,
        background = xuiColors.canvasBackground,
        onBackground = xuiColors.titlePrimary,
        surface = xuiColors.cardSurface,
        onSurface = xuiColors.titlePrimary,
        surfaceVariant = xuiColors.elevatedSurface,
        onSurfaceVariant = xuiColors.textSecondary,
        outline = xuiColors.separatorHairline,
        error = xuiColors.error,
      )
    } else {
      lightColorScheme(
        primary = xuiColors.brandPrimary,
        onPrimary = xuiColors.onBrandPrimary,
        primaryContainer = xuiColors.brandContainer,
        onPrimaryContainer = xuiColors.titlePrimary,
        secondary = xuiColors.secondaryAccent,
        background = xuiColors.canvasBackground,
        onBackground = xuiColors.titlePrimary,
        surface = xuiColors.cardSurface,
        onSurface = xuiColors.titlePrimary,
        surfaceVariant = xuiColors.elevatedSurface,
        onSurfaceVariant = xuiColors.textSecondary,
        outline = xuiColors.separatorHairline,
        error = xuiColors.error,
      )
    }

  CompositionLocalProvider(
    LocalXuiColors provides xuiColors,
    LocalXuiDimens provides dimens,
    LocalXuiShapes provides shapes,
    LocalXuiTextSizes provides textSizes,
    LocalXuiMotion provides motionTokens,
    LocalXuiStrings provides strings,
    LocalXuiSkinPreset provides skinPreset,
    LocalXuiDensityPreset provides densityPreset,
  ) {
    MaterialTheme(
      colorScheme = m3ColorScheme,
      typography = typography,
      content = content,
    )
  }
}
