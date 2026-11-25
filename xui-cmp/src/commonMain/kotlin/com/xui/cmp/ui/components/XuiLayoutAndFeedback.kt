package com.xui.cmp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiLinkPatterns
import com.xui.cmp.tokens.XuiTemplateFormatter
import kotlin.math.max
import kotlin.math.roundToInt

@Composable
fun XuiFloatLayout(
  modifier: Modifier = Modifier,
  horizontalSpacing: Dp = XuiThemeTokens.dimens.spacing8,
  verticalSpacing: Dp = XuiThemeTokens.dimens.spacing8,
  alignment: Alignment.Horizontal = Alignment.Start,
  maxLines: Int = Int.MAX_VALUE,
  content: @Composable () -> Unit,
) {
  Layout(
    content = content,
    modifier = modifier.testTag("xui_float_layout"),
  ) { measurables, constraints ->
    val hSpacePx = horizontalSpacing.roundToPx()
    val vSpacePx = verticalSpacing.roundToPx()
    val maxLineWidth = if (constraints.hasBoundedWidth) constraints.maxWidth else Int.MAX_VALUE
    val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)

    val rows = mutableListOf<MutableList<Placeable>>()
    val rowWidths = mutableListOf<Int>()
    val rowHeights = mutableListOf<Int>()

    var currentRow = mutableListOf<Placeable>()
    var currentWidth = 0
    var currentHeight = 0

    for (measurable in measurables) {
      val placeable = measurable.measure(childConstraints)
      val neededWidth =
        if (currentRow.isEmpty()) placeable.width else currentWidth + hSpacePx + placeable.width

      if (currentRow.isNotEmpty() && neededWidth > maxLineWidth) {
        if (rows.size >= maxLines) break
        rows.add(currentRow)
        rowWidths.add(currentWidth)
        rowHeights.add(currentHeight)
        if (rows.size >= maxLines) {
          currentRow = mutableListOf()
          currentWidth = 0
          currentHeight = 0
          break
        }
        currentRow = mutableListOf(placeable)
        currentWidth = placeable.width
        currentHeight = placeable.height
      } else {
        currentRow.add(placeable)
        currentWidth = neededWidth
        currentHeight = max(currentHeight, placeable.height)
      }
    }

    if (currentRow.isNotEmpty() && rows.size < maxLines) {
      rows.add(currentRow)
      rowWidths.add(currentWidth)
      rowHeights.add(currentHeight)
    }

    val totalWidth =
      if (constraints.hasBoundedWidth) constraints.maxWidth
      else (rowWidths.maxOrNull() ?: 0).coerceIn(constraints.minWidth, constraints.maxWidth)

    val totalHeight =
      (rowHeights.sum() + max(0, rows.size - 1) * vSpacePx).coerceIn(
        constraints.minHeight,
        constraints.maxHeight,
      )

    layout(totalWidth, totalHeight) {
      var yOffset = 0
      rows.forEachIndexed { rowIndex, row ->
        val rowWidth = rowWidths[rowIndex]
        val rowHeight = rowHeights[rowIndex]
        var xOffset =
          when (alignment) {
            Alignment.CenterHorizontally -> (totalWidth - rowWidth) / 2
            Alignment.End -> totalWidth - rowWidth
            else -> 0
          }.coerceAtLeast(0)

        row.forEach { placeable ->
          val itemY = yOffset + (rowHeight - placeable.height) / 2
          placeable.placeRelative(x = xOffset, y = itemY)
          xOffset += placeable.width + hSpacePx
        }
        yOffset += rowHeight + vSpacePx
      }
    }
  }
}

enum class XuiEmptyState {
  LOADING,
  EMPTY,
  ERROR,
  INFO,
}

