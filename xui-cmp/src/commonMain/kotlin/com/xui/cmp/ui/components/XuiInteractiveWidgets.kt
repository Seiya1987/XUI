package com.xui.cmp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiTemplateFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Pure CMP replacement for XUISlider supporting continuous/stepped values and optional dragging bubble.
 */
@Composable
fun XuiSlider(
  value: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  valueRange: ClosedFloatingPointRange<Float> = 0f..100f,
  steps: Int = 0,
  showBubbleOnDrag: Boolean = true,
  thumbSize: Dp = XuiThemeTokens.dimens.sliderThumbSize,
  trackHeight: Dp = XuiThemeTokens.dimens.sliderTrackHeight,
  valueFormatter: (Float) -> String = { "${it.roundToInt()}" },
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val density = LocalDensity.current
  val minVal = valueRange.start
  val maxVal = valueRange.endInclusive.coerceAtLeast(minVal + 0.001f)
  val span = maxVal - minVal

  fun snapValue(raw: Float): Float {
    val clamped = raw.coerceIn(minVal, maxVal)
    if (steps <= 0) return clamped
    val stepSize = span / (steps + 1)
    val index = ((clamped - minVal) / stepSize).roundToInt().coerceIn(0, steps + 1)
    return (minVal + index * stepSize).coerceIn(minVal, maxVal)
  }

  val normalized = ((value - minVal) / span).coerceIn(0f, 1f)
  var isDragging by remember { mutableStateOf(false) }

  BoxWithConstraints(
    modifier =
      modifier
        .fillMaxWidth()
        .height(if (showBubbleOnDrag) dimens.sliderBubbleHeight else dimens.sliderHeight)
        .testTag("xui_slider"),
    contentAlignment = Alignment.BottomStart,
  ) {
    val totalWidthPx = with(density) { maxWidth.toPx() }
    val thumbRadiusPx = with(density) { (thumbSize / 2).toPx() }
    val usableWidthPx = (totalWidthPx - thumbRadiusPx * 2f).coerceAtLeast(1f)
    val thumbCenterPx = thumbRadiusPx + normalized * usableWidthPx

    if (showBubbleOnDrag && isDragging) {
      val bubbleHalfPx = with(density) { dimens.spacing22.toPx() }
      val bubbleMaxX = (totalWidthPx - with(density) { dimens.sliderBubbleMinWidth.toPx() }).roundToInt().coerceAtLeast(0)
      val bubbleOffsetX = (thumbCenterPx - bubbleHalfPx).roundToInt()
      Surface(
        shape = shapes.badge,
        color = colors.tipHudBackground,
        shadowElevation = dimens.shadowSm,
        modifier =
          Modifier.align(Alignment.TopStart)
            .offset { IntOffset(bubbleOffsetX.coerceIn(0, bubbleMaxX), 0) }
            .defaultMinSize(minWidth = dimens.sliderBubbleMinWidth),
      ) {
        Text(
          text = valueFormatter(value),
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = textSizes.labelSm,
          ),
          color = colors.onTipHud,
          modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
        )
      }
    }

    Box(
      modifier =
        Modifier.fillMaxWidth()
          .height(dimens.sliderHeight)
          .pointerInput(minVal, maxVal, steps, usableWidthPx) {
            detectTapGestures { offset ->
              val ratio = ((offset.x - thumbRadiusPx) / usableWidthPx).coerceIn(0f, 1f)
              onValueChange(snapValue(minVal + ratio * span))
            }
          }
          .pointerInput(minVal, maxVal, steps, usableWidthPx) {
            detectDragGestures(
              onDragStart = { offset ->
                isDragging = true
                val ratio = ((offset.x - thumbRadiusPx) / usableWidthPx).coerceIn(0f, 1f)
                onValueChange(snapValue(minVal + ratio * span))
              },
              onDragEnd = { isDragging = false },
              onDragCancel = { isDragging = false },
              onDrag = { change, _ ->
                change.consume()
                val ratio = ((change.position.x - thumbRadiusPx) / usableWidthPx).coerceIn(0f, 1f)
                onValueChange(snapValue(minVal + ratio * span))
              },
            )
          },
      contentAlignment = Alignment.CenterStart,
    ) {
      Canvas(modifier = Modifier.fillMaxWidth().height(trackHeight)) {
        val cy = size.height / 2f
        val startX = thumbRadiusPx
        val endX = size.width - thumbRadiusPx
        val activeEndX = startX + normalized * (endX - startX).coerceAtLeast(0f)

        drawLine(
          color = colors.separatorHairline,
          start = Offset(startX, cy),
          end = Offset(endX, cy),
          strokeWidth = trackHeight.toPx(),
          cap = StrokeCap.Round,
        )
        drawLine(
          color = colors.brandPrimary,
          start = Offset(startX, cy),
          end = Offset(activeEndX, cy),
          strokeWidth = trackHeight.toPx(),
          cap = StrokeCap.Round,
        )

        if (steps in 1..12) {
          for (i in 0..(steps + 1)) {
            val tickRatio = i.toFloat() / (steps + 1)
            val tickX = startX + tickRatio * (endX - startX)
            drawCircle(
              color = if (tickRatio <= normalized) colors.sliderActiveTick else colors.textPlaceholder,
              radius = dimens.sliderTickRadius.toPx(),
              center = Offset(tickX, cy),
            )
          }
        }
      }

      Box(
        modifier =
          Modifier.offset { IntOffset((thumbCenterPx - thumbRadiusPx).roundToInt(), 0) }
            .size(thumbSize)
            .shadow(elevation = if (isDragging) dimens.shadowMd else dimens.shadowXs, shape = shapes.circle)
            .clip(shapes.circle)
            .background(colors.sliderThumbSurface),
        contentAlignment = Alignment.Center,
      ) {
        Box(
          modifier =
            Modifier.size(thumbSize * motion.sliderThumbInnerRatio)
              .clip(shapes.circle)
              .background(colors.brandPrimary)
        )
      }
    }
  }
}

