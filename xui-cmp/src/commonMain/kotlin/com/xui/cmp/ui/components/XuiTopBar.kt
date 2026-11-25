package com.xui.cmp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiColorHelper

@Composable
fun XuiTopBar(
  title: String,
  modifier: Modifier = Modifier,
  subtitle: String? = null,
  centerTitle: Boolean = true,
  backgroundColor: Color = XuiThemeTokens.colors.topBarBackground,
  contentColor: Color = XuiThemeTokens.colors.topBarContent,
  showBottomSeparator: Boolean = true,
  applyStatusBarPadding: Boolean = true,
  onBackClick: (() -> Unit)? = null,
  leftActions: @Composable RowScope.() -> Unit = {},
  rightActions: @Composable RowScope.() -> Unit = {},
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = backgroundColor,
    contentColor = contentColor,
  ) {
    Column(
      modifier =
        Modifier.fillMaxWidth().then(
          if (applyStatusBarPadding) Modifier.windowInsetsPadding(WindowInsets.statusBars)
          else Modifier
        )
    ) {
      Box(
        modifier = Modifier.fillMaxWidth().height(dimens.topBarHeight).padding(horizontal = dimens.spacing4),
      ) {
        Row(
          modifier = Modifier.align(Alignment.CenterStart),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          if (onBackClick != null) {
            IconButton(
              onClick = onBackClick,
              modifier = Modifier.testTag("xui_topbar_back_button"),
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = strings.common.backContentDescription,
                tint = contentColor,
              )
            }
          }
          leftActions()
        }

        Column(
          modifier =
            if (centerTitle) {
              Modifier.align(Alignment.Center).padding(horizontal = dimens.topBarTitleCenterPadding)
            } else {
              Modifier.align(Alignment.CenterStart)
                .padding(
                  start =
                    if (onBackClick != null) dimens.topBarTitleLeftStartWithBack
                    else dimens.topBarTitleLeftStartNoBack,
                  end = dimens.topBarTitleLeftEndPadding,
                )
            },
          horizontalAlignment = if (centerTitle) Alignment.CenterHorizontally else Alignment.Start,
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = textSizes.titleMd,
            ),
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          if (!subtitle.isNullOrBlank()) {
            Text(
              text = subtitle,
              style = MaterialTheme.typography.labelSmall.copy(fontSize = textSizes.labelSm),
              color = contentColor.copy(alpha = 0.68f),
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }

        Row(
          modifier = Modifier.align(Alignment.CenterEnd),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(dimens.spacing2),
          content = rightActions,
        )
      }

      if (showBottomSeparator) {
        HorizontalDivider(thickness = dimens.hairline, color = colors.separatorHairline)
      }
    }
  }
}

@Composable
fun XuiTopBarIconAction(
  icon: ImageVector,
  contentDescription: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  tint: Color = XuiThemeTokens.colors.topBarContent,
) {
  val dimens = XuiThemeTokens.dimens
  IconButton(
    onClick = onClick,
    modifier = modifier.testTag("xui_topbar_icon_action"),
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = tint,
      modifier = Modifier.size(dimens.iconAction),
    )
  }
}

@Composable
fun XuiTopBarTextAction(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  color: Color = XuiThemeTokens.colors.brandPrimary,
  enabled: Boolean = true,
) {
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val interactionSource = remember { MutableInteractionSource() }

  Box(
    modifier =
      modifier
        .minimumInteractiveComponentSize()
        .clip(shapes.badge)
        .clickable(
          enabled = enabled,
          interactionSource = interactionSource,
          indication = ripple(bounded = true),
          onClick = onClick,
        )
        .padding(horizontal = dimens.spacing12, vertical = dimens.spacing8)
        .testTag("xui_topbar_text_action"),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = textSizes.bodyLg,
      ),
      color = if (enabled) color else color.copy(alpha = motion.disabledButtonAlpha),
      maxLines = 1,
    )
  }
}