@Composable
fun XuiEmptyView(
  state: XuiEmptyState,
  title: String,
  modifier: Modifier = Modifier,
  detail: String? = null,
  actionText: String? = null,
  onActionClick: (() -> Unit)? = null,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes

  Column(
    modifier = modifier.fillMaxWidth().padding(horizontal = dimens.spacing28, vertical = dimens.spacing32),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
  ) {
    when (state) {
      XuiEmptyState.LOADING -> {
        CircularProgressIndicator(
          color = colors.brandPrimary,
          strokeWidth = dimens.progressStrokeMd,
          modifier = Modifier.size(dimens.emptyLoadingSize),
        )
      }
      XuiEmptyState.EMPTY -> {
        Surface(
          shape = shapes.emptyIcon,
          color = colors.brandContainer,
          modifier = Modifier.size(dimens.iconBoxXl),
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Inbox,
              contentDescription = null,
              tint = colors.brandPrimary,
              modifier = Modifier.size(dimens.iconEmpty),
            )
          }
        }
      }
      XuiEmptyState.ERROR -> {
        Surface(
          shape = shapes.emptyIcon,
          color = colors.error.copy(alpha = 0.12f),
          modifier = Modifier.size(dimens.iconBoxXl),
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.CloudOff,
              contentDescription = null,
              tint = colors.error,
              modifier = Modifier.size(dimens.iconEmpty),
            )
          }
        }
      }
      XuiEmptyState.INFO -> {
        Surface(
          shape = shapes.emptyIcon,
          color = colors.elevatedSurface,
          modifier = Modifier.size(dimens.iconBoxXl),
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              imageVector = Icons.Default.Info,
              contentDescription = null,
              tint = colors.textSecondary,
              modifier = Modifier.size(dimens.iconEmpty),
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(dimens.spacing16))
    Text(
      text = title,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = textSizes.titleSm,
      ),
      color = colors.titlePrimary,
      textAlign = TextAlign.Center,
    )
    if (!detail.isNullOrBlank()) {
      Spacer(modifier = Modifier.height(dimens.spacing6))
      Text(
        text = detail,
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = textSizes.bodySm,
          lineHeight = textSizes.lineHeightBodyCompact,
        ),
        color = colors.textSecondary,
        textAlign = TextAlign.Center,
      )
    }
    if (!actionText.isNullOrBlank() && onActionClick != null) {
      Spacer(modifier = Modifier.height(dimens.spacing18))
      XuiRoundButton(
        text = actionText,
        style = if (state == XuiEmptyState.ERROR) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
        onClick = onActionClick,
      )
    }
  }
}

@Composable
fun XuiPullRefreshLayout(
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable BoxScope.() -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings
  val state = rememberPullToRefreshState()

  PullToRefreshBox(
    isRefreshing = isRefreshing,
    onRefresh = onRefresh,
    state = state,
    modifier = modifier.testTag("xui_pull_refresh_layout"),
    indicator = {
      if (isRefreshing || state.distanceFraction > motion.pullRefreshMinFraction) {
        Box(
          modifier = Modifier.align(Alignment.TopCenter).padding(top = dimens.spacing12),
        ) {
          Surface(
            shape = shapes.emptyIcon,
            color = colors.cardSurface,
            border = BorderStroke(dimens.hairline, colors.separatorHairline),
            shadowElevation = dimens.shadowSm,
          ) {
            Row(
              modifier = Modifier.padding(horizontal = dimens.spacing14, vertical = dimens.spacing8),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              if (isRefreshing) {
                CircularProgressIndicator(
                  modifier = Modifier.size(dimens.progressButtonSpinnerSize),
                  strokeWidth = dimens.progressStrokeSm,
                  color = colors.brandPrimary,
                )
              } else {
                val sweep = (state.distanceFraction.coerceIn(0f, 1f)) * 300f
                Canvas(modifier = Modifier.size(dimens.progressButtonSpinnerSize)) {
                  drawArc(
                    color = colors.brandPrimary,
                    startAngle = -90f,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = dimens.progressStrokePull.toPx(), cap = StrokeCap.Round),
                  )
                }
              }
              Spacer(modifier = Modifier.width(dimens.spacing8))
              Text(
                text =
                  when {
                    isRefreshing ->
                      XuiTemplateFormatter.format(
                        strings.common.pullRefreshingTemplate,
                        "brand" to strings.common.appBrand,
                      )
                    state.distanceFraction >= 1f -> strings.common.pullReleaseReady
                    else -> strings.common.pullToRefresh
                  },
                style = MaterialTheme.typography.labelMedium,
                color = colors.textSecondary,
              )
            }
          }
        }
      }
    },
    content = content,
  )
}

@Composable
fun XuiListPopup(
  expanded: Boolean,
  items: List<String>,
  onDismiss: () -> Unit,
  onItemClick: (Int) -> Unit,
  modifier: Modifier = Modifier,
  showArrow: Boolean = true,
) {
  if (!expanded) return
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes

  Popup(
    alignment = Alignment.TopEnd,
    offset = IntOffset(0, 12),
    onDismissRequest = onDismiss,
    properties = PopupProperties(focusable = true),
  ) {
    Column(
      modifier = modifier.widthIn(min = dimens.popupMinWidth, max = dimens.popupListMaxWidth),
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
        modifier = Modifier.testTag("xui_list_popup"),
      ) {
        Column(modifier = Modifier.fillMaxWidth()) {
          items.forEachIndexed { index, label ->
            Box(
              modifier =
                Modifier.fillMaxWidth()
                  .defaultMinSize(minHeight = dimens.buttonMinHeight)
                  .clickable {
                    onDismiss()
                    onItemClick(index)
                  }
                  .padding(horizontal = dimens.spacing16, vertical = dimens.spacing11),
              contentAlignment = Alignment.CenterStart,
            ) {
              Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Medium,
                  fontSize = textSizes.bodyMd,
                ),
                color = colors.titlePrimary,
              )
            }
            if (index < items.lastIndex) {
              XuiDivider(startIndent = dimens.spacing14)
            }
          }
        }
      }
    }
  }
}