data class XuiStickySection<T>(
  val title: String,
  val badge: String? = null,
  val items: List<T>,
)

/**
 * Pure CMP replacement for XUIStickySectionLayout using LazyColumn stickyHeader.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> XuiStickySectionList(
  sections: List<XuiStickySection<T>>,
  modifier: Modifier = Modifier,
  contentPadding: PaddingValues = PaddingValues(XuiThemeTokens.dimens.zero),
  itemContent: @Composable (sectionIndex: Int, item: T, isLastInSection: Boolean) -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes

  LazyColumn(
    modifier = modifier.fillMaxWidth().testTag("xui_sticky_section_list"),
    contentPadding = contentPadding,
  ) {
    sections.forEachIndexed { sectionIdx, section ->
      stickyHeader(key = "sticky_header_$sectionIdx") {
        Surface(
          color = colors.elevatedSurface,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Column {
            Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = dimens.spacing16, vertical = dimens.spacing10),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Text(
                text = section.title,
                style = MaterialTheme.typography.labelLarge.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = textSizes.labelMd,
                ),
                color = colors.brandPrimary,
              )
              if (!section.badge.isNullOrBlank()) {
                Text(
                  text = section.badge,
                  style = MaterialTheme.typography.labelSmall,
                  color = colors.textSecondary,
                )
              }
            }
            XuiDivider()
          }
        }
      }

      itemsIndexed(
        items = section.items,
        key = { itemIdx, _ -> "section_${sectionIdx}_item_$itemIdx" },
      ) { itemIdx, item ->
        itemContent(sectionIdx, item, itemIdx == section.items.lastIndex)
      }
    }
  }
}

data class XuiSwipeActionSpec(
  val label: String,
  val containerColor: Color,
  val contentColor: Color? = null,
  val onClick: () -> Unit,
)

/**
 * Pure CMP replacement for XUIRVItemSwipeAction (swipe-to-reveal action buttons).
 */
