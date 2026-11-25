package com.xui.cmp.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import com.xui.cmp.theme.XuiThemeTokens

enum class XuiTabIndicatorStyle {
  UNDERLINE,
  PILL,
  DOT,
}

data class XuiTabItem(
  val title: String,
  val icon: ImageVector? = null,
  val badgeCount: Int = 0,
  val showDot: Boolean = false,
)

@Composable
fun XuiTabSegment(
  tabs: List<XuiTabItem>,
  selectedIndex: Int,
  onTabSelected: (Int) -> Unit,
  modifier: Modifier = Modifier,
  indicatorStyle: XuiTabIndicatorStyle = XuiTabIndicatorStyle.UNDERLINE,
  scrollable: Boolean = false,
  showBottomSeparator: Boolean = true,
) {
  if (tabs.isEmpty()) return
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val motion = XuiThemeTokens.motion
  val density = LocalDensity.current
  val safeSelected = selectedIndex.coerceIn(0, tabs.lastIndex)

  Surface(
    modifier = modifier.fillMaxWidth(),
    color = colors.cardSurface,
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      if (scrollable) {
        val tabBounds = remember { mutableStateMapOf<Int, Pair<Float, Float>>() }
        val scrollState = rememberScrollState()
        val activeBounds = tabBounds[safeSelected]
        val indicatorX by
          animateDpAsState(
            targetValue = with(density) { (activeBounds?.first ?: 0f).toDp() },
            animationSpec = spring(),
            label = "tab_scroll_x",
          )
        val indicatorW by
          animateDpAsState(
            targetValue = with(density) { (activeBounds?.second ?: 0f).toDp() },
            animationSpec = spring(),
            label = "tab_scroll_w",
          )

        Box(modifier = Modifier.fillMaxWidth().horizontalScroll(scrollState)) {
          if (indicatorStyle == XuiTabIndicatorStyle.PILL && activeBounds != null) {
            Box(
              modifier =
                Modifier.offset(x = indicatorX + dimens.tabPillHorizontalInset, y = dimens.tabPillTopOffset)
                  .width((indicatorW - dimens.spacing8).coerceAtLeast(dimens.zero))
                  .height(dimens.tabPillHeight)
                  .clip(RoundedCornerShape(dimens.tabPillCornerRadius))
                  .background(colors.brandContainer)
            )
          }

          Row(
            modifier = Modifier.padding(horizontal = dimens.spacing8),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            tabs.forEachIndexed { index, tab ->
              XuiTabCell(
                item = tab,
                selected = index == safeSelected,
                modifier =
                  Modifier.onGloballyPositioned { coords ->
                    tabBounds[index] = coords.positionInParent().x to coords.size.width.toFloat()
                  },
                onClick = { onTabSelected(index) },
              )
            }
          }

          if (indicatorStyle == XuiTabIndicatorStyle.UNDERLINE && activeBounds != null) {
            Box(
              modifier =
                Modifier.align(Alignment.BottomStart)
                  .offset(x = indicatorX + dimens.spacing14)
                  .width((indicatorW - dimens.tabIndicatorInsetTotal).coerceAtLeast(dimens.spacing16))
                  .height(dimens.tabIndicatorHeight)
                  .clip(RoundedCornerShape(topStart = dimens.tabIndicatorCorner, topEnd = dimens.tabIndicatorCorner))
                  .background(colors.brandPrimary)
            )
          } else if (indicatorStyle == XuiTabIndicatorStyle.DOT && activeBounds != null) {
            Box(
              modifier =
                Modifier.align(Alignment.BottomStart)
                  .offset(
                    x = indicatorX + indicatorW / 2 - dimens.spacing3,
                    y = dimens.tabDotBottomOffset,
                  )
                  .size(dimens.tabDotSize)
                  .clip(shapes.circle)
                  .background(colors.brandPrimary)
            )
          }
        }
      } else {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
          val tabWidth = maxWidth / tabs.size
          val indicatorOffset by
            animateDpAsState(
              targetValue = tabWidth * safeSelected,
              animationSpec = spring(),
              label = "tab_fixed_offset",
            )

          if (indicatorStyle == XuiTabIndicatorStyle.PILL) {
            Box(
              modifier =
                Modifier.offset {
                  IntOffset(
                    (indicatorOffset + dimens.spacing6).roundToPx(),
                    dimens.tabPillTopOffset.roundToPx(),
                  )
                }
                  .width((tabWidth - dimens.spacing12).coerceAtLeast(dimens.zero))
                  .height(dimens.tabPillHeight)
                  .clip(RoundedCornerShape(dimens.tabPillCornerRadius))
                  .background(colors.brandContainer)
            )
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            tabs.forEachIndexed { index, tab ->
              XuiTabCell(
                item = tab,
                selected = index == safeSelected,
                modifier = Modifier.weight(1f),
                onClick = { onTabSelected(index) },
              )
            }
          }

          if (indicatorStyle == XuiTabIndicatorStyle.UNDERLINE) {
            val lineWidth =
              (tabWidth * motion.tabUnderlineWidthRatio)
                .coerceIn(dimens.tabIndicatorMinWidth, dimens.tabIndicatorMaxWidth)
            Box(
              modifier =
                Modifier.align(Alignment.BottomStart)
                  .offset(x = indicatorOffset + (tabWidth - lineWidth) / 2)
                  .width(lineWidth)
                  .height(dimens.tabIndicatorHeight)
                  .clip(RoundedCornerShape(topStart = dimens.tabIndicatorCorner, topEnd = dimens.tabIndicatorCorner))
                  .background(colors.brandPrimary)
            )
          } else if (indicatorStyle == XuiTabIndicatorStyle.DOT) {
            Box(
              modifier =
                Modifier.align(Alignment.BottomStart)
                  .offset(
                    x = indicatorOffset + tabWidth / 2 - dimens.spacing3,
                    y = dimens.tabDotBottomOffset,
                  )
                  .size(dimens.tabDotSize)
                  .clip(shapes.circle)
                  .background(colors.brandPrimary)
            )
          }
        }
      }

      if (showBottomSeparator) {
        XuiDivider()
      }
    }
  }
}

