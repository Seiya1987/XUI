package com.xui.cmp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.xui.cmp.theme.XuiThemeTokens

enum class XuiButtonStyle {
  FILLED,
  OUTLINED,
  TONAL,
  DESTRUCTIVE,
}

@Composable
fun XuiRoundButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  style: XuiButtonStyle = XuiButtonStyle.FILLED,
  cornerRadius: Dp = XuiThemeTokens.dimens.buttonCornerRadius,
  isRadiusAdjustBounds: Boolean = false,
  borderWidth: Dp = XuiThemeTokens.dimens.borderThin,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  leadingIcon: ImageVector? = null,
  contentPadding: PaddingValues =
    PaddingValues(
      horizontal = XuiThemeTokens.dimens.spacing20,
      vertical = XuiThemeTokens.dimens.spacing12,
    ),
  customContainerColor: Color? = null,
  customContentColor: Color? = null,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()

  val baseContainer =
    customContainerColor
      ?: when (style) {
        XuiButtonStyle.FILLED -> colors.brandPrimary
        XuiButtonStyle.OUTLINED -> colors.transparent
        XuiButtonStyle.TONAL -> colors.brandContainer
        XuiButtonStyle.DESTRUCTIVE -> colors.error
      }

  val pressedContainer =
    when (style) {
      XuiButtonStyle.FILLED -> colors.brandPrimaryPressed
      XuiButtonStyle.OUTLINED -> colors.brandPrimary.copy(alpha = 0.08f)
      XuiButtonStyle.TONAL -> colors.brandPrimary.copy(alpha = 0.2f)
      XuiButtonStyle.DESTRUCTIVE -> colors.error.copy(alpha = 0.84f)
    }

  val contentColor =
    customContentColor
      ?: when (style) {
        XuiButtonStyle.FILLED -> colors.onBrandPrimary
        XuiButtonStyle.OUTLINED -> colors.brandPrimary
        XuiButtonStyle.TONAL -> colors.brandPrimary
        XuiButtonStyle.DESTRUCTIVE -> colors.onDestructive
      }

  val animatedBg by
    animateColorAsState(
      targetValue = if (isPressed && enabled) pressedContainer else baseContainer,
      label = "xui_round_btn_bg",
    )
  val scale by
    animateFloatAsState(
      targetValue = if (isPressed && enabled) motion.pressedScale else 1f,
      animationSpec = spring(),
      label = "xui_round_btn_scale",
    )

  val shape: Shape = if (isRadiusAdjustBounds) CircleShape else RoundedCornerShape(cornerRadius)
  val border =
    if (style == XuiButtonStyle.OUTLINED) {
      BorderStroke(
        width = borderWidth,
        color = if (enabled) contentColor else contentColor.copy(alpha = motion.disabledBorderAlpha),
      )
    } else null

  Surface(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .defaultMinSize(minHeight = dimens.buttonMinHeight)
        .graphicsLayer {
          scaleX = scale
          scaleY = scale
        }
        .clip(shape)
        .clickable(
          enabled = enabled && !isLoading,
          interactionSource = interactionSource,
          indication = ripple(bounded = true),
          onClick = onClick,
        )
        .alpha(if (enabled) 1f else motion.disabledAlpha)
        .testTag("xui_round_button"),
    shape = shape,
    color = animatedBg,
    contentColor = contentColor,
    border = border,
  ) {
    Row(
      modifier = Modifier.padding(contentPadding),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center,
    ) {
      if (isLoading) {
        CircularProgressIndicator(
          modifier = Modifier.size(dimens.progressButtonSpinnerSize),
          strokeWidth = dimens.progressStrokeSm,
          color = contentColor,
        )
        Spacer(modifier = Modifier.width(dimens.spacing8))
      } else if (leadingIcon != null) {
        Icon(
          imageVector = leadingIcon,
          contentDescription = null,
          tint = contentColor,
          modifier = Modifier.size(dimens.iconButtonLead),
        )
        Spacer(modifier = Modifier.width(dimens.spacing6))
      }
      Text(
        text = text,
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = textSizes.bodyLg,
        ),
        color = contentColor,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
  }
}