@Composable
fun XuiSwipeActionRow(
  actions: List<XuiSwipeActionSpec>,
  modifier: Modifier = Modifier,
  actionWidth: Dp = XuiThemeTokens.dimens.swipeActionWidth,
  content: @Composable () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val density = LocalDensity.current
  val scope = rememberCoroutineScope()
  val maxRevealPx = with(density) { (actionWidth * actions.size).toPx() }
  val offsetX = remember { Animatable(0f) }

  Box(
    modifier =
      modifier
        .fillMaxWidth()
        .clip(shapes.popup)
        .background(colors.cardSurface)
        .testTag("xui_swipe_action_row"),
  ) {
    if (actions.isNotEmpty()) {
      Row(
        modifier = Modifier.align(Alignment.CenterEnd).matchParentSize(),
        horizontalArrangement = Arrangement.End,
      ) {
        actions.forEach { action ->
          Box(
            modifier =
              Modifier.width(actionWidth)
                .fillMaxHeight()
                .background(action.containerColor)
                .clickable {
                  scope.launch {
                    offsetX.animateTo(0f, spring())
                  }
                  action.onClick()
                },
            contentAlignment = Alignment.Center,
          ) {
            Text(
              text = action.label,
              style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = textSizes.labelMd,
              ),
              color = action.contentColor ?: colors.onAccentBadge,
              maxLines = 1,
            )
          }
        }
      }
    }

    Box(
      modifier =
        Modifier.fillMaxWidth()
          .offset { IntOffset(offsetX.value.roundToInt(), 0) }
          .background(colors.cardSurface)
          .pointerInput(maxRevealPx) {
            detectHorizontalDragGestures(
              onDragEnd = {
                scope.launch {
                  val target =
                    if (offsetX.value < -maxRevealPx * motion.swipeActionThresholdFraction) {
                      -maxRevealPx
                    } else 0f
                  offsetX.animateTo(target, spring())
                }
              },
              onDragCancel = {
                scope.launch { offsetX.animateTo(0f, spring()) }
              },
              onHorizontalDrag = { change, dragAmount ->
                change.consume()
                scope.launch {
                  val next = (offsetX.value + dragAmount).coerceIn(-maxRevealPx, 0f)
                  offsetX.snapTo(next)
                }
              },
            )
          }
          .clickable(enabled = offsetX.value < -motion.swipeActionCloseThresholdPx) {
            scope.launch { offsetX.animateTo(0f, spring()) }
          },
    ) {
      content()
    }
  }
}

/**
 * Pure CMP cross-platform left-edge swipe-back gesture container (replacement for XUISwipeBackLayout).
 * Works uniformly on both Android and iOS.
 */
@Composable
fun XuiSwipeBackLayout(
  onSwipeBack: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  edgeWidth: Dp = XuiThemeTokens.dimens.swipeEdgeWidth,
  content: @Composable BoxScope.() -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val motion = XuiThemeTokens.motion
  val density = LocalDensity.current
  val scope = rememberCoroutineScope()
  val edgePx = with(density) { edgeWidth.toPx() }
  val dragOffset = remember { Animatable(0f) }
  var trackingEdge by remember { mutableStateOf(false) }

  BoxWithConstraints(modifier = modifier.fillMaxSize().testTag("xui_swipe_back_layout")) {
    val widthPx = with(density) { maxWidth.toPx().coerceAtLeast(1f) }
    val dragRatio = (dragOffset.value / widthPx).coerceIn(0f, 1f)

    if (dragRatio > motion.swipeBackRevealMinRatio) {
      Box(
        modifier =
          Modifier.fillMaxSize()
            .background(colors.swipeBackScrim.copy(alpha = 0.24f * (1f - dragRatio)))
      )
    }

    Box(
      modifier =
        Modifier.fillMaxSize()
          .offset { IntOffset(dragOffset.value.roundToInt(), 0) }
          .pointerInput(enabled, edgePx, widthPx) {
            if (!enabled) return@pointerInput
            detectHorizontalDragGestures(
              onDragStart = { startOffset ->
                trackingEdge = startOffset.x <= edgePx
              },
              onDragEnd = {
                if (trackingEdge) {
                  val shouldPop = dragOffset.value > widthPx * motion.swipeBackThresholdFraction
                  trackingEdge = false
                  scope.launch {
                    if (shouldPop) {
                      dragOffset.animateTo(widthPx, spring())
                      onSwipeBack()
                    } else {
                      dragOffset.animateTo(0f, spring())
                    }
                  }
                }
              },
              onDragCancel = {
                trackingEdge = false
                scope.launch { dragOffset.animateTo(0f, spring()) }
              },
              onHorizontalDrag = { change, dragAmount ->
                if (!trackingEdge) return@detectHorizontalDragGestures
                change.consume()
                scope.launch {
                  val next = (dragOffset.value + dragAmount).coerceIn(0f, widthPx)
                  dragOffset.snapTo(next)
                }
              },
            )
          },
      content = content,
    )
  }
}