@Composable
private fun XuiTabCell(
  item: XuiTabItem,
  selected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings
  val textColor by
    animateColorAsState(
      targetValue = if (selected) colors.brandPrimary else colors.textSecondary,
      label = "tab_text_color",
    )
  val interactionSource = remember { MutableInteractionSource() }

  Row(
    modifier =
      modifier
        .height(dimens.tabHeight)
        .clip(shapes.badge)
        .clickable(
          interactionSource = interactionSource,
          indication = ripple(bounded = true),
          onClick = onClick,
        )
        .padding(horizontal = dimens.spacing14)
        .testTag("xui_tab_${item.title}"),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center,
  ) {
    if (item.icon != null) {
      Icon(
        imageVector = item.icon,
        contentDescription = null,
        tint = textColor,
        modifier = Modifier.size(dimens.iconSm),
      )
      Spacer(modifier = Modifier.width(dimens.spacing5))
    }
    Text(
      text = item.title,
      style = MaterialTheme.typography.labelLarge.copy(
        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
        fontSize = textSizes.bodyMd,
      ),
      color = textColor,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
    )
    if (item.badgeCount > 0) {
      Spacer(modifier = Modifier.width(dimens.spacing5))
      Box(
        modifier =
          Modifier.clip(shapes.circle)
            .background(colors.error)
            .padding(horizontal = dimens.spacing5, vertical = dimens.spacing1),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text =
            if (item.badgeCount > motion.badgeOverflowThreshold) strings.common.badgeOverflow
            else item.badgeCount.toString(),
          style = MaterialTheme.typography.labelSmall.copy(
            fontSize = textSizes.micro,
            fontWeight = FontWeight.Bold,
          ),
          color = colors.onAccentBadge,
        )
      }
    } else if (item.showDot) {
      Spacer(modifier = Modifier.width(dimens.spacing4))
      Box(modifier = Modifier.size(dimens.badgeDotSize).clip(shapes.circle).background(colors.error))
    }
  }
}