@Composable
fun XuiAlphaButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  pressedAlpha: Float = XuiThemeTokens.motion.pressedAlpha,
  disabledAlpha: Float = XuiThemeTokens.motion.disabledButtonAlpha,
  content: @Composable BoxScope.() -> Unit,
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val targetAlpha =
    when {
      !enabled -> disabledAlpha
      isPressed -> pressedAlpha
      else -> 1f
    }
  val animatedAlpha by animateFloatAsState(targetValue = targetAlpha, label = "xui_alpha_btn")

  Box(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .alpha(animatedAlpha)
        .clickable(
          enabled = enabled,
          interactionSource = interactionSource,
          indication = null,
          onClick = onClick,
        )
        .testTag("xui_alpha_button"),
    contentAlignment = Alignment.Center,
    content = content,
  )
}

enum class XuiRadiusShapeMode {
  ROUNDED_RECT,
  CIRCLE,
  OVAL,
}

@Composable
fun XuiRadiusImageView(
  modifier: Modifier = Modifier,
  shapeMode: XuiRadiusShapeMode = XuiRadiusShapeMode.ROUNDED_RECT,
  cornerRadius: Dp = XuiThemeTokens.dimens.dialogRadius,
  borderWidth: Dp = XuiThemeTokens.dimens.progressStrokeSm,
  borderColor: Color = XuiThemeTokens.colors.brandPrimary,
  isSelected: Boolean = false,
  selectedBorderColor: Color = XuiThemeTokens.colors.brandPrimary,
  selectedBorderWidth: Dp = XuiThemeTokens.dimens.borderThick,
  onClick: (() -> Unit)? = null,
  content: @Composable BoxScope.() -> Unit,
) {
  val dimens = XuiThemeTokens.dimens
  val ovalShape = remember {
    GenericShape { size, _ ->
      addOval(Rect(Offset.Zero, size))
    }
  }
  val shape =
    when (shapeMode) {
      XuiRadiusShapeMode.ROUNDED_RECT -> RoundedCornerShape(cornerRadius)
      XuiRadiusShapeMode.CIRCLE -> CircleShape
      XuiRadiusShapeMode.OVAL -> ovalShape
    }
  val activeBorderColor = if (isSelected) selectedBorderColor else borderColor
  val activeBorderWidth = if (isSelected) selectedBorderWidth else borderWidth

  Box(
    modifier =
      modifier
        .clip(shape)
        .then(
          if (activeBorderWidth > dimens.zero) {
            Modifier.border(activeBorderWidth, activeBorderColor, shape)
          } else Modifier
        )
        .then(
          if (onClick != null) {
            Modifier.clickable(onClick = onClick)
          } else Modifier
        )
        .testTag("xui_radius_image_view"),
    contentAlignment = Alignment.Center,
    content = content,
  )
}

@Composable
fun XuiArtworkPlaceholder(
  initials: String,
  modifier: Modifier = Modifier,
  primaryColor: Color = XuiThemeTokens.colors.brandPrimary,
  secondaryColor: Color = XuiThemeTokens.colors.secondaryAccent,
) {
  val colors = XuiThemeTokens.colors
  val textSizes = XuiThemeTokens.textSizes
  Box(
    modifier =
      modifier.background(
        Brush.linearGradient(listOf(primaryColor, secondaryColor))
      ),
    contentAlignment = Alignment.Center,
  ) {
    Canvas(modifier = Modifier.matchParentSize()) {
      drawCircle(
        color = colors.heroOverlayStrong,
        radius = size.minDimension * 0.42f,
        center = Offset(size.width * 0.82f, size.height * 0.22f),
      )
      drawCircle(
        color = colors.heroOverlaySoft,
        radius = size.minDimension * 0.30f,
        center = Offset(size.width * 0.16f, size.height * 0.84f),
      )
    }
    Text(
      text = initials,
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Bold,
        letterSpacing = textSizes.letterSpacingMonogram,
      ),
      color = colors.heroText,
    )
  }
}