/**
 * Pure CMP general bubble popup (replacement for XUIPopup).
 */
@Composable
fun XuiBubblePopup(
  expanded: Boolean,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
  showArrow: Boolean = true,
  content: @Composable ColumnScopeContent.() -> Unit,
) {
  if (!expanded) return
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes

  Popup(
    alignment = Alignment.TopEnd,
    offset = IntOffset(0, 12),
    onDismissRequest = onDismiss,
    properties = PopupProperties(focusable = true),
  ) {
    Column(
      modifier = modifier.widthIn(min = dimens.popupMinWidth, max = dimens.popupMaxWidth),
      horizontalAlignment = Alignment.End,
    ) {
      if (showArrow) {
        Canvas(
          modifier =
            Modifier.padding(end = dimens.spacing18)
              .size(width = dimens.popupArrowWidth, height = dimens.popupArrowHeight)
        ) {
          val path =
            Path().apply {
              moveTo(size.width / 2f, 0f)
              lineTo(size.width, size.height)
              lineTo(0f, size.height)
              close()
            }
          drawPath(path = path, color = colors.cardSurface)
        }
      }
      Surface(
        shape = shapes.popup,
        color = colors.cardSurface,
        border = BorderStroke(dimens.hairline, colors.separatorHairline),
        shadowElevation = dimens.shadowPopover,
        modifier = Modifier.testTag("xui_bubble_popup"),
      ) {
        Column(modifier = Modifier.padding(dimens.spacing14)) {
          ColumnScopeContent.content()
        }
      }
    }
  }
}

object ColumnScopeContent

data class XuiQuickActionItem(
  val label: String,
  val icon: ImageVector? = null,
  val onClick: () -> Unit,
)

/**
 * Pure CMP horizontal quick action bar popup (replacement for XUIQuickAction).
 */
@Composable
fun XuiQuickActionPopup(
  expanded: Boolean,
  actions: List<XuiQuickActionItem>,
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (!expanded || actions.isEmpty()) return
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes

  Popup(
    alignment = Alignment.TopCenter,
    offset = IntOffset(0, -14),
    onDismissRequest = onDismiss,
    properties = PopupProperties(focusable = true),
  ) {
    Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Surface(
        shape = shapes.quickAction,
        color = colors.tipHudBackground,
        shadowElevation = dimens.shadowPopover,
        modifier = Modifier.testTag("xui_quick_action_popup"),
      ) {
        Row(
          modifier = Modifier.height(dimens.quickActionBarHeight),
          verticalAlignment = Alignment.CenterVertically,
        ) {
          actions.forEachIndexed { index, action ->
            Row(
              modifier =
                Modifier.fillMaxHeight()
                  .clickable {
                    onDismiss()
                    action.onClick()
                  }
                  .padding(horizontal = dimens.spacing14),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              if (action.icon != null) {
                Icon(
                  imageVector = action.icon,
                  contentDescription = null,
                  tint = colors.onTipHud,
                  modifier = Modifier.size(dimens.iconBarSmall),
                )
                Spacer(modifier = Modifier.width(dimens.spacing5))
              }
              Text(
                text = action.label,
                style = MaterialTheme.typography.labelMedium.copy(
                  fontWeight = FontWeight.Medium,
                  fontSize = textSizes.bodyXs,
                ),
                color = colors.onTipHud,
              )
            }
            if (index < actions.lastIndex) {
              VerticalDivider(
                modifier = Modifier.height(dimens.quickActionDividerHeight),
                thickness = dimens.hairline,
                color = colors.hudDivider,
              )
            }
          }
        }
      }
      Canvas(modifier = Modifier.size(width = dimens.quickActionArrowWidth, height = dimens.quickActionArrowHeight)) {
        val path =
          Path().apply {
            moveTo(0f, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width / 2f, size.height)
            close()
          }
        drawPath(path = path, color = colors.tipHudBackground)
      }
    }
  }
}

/**
 * Pure CMP replacement for XUIPriorityLinearLayout:
 * allocates fixed/incompressible children first, then distributes remaining space to flexible children.
 */
