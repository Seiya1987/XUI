package com.xui.cmp.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.xui.cmp.tokens.XuiDensityPreset
import com.xui.cmp.tokens.XuiDimenTemplate
import com.xui.cmp.tokens.XuiTemplateFormatter
import kotlin.math.roundToInt

/**
 * Pure Kotlin Multiplatform replacement for XUIDisplayHelper / XUIResHelper metrics utilities.
 */
object XuiDisplayHelper {
  private val defaultExpandedBreakpoint: Dp =
    XuiDimenTemplate.buildDimens(XuiDensityPreset.STANDARD, roundedCards = true).expandedBreakpoint

  fun dpToPx(density: Density, dp: Dp): Float = with(density) { dp.toPx() }

  fun dpToPxInt(density: Density, dp: Dp): Int = with(density) { dp.roundToPx() }

  fun pxToDp(density: Density, px: Float): Dp = with(density) { px.toDp() }

  fun isExpandedLayout(availableWidth: Dp, breakpoint: Dp = defaultExpandedBreakpoint): Boolean =
    availableWidth >= breakpoint

  fun isCompactLayout(availableWidth: Dp, breakpoint: Dp = defaultExpandedBreakpoint): Boolean =
    availableWidth < breakpoint

  fun formatRatioPercent(fraction: Float, template: String = "{value}%"): String =
    XuiTemplateFormatter.format(template, "value" to (fraction.coerceIn(0f, 1f) * 100f).roundToInt())
}

/**
 * Pure Compose Multiplatform replacement for XUISpan / XUIHighlightTextSpan.
 */
object XuiSpanHelper {
  fun buildHighlightedText(
    source: String,
    highlights: List<String>,
    highlightColor: Color,
    highlightBackground: Color = highlightColor.copy(alpha = 0.14f),
  ): AnnotatedString {
    if (highlights.isEmpty()) return AnnotatedString(source)
    val style =
      SpanStyle(
        color = highlightColor,
        background = highlightBackground,
        fontWeight = FontWeight.SemiBold,
      )
    return buildAnnotatedString {
      append(source)
      highlights.filter { it.isNotEmpty() }.forEach { token ->
        var startIndex = source.indexOf(token, ignoreCase = true)
        while (startIndex >= 0) {
          addStyle(style, startIndex, startIndex + token.length)
          startIndex = source.indexOf(token, startIndex = startIndex + token.length, ignoreCase = true)
        }
      }
    }
  }
}

@Composable
fun XuiHighlightedText(
  text: String,
  highlights: List<String>,
  modifier: Modifier = Modifier,
  highlightColor: Color = XuiThemeTokens.colors.brandPrimary,
  textColor: Color = XuiThemeTokens.colors.titlePrimary,
) {
  val textSizes = XuiThemeTokens.textSizes
  Text(
    text = XuiSpanHelper.buildHighlightedText(text, highlights, highlightColor),
    style = MaterialTheme.typography.bodyMedium.copy(
      color = textColor,
      fontSize = textSizes.bodyMd,
      lineHeight = textSizes.lineHeightBody,
    ),
    modifier = modifier,
  )
}