@Composable
fun XuiLinkTextView(
  text: String,
  onLinkClick: (String) -> Unit,
  modifier: Modifier = Modifier,
  linkColor: Color = XuiThemeTokens.colors.brandPrimary,
  textColor: Color = XuiThemeTokens.colors.titlePrimary,
) {
  val textSizes = XuiThemeTokens.textSizes
  val linkPattern = remember { Regex(XuiLinkPatterns.DETECTABLE_LINK_REGEX) }

  val annotated =
    remember(text, linkColor) {
      buildAnnotatedString {
        var lastIndex = 0
        linkPattern.findAll(text).forEach { match ->
          if (match.range.first > lastIndex) {
            append(text.substring(lastIndex, match.range.first))
          }
          val token = match.value
          withLink(
            LinkAnnotation.Clickable(
              tag = token,
              styles =
                TextLinkStyles(
                  style =
                    SpanStyle(
                      color = linkColor,
                      fontWeight = FontWeight.Medium,
                      textDecoration = TextDecoration.Underline,
                    ),
                ),
              linkInteractionListener = { onLinkClick(token) },
            )
          ) {
            append(token)
          }
          lastIndex = match.range.last + 1
        }
        if (lastIndex < text.length) {
          append(text.substring(lastIndex))
        }
      }
    }

  Text(
    text = annotated,
    style = MaterialTheme.typography.bodyMedium.copy(
      color = textColor,
      fontSize = textSizes.bodyMd,
      lineHeight = textSizes.lineHeightLink,
    ),
    modifier = modifier.testTag("xui_link_text_view"),
  )
}

@Composable
fun XuiProgressBar(
  progress: Float,
  modifier: Modifier = Modifier,
  circular: Boolean = false,
  size: Dp = XuiThemeTokens.dimens.progressRingSize,
  strokeWidth: Dp = XuiThemeTokens.dimens.progressStrokeLg,
  showLabel: Boolean = true,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings
  val clamped = progress.coerceIn(0f, 1f)
  val animatedProgress by
    animateFloatAsState(
      targetValue = clamped,
      animationSpec = tween(durationMillis = motion.progressAnimDurationMs, easing = LinearEasing),
      label = "xui_progress",
    )
  val percent = (animatedProgress * 100f).roundToInt()
  val percentLabel =
    XuiTemplateFormatter.format(strings.common.percentTemplate, "value" to percent)

  if (circular) {
    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
      Canvas(modifier = Modifier.fillMaxSize()) {
        val strokePx = strokeWidth.toPx()
        val inset = strokePx / 2f
        val arcSize = Size(this.size.width - strokePx, this.size.height - strokePx)
        drawArc(
          color = colors.separatorHairline,
          startAngle = 0f,
          sweepAngle = 360f,
          useCenter = false,
          topLeft = Offset(inset, inset),
          size = arcSize,
          style = Stroke(width = strokePx, cap = StrokeCap.Round),
        )
        drawArc(
          color = colors.brandPrimary,
          startAngle = -90f,
          sweepAngle = 360f * animatedProgress,
          useCenter = false,
          topLeft = Offset(inset, inset),
          size = arcSize,
          style = Stroke(width = strokePx, cap = StrokeCap.Round),
        )
      }
      if (showLabel) {
        Text(
          text = percentLabel,
          style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            fontSize = textSizes.labelLg,
          ),
          color = colors.titlePrimary,
        )
      }
    }
  } else {
    Row(
      modifier = modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Box(
        modifier =
          Modifier.weight(1f)
            .height(strokeWidth)
            .clip(RoundedCornerShape(strokeWidth / 2))
            .background(colors.separatorHairline),
      ) {
        Box(
          modifier =
            Modifier.fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
              .height(strokeWidth)
              .clip(RoundedCornerShape(strokeWidth / 2))
              .background(colors.brandPrimary),
        )
      }
      if (showLabel) {
        Spacer(modifier = Modifier.width(dimens.spacing10))
        Text(
          text = percentLabel,
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
          color = colors.textSecondary,
        )
      }
    }
  }
}