@Composable
fun XuiPriorityRow(
  modifier: Modifier = Modifier,
  spacing: Dp = XuiThemeTokens.dimens.spacing8,
  verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
  content: @Composable XuiPriorityRowScope.() -> Unit,
) {
  val scope = remember { XuiPriorityRowScopeImpl() }
  Layout(
    content = { scope.content() },
    modifier = modifier.fillMaxWidth().testTag("xui_priority_row"),
  ) { measurables, constraints ->
    val gapPx = spacing.roundToPx()
    val maxWidth = if (constraints.hasBoundedWidth) constraints.maxWidth else Int.MAX_VALUE
    val totalGap = gapPx * max(0, measurables.size - 1)
    var remaining = (maxWidth - totalGap).coerceAtLeast(0)

    val placeables = arrayOfNulls<androidx.compose.ui.layout.Placeable>(measurables.size)
    val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)

    // Pass 1: measure incompressible (high priority) children
    measurables.forEachIndexed { idx, measurable ->
      val isFlexible = measurable.parentData == XuiPriorityTag.FLEXIBLE
      if (!isFlexible) {
        val p = measurable.measure(childConstraints.copy(maxWidth = remaining.coerceAtLeast(0)))
        placeables[idx] = p
        remaining = (remaining - p.width).coerceAtLeast(0)
      }
    }

    // Pass 2: measure flexible (compressible) children within remaining space
    val flexIndices = measurables.indices.filter { measurables[it].parentData == XuiPriorityTag.FLEXIBLE }
    if (flexIndices.isNotEmpty()) {
      var perFlexBudget = remaining
      flexIndices.forEach { idx ->
        val p = measurables[idx].measure(childConstraints.copy(maxWidth = perFlexBudget.coerceAtLeast(0)))
        placeables[idx] = p
        perFlexBudget = (perFlexBudget - p.width).coerceAtLeast(0)
      }
    }

    val measured = placeables.filterNotNull()
    val widthSum = measured.sumOf { it.width } + totalGap
    val totalWidth =
      if (constraints.hasBoundedWidth) constraints.maxWidth
      else widthSum.coerceIn(constraints.minWidth, constraints.maxWidth)
    val totalHeight =
      (measured.maxOfOrNull { it.height } ?: 0).coerceIn(constraints.minHeight, constraints.maxHeight)

    layout(totalWidth, totalHeight) {
      var x = 0
      measured.forEach { placeable ->
        val y = verticalAlignment.align(placeable.height, totalHeight)
        placeable.placeRelative(x = x, y = y)
        x += placeable.width + gapPx
      }
    }
  }
}

private enum class XuiPriorityTag {
  FLEXIBLE,
}

interface XuiPriorityRowScope {
  fun Modifier.xuiFlexible(): Modifier
}

private class XuiPriorityRowScopeImpl : XuiPriorityRowScope {
  override fun Modifier.xuiFlexible(): Modifier =
    this.then(
      object : androidx.compose.ui.layout.ParentDataModifier {
        override fun androidx.compose.ui.unit.Density.modifyParentData(parentData: Any?): Any =
          XuiPriorityTag.FLEXIBLE
      }
    )
}

/**
 * Pure CMP countdown button helper (common in XUI verification & retry flows).
 */
@Composable
fun XuiCountdownButton(
  idleText: String,
  onStartCountdown: () -> Unit,
  modifier: Modifier = Modifier,
  totalSeconds: Int = XuiThemeTokens.motion.defaultCountdownSeconds,
  runningPrefix: String = XuiThemeTokens.strings.common.defaultCountdownPrefix,
) {
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings
  var remaining by remember { mutableIntStateOf(0) }

  LaunchedEffect(remaining) {
    if (remaining > 0) {
      delay(motion.countdownStepMs)
      remaining -= 1
    }
  }

  val runningText =
    XuiTemplateFormatter.format(
      strings.common.countdownRunningTemplate,
      "prefix" to runningPrefix,
      "seconds" to remaining,
    )

  XuiRoundButton(
    text = if (remaining > 0) runningText else idleText,
    style = if (remaining > 0) XuiButtonStyle.OUTLINED else XuiButtonStyle.FILLED,
    enabled = remaining == 0,
    modifier = modifier.testTag("xui_countdown_button"),
    onClick = {
      remaining = totalSeconds
      onStartCountdown()
    },
  )
}