@Composable
fun XuiCollapsingTopBarLayout(
  title: String,
  modifier: Modifier = Modifier,
  expandedSubtitle: String? = null,
  expandedHeight: Dp = XuiThemeTokens.dimens.collapsingExpandedHeight,
  collapsedHeight: Dp = XuiThemeTokens.dimens.collapsingCollapsedHeight,
  applyStatusBarPadding: Boolean = true,
  onBackClick: (() -> Unit)? = null,
  rightActions: @Composable RowScope.() -> Unit = {},
  headerBackground: @Composable BoxScope.(collapseFraction: Float) -> Unit = {},
  content: @Composable (PaddingValues) -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings
  val density = LocalDensity.current
  val statusBarTopPx =
    if (applyStatusBarPadding) WindowInsets.statusBars.getTop(density).toFloat() else 0f
  val minHeightPx = with(density) { collapsedHeight.toPx() } + statusBarTopPx
  val maxHeightPx = with(density) { expandedHeight.toPx() } + statusBarTopPx
  val maxScrollRange = (maxHeightPx - minHeightPx).coerceAtLeast(1f)

  var scrollOffsetPx by remember { mutableFloatStateOf(0f) }

  val nestedScrollConnection = remember(maxScrollRange) {
    object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y
        if (delta < 0f && scrollOffsetPx > -maxScrollRange) {
          val newOffset = (scrollOffsetPx + delta).coerceIn(-maxScrollRange, 0f)
          val consumed = newOffset - scrollOffsetPx
          scrollOffsetPx = newOffset
          return Offset(0f, consumed)
        }
        return Offset.Zero
      }

      override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
      ): Offset {
        val delta = available.y
        if (delta > 0f && scrollOffsetPx < 0f) {
          val newOffset = (scrollOffsetPx + delta).coerceIn(-maxScrollRange, 0f)
          val consumedY = newOffset - scrollOffsetPx
          scrollOffsetPx = newOffset
          return Offset(0f, consumedY)
        }
        return Offset.Zero
      }
    }
  }

  val collapseFraction = (-scrollOffsetPx / maxScrollRange).coerceIn(0f, 1f)
  val currentHeightDp =
    with(density) { (maxHeightPx + scrollOffsetPx).toDp() }.coerceAtLeast(collapsedHeight)
  val topBarBg by
    animateColorAsState(
      targetValue =
        XuiColorHelper.computeColor(
          colors.brandPrimary,
          colors.topBarBackground,
          collapseFraction,
        ),
      label = "collapsing_topbar_bg",
    )
  val topBarFg =
    XuiColorHelper.computeColor(colors.heroText, colors.topBarContent, collapseFraction)

  Box(modifier = modifier.fillMaxSize().nestedScroll(nestedScrollConnection)) {
    Box(modifier = Modifier.fillMaxSize()) {
      content(PaddingValues(top = currentHeightDp))
    }

    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(currentHeightDp)
          .background(
            Brush.verticalGradient(
              listOf(colors.brandPrimary, colors.brandPrimaryPressed)
            )
          )
          .background(topBarBg.copy(alpha = collapseFraction)),
    ) {
      Box(
        modifier =
          Modifier.fillMaxSize()
            .alpha(1f - collapseFraction)
            .graphicsLayer { translationY = scrollOffsetPx * motion.headerParallaxFactor },
      ) {
        headerBackground(collapseFraction)
      }

      Column(
        modifier =
          Modifier.fillMaxWidth()
            .align(Alignment.BottomStart)
            .padding(start = dimens.spacing20, end = dimens.spacing20, bottom = dimens.spacing18)
            .alpha((1f - collapseFraction * motion.collapsingSubtitleFadeRate).coerceIn(0f, 1f)),
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = textSizes.headline,
          ),
          color = colors.heroText,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        if (!expandedSubtitle.isNullOrBlank()) {
          Spacer(modifier = Modifier.height(dimens.spacing4))
          Text(
            text = expandedSubtitle,
            style = MaterialTheme.typography.bodySmall,
            color = colors.heroTextMuted,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
          )
        }
      }

      Column(
        modifier =
          Modifier.fillMaxWidth().then(
            if (applyStatusBarPadding) Modifier.windowInsetsPadding(WindowInsets.statusBars)
            else Modifier
          )
      ) {
        Box(
          modifier = Modifier.fillMaxWidth().height(collapsedHeight).padding(horizontal = dimens.spacing4),
        ) {
          if (onBackClick != null) {
            IconButton(
              onClick = onBackClick,
              modifier = Modifier.align(Alignment.CenterStart).testTag("xui_collapsing_back"),
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = strings.common.backContentDescription,
                tint = topBarFg,
              )
            }
          }

          Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.SemiBold,
              fontSize = textSizes.titleMd,
            ),
            color = topBarFg,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier =
              Modifier.align(Alignment.Center)
                .padding(horizontal = dimens.collapsingTitleSidePadding)
                .alpha(
                  ((collapseFraction - motion.collapsingTitleStartFraction) /
                    motion.collapsingTitleFadeSpan)
                    .coerceIn(0f, 1f)
                ),
          )

          Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            content = rightActions,
          )
        }
      }

      if (collapseFraction > motion.collapsingDividerFraction) {
        HorizontalDivider(
          modifier = Modifier.align(Alignment.BottomCenter),
          thickness = dimens.hairline,
          color = colors.separatorHairline,
        )
      }
    }
  }
}
