package com.xui.cmp.showcase

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.xui.cmp.platform.XuiHapticType
import com.xui.cmp.theme.XuiDisplayHelper
import com.xui.cmp.theme.XuiHighlightedText
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiColorHelper
import com.xui.cmp.tokens.XuiTemplateFormatter
import com.xui.cmp.ui.components.XuiArtworkPlaceholder
import com.xui.cmp.ui.components.XuiButtonStyle
import com.xui.cmp.ui.components.XuiFloatLayout
import com.xui.cmp.ui.components.XuiLinkTextView
import com.xui.cmp.ui.components.XuiProgressBar
import com.xui.cmp.ui.components.XuiRadiusImageView
import com.xui.cmp.ui.components.XuiRadiusShapeMode
import com.xui.cmp.ui.components.XuiRoundButton
import com.xui.cmp.ui.components.XuiTopBar
import kotlin.math.roundToInt

@Composable
fun HelpersLabScreen(
  uiState: HelpersLabUiState,
  viewModel: HelpersLabViewModel,
  modifier: Modifier = Modifier,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val strings = XuiThemeTokens.strings
  val helpers = strings.helpers
  val density = LocalDensity.current

  val blendedColor =
    XuiColorHelper.computeColor(colors.demoBlendStart, colors.demoBlendEnd, uiState.blendFraction)
  val blendedHex = XuiColorHelper.toHexString(blendedColor)
  val contrastOnWhite =
    ((XuiColorHelper.contrastRatio(blendedColor, colors.swatchLightBackground) * 10f).roundToInt() / 10f)
  val isDarkColor = XuiColorHelper.isDarkColor(blendedColor)

  Column(modifier = modifier.fillMaxSize().background(colors.canvasBackground)) {
    XuiTopBar(
      title = helpers.topBarTitle,
      subtitle = helpers.topBarSubtitle,
      centerTitle = false,
    )

    BoxWithConstraints(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
      val containerWidthDp = maxWidth
      val containerWidthPx = XuiDisplayHelper.dpToPxInt(density, containerWidthDp)
      val layoutClass =
        if (XuiDisplayHelper.isExpandedLayout(containerWidthDp, dimens.expandedBreakpoint)) {
          helpers.layoutExpandedLabel
        } else {
          helpers.layoutCompactLabel
        }

      LazyColumn(
        modifier = Modifier.fillMaxSize().widthIn(max = dimens.contentMaxWidth),
        contentPadding = PaddingValues(dimens.horizontalPadding),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
      ) {
        item {
          Surface(
            shape = shapes.card,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
          ) {
            Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
              Text(
                text = helpers.colorCardTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing12))
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.spacing14),
              ) {
                Box(
                  modifier =
                    Modifier.size(dimens.swatchSize)
                      .clip(shapes.card)
                      .background(blendedColor),
                  contentAlignment = Alignment.Center,
                ) {
                  Text(
                    text = if (isDarkColor) helpers.colorDarkLabel else helpers.colorLightLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = if (isDarkColor) colors.swatchLightLabel else colors.swatchDarkLabel,
                  )
                }
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = XuiTemplateFormatter.format(helpers.colorHexTemplate, "hex" to blendedHex),
                    style = MaterialTheme.typography.titleSmall.copy(
                      fontFamily = FontFamily.Monospace,
                      fontWeight = FontWeight.SemiBold,
                    ),
                    color = colors.titlePrimary,
                  )
                  Text(
                    text =
                      XuiTemplateFormatter.format(
                        helpers.colorContrastTemplate,
                        "ratio" to contrastOnWhite,
                        "percent" to (uiState.blendFraction * 100f).roundToInt(),
                      ),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                  )
                }
              }
              Spacer(modifier = Modifier.height(dimens.spacing10))
              Slider(
                value = uiState.blendFraction,
                onValueChange = { viewModel.updateBlendFraction(it) },
                colors =
                  SliderDefaults.colors(
                    thumbColor = colors.brandPrimary,
                    activeTrackColor = colors.brandPrimary,
                  ),
              )
            }
          }
        }

        item {
          Surface(
            shape = shapes.card,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
          ) {
            Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
              Text(
                text = helpers.radiusCardTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing4))
              Text(
                text = helpers.radiusCardSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing14))

              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.spacing18),
              ) {
                XuiRadiusImageView(
                  shapeMode = uiState.imageShapeMode,
                  cornerRadius = dimens.cardLargeRadius,
                  isSelected = uiState.imageSelected,
                  borderColor = colors.separatorHairline,
                  selectedBorderColor = colors.brandPrimary,
                  onClick = { viewModel.toggleImageSelected() },
                  modifier =
                    Modifier.size(
                      width =
                        if (uiState.imageShapeMode == XuiRadiusShapeMode.OVAL) dimens.avatarOvalWidth
                        else dimens.avatarSize,
                      height = dimens.avatarSize,
                    ),
                ) {
                  XuiArtworkPlaceholder(
                    initials = strings.common.brandInitials,
                    primaryColor = colors.brandPrimary,
                    secondaryColor = colors.secondaryAccent,
                    modifier = Modifier.fillMaxSize(),
                  )
                }

                Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing8)) {
                  XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
                    XuiRoundButton(
                      text = helpers.shapeRoundedRect,
                      style =
                        if (uiState.imageShapeMode == XuiRadiusShapeMode.ROUNDED_RECT)
                          XuiButtonStyle.FILLED
                        else XuiButtonStyle.OUTLINED,
                      onClick = { viewModel.setImageShape(XuiRadiusShapeMode.ROUNDED_RECT) },
                    )
                    XuiRoundButton(
                      text = helpers.shapeCircle,
                      style =
                        if (uiState.imageShapeMode == XuiRadiusShapeMode.CIRCLE)
                          XuiButtonStyle.FILLED
                        else XuiButtonStyle.OUTLINED,
                      onClick = { viewModel.setImageShape(XuiRadiusShapeMode.CIRCLE) },
                    )
                    XuiRoundButton(
                      text = helpers.shapeOval,
                      style =
                        if (uiState.imageShapeMode == XuiRadiusShapeMode.OVAL)
                          XuiButtonStyle.FILLED
                        else XuiButtonStyle.OUTLINED,
                      onClick = { viewModel.setImageShape(XuiRadiusShapeMode.OVAL) },
                    )
                  }
                }
              }
            }
          }
        }

        item {
          Surface(
            shape = shapes.card,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
          ) {
            Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
              Text(
                text = helpers.progressCardTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing14))
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.spacing16),
              ) {
                XuiProgressBar(
                  progress = uiState.progressValue,
                  circular = true,
                  size = dimens.progressRingSize,
                )
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(dimens.spacing10)) {
                  XuiProgressBar(
                    progress = uiState.progressValue,
                    circular = false,
                    strokeWidth = dimens.sliderTrackThick,
                  )
                  XuiRoundButton(
                    text =
                      if (uiState.isAutoRunningProgress) helpers.progressRunningBtn
                      else helpers.progressRunBtn,
                    leadingIcon = Icons.Default.PlayArrow,
                    isLoading = uiState.isAutoRunningProgress,
                    onClick = { viewModel.runAnimatedProgress() },
                  )
                }
              }
              Spacer(modifier = Modifier.height(dimens.spacing6))
              Slider(
                value = uiState.progressValue,
                onValueChange = { viewModel.updateProgress(it) },
                colors =
                  SliderDefaults.colors(
                    thumbColor = colors.brandPrimary,
                    activeTrackColor = colors.brandPrimary,
                  ),
              )
            }
          }
        }

        item {
          Surface(
            shape = shapes.card,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
          ) {
            Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
              Text(
                text = helpers.linkCardTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing6))
              Text(
                text = uiState.lastLinkClicked,
                style = MaterialTheme.typography.bodySmall,
                color = colors.brandPrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing10))
              XuiLinkTextView(
                text = helpers.linkSampleText,
                onLinkClick = { viewModel.onLinkTapped(it) },
              )
            }
          }
        }

        item {
          Surface(
            shape = shapes.card,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
          ) {
            Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
              Text(
                text = helpers.spanCardTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing8))
              XuiHighlightedText(
                text = helpers.spanSampleText,
                highlights = helpers.spanHighlights,
              )
              Spacer(modifier = Modifier.height(dimens.spacing8))
              Text(
                text =
                  XuiTemplateFormatter.format(
                    helpers.displayMetricsTemplate,
                    "widthDp" to containerWidthDp.value.roundToInt(),
                    "widthPx" to containerWidthPx,
                    "density" to density.density,
                    "layoutClass" to layoutClass,
                  ),
                style = MaterialTheme.typography.labelMedium.copy(fontFamily = FontFamily.Monospace),
                color = colors.textSecondary,
              )
            }
          }
        }

        item {
          Surface(
            shape = shapes.card,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
          ) {
            Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
              Text(
                text = helpers.hapticCardTitle,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing10))
              XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
                XuiHapticType.entries.forEach { haptic ->
                  XuiRoundButton(
                    text = haptic.name.lowercase().replace("_", " "),
                    style = XuiButtonStyle.TONAL,
                    leadingIcon = Icons.Default.Vibration,
                    onClick = { viewModel.testHaptic(haptic) },
                  )
                }
              }
            }
          }
        }

        item { Spacer(modifier = Modifier.height(dimens.spacing16)) }
      }
    }
  }
}
