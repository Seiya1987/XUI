package com.xui.cmp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.xui.cmp.theme.XuiThemeTokens

sealed interface XuiAccessoryType {
  data object None : XuiAccessoryType

  data object Chevron : XuiAccessoryType

  data class SwitchToggle(
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
  ) : XuiAccessoryType

  data class Checkmark(val selected: Boolean) : XuiAccessoryType

  data class RedDot(val count: Int? = null) : XuiAccessoryType

  data class TagBadge(val text: String, val highlight: Boolean = true) : XuiAccessoryType

  data class Custom(val content: @Composable () -> Unit) : XuiAccessoryType
}

@Composable
fun XuiDivider(
  modifier: Modifier = Modifier,
  startIndent: Dp = XuiThemeTokens.dimens.zero,
  color: Color = XuiThemeTokens.colors.separatorHairline,
) {
  HorizontalDivider(
    modifier = modifier.padding(start = startIndent),
    thickness = XuiThemeTokens.dimens.hairline,
    color = color,
  )
}

@Composable
fun XuiGroupListSection(
  modifier: Modifier = Modifier,
  title: String? = null,
  footer: String? = null,
  cardCornerRadius: Dp = XuiThemeTokens.dimens.cardCornerRadius,
  content: @Composable ColumnScope.() -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes

  Column(modifier = modifier.fillMaxWidth()) {
    if (!title.isNullOrBlank()) {
      Text(
        text = title,
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = textSizes.labelMd,
          letterSpacing = textSizes.letterSpacingLabel,
        ),
        color = colors.textSecondary,
        modifier = Modifier.padding(start = dimens.spacing6, end = dimens.spacing6, bottom = dimens.spacing8),
      )
    }

    Surface(
      modifier = Modifier.fillMaxWidth(),
      shape = RoundedCornerShape(cardCornerRadius),
      color = colors.cardSurface,
      border = BorderStroke(dimens.hairline, colors.separatorHairline),
    ) {
      Column(modifier = Modifier.fillMaxWidth(), content = content)
    }

    if (!footer.isNullOrBlank()) {
      Text(
        text = footer,
        style = MaterialTheme.typography.bodySmall.copy(fontSize = textSizes.bodyXs),
        color = colors.textPlaceholder,
        modifier = Modifier.padding(start = dimens.spacing6, end = dimens.spacing6, top = dimens.spacing8),
      )
    }
  }
}

@Composable
fun XuiCommonListItemView(
  title: String,
  modifier: Modifier = Modifier,
  detailText: String? = null,
  verticalDetail: Boolean = false,
  leadingIcon: ImageVector? = null,
  leadingIconTint: Color = XuiThemeTokens.colors.brandPrimary,
  accessory: XuiAccessoryType = XuiAccessoryType.None,
  showBottomDivider: Boolean = true,
  enabled: Boolean = true,
  testTag: String = "xui_list_item",
  onClick: (() -> Unit)? = null,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings
  val interactionSource = remember { MutableInteractionSource() }

  val rowModifier =
    modifier
      .fillMaxWidth()
      .defaultMinSize(minHeight = dimens.listItemMinHeight)
      .then(
        if (onClick != null && enabled) {
          Modifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(bounded = true),
            onClick = onClick,
          )
        } else Modifier
      )
      .padding(horizontal = dimens.spacing16, vertical = dimens.spacing12)
      .testTag(testTag)

  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = rowModifier,
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (leadingIcon != null) {
        Box(
          modifier =
            Modifier.size(dimens.iconBoxMd)
              .clip(shapes.iconContainer)
              .background(leadingIconTint.copy(alpha = 0.12f)),
          contentAlignment = Alignment.Center,
        ) {
          Icon(
            imageVector = leadingIcon,
            contentDescription = null,
            tint = leadingIconTint,
            modifier = Modifier.size(dimens.iconListLead),
          )
        }
        Spacer(modifier = Modifier.width(dimens.spacing12))
      }

      if (verticalDetail) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.Medium,
              fontSize = textSizes.bodyLg,
            ),
            color = if (enabled) colors.titlePrimary else colors.textPlaceholder,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
          )
          if (!detailText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(dimens.spacing2))
            Text(
              text = detailText,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = textSizes.bodyXs),
              color = colors.textSecondary,
              maxLines = 2,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }
      } else {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween,
        ) {
          Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
              fontWeight = FontWeight.Medium,
              fontSize = textSizes.bodyLg,
            ),
            color = if (enabled) colors.titlePrimary else colors.textPlaceholder,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false),
          )
          if (!detailText.isNullOrBlank()) {
            Spacer(modifier = Modifier.width(dimens.spacing12))
            Text(
              text = detailText,
              style = MaterialTheme.typography.bodyMedium.copy(fontSize = textSizes.bodySm),
              color = colors.textSecondary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
            )
          }
        }
      }

      Spacer(modifier = Modifier.width(dimens.spacing8))

      when (accessory) {
        XuiAccessoryType.None -> Unit
        XuiAccessoryType.Chevron -> {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = strings.common.enterContentDescription,
            tint = colors.textPlaceholder,
            modifier = Modifier.size(dimens.iconMd),
          )
        }
        is XuiAccessoryType.SwitchToggle -> {
          Switch(
            checked = accessory.checked,
            onCheckedChange = accessory.onCheckedChange,
            colors =
              SwitchDefaults.colors(
                checkedThumbColor = colors.switchThumb,
                checkedTrackColor = colors.brandPrimary,
                uncheckedThumbColor = colors.switchThumb,
                uncheckedTrackColor = colors.separatorHairline,
                uncheckedBorderColor = colors.transparent,
              ),
          )
        }
        is XuiAccessoryType.Checkmark -> {
          if (accessory.selected) {
            Icon(
              imageVector = Icons.Default.Check,
              contentDescription = strings.common.selectedContentDescription,
              tint = colors.brandPrimary,
              modifier = Modifier.size(dimens.iconMd),
            )
          }
        }
        is XuiAccessoryType.RedDot -> {
          Row(verticalAlignment = Alignment.CenterVertically) {
            if (accessory.count != null && accessory.count > 0) {
              Box(
                modifier =
                  Modifier.clip(shapes.circle)
                    .background(colors.error)
                    .padding(horizontal = dimens.spacing7, vertical = dimens.spacing2),
                contentAlignment = Alignment.Center,
              ) {
                Text(
                  text =
                    if (accessory.count > motion.badgeOverflowThreshold) strings.common.badgeOverflow
                    else accessory.count.toString(),
                  style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = textSizes.labelSm,
                    fontWeight = FontWeight.Bold,
                  ),
                  color = colors.onAccentBadge,
                )
              }
            } else {
              Box(
                modifier = Modifier.size(dimens.redDotSize).clip(shapes.circle).background(colors.error)
              )
            }
            Spacer(modifier = Modifier.width(dimens.spacing4))
            Icon(
              imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
              contentDescription = strings.common.enterContentDescription,
              tint = colors.textPlaceholder,
              modifier = Modifier.size(dimens.iconMd),
            )
          }
        }
        is XuiAccessoryType.TagBadge -> {
          Surface(
            shape = shapes.badgeSmall,
            color =
              if (accessory.highlight) colors.brandContainer else colors.elevatedSurface,
          ) {
            Text(
              text = accessory.text,
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = textSizes.labelSm,
              ),
              color = if (accessory.highlight) colors.brandPrimary else colors.textSecondary,
              modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing3),
            )
          }
        }
        is XuiAccessoryType.Custom -> accessory.content()
      }
    }

    if (showBottomDivider) {
      XuiDivider(
        startIndent =
          if (leadingIcon != null) dimens.listDividerIconIndent else dimens.spacing16
      )
    }
  }
}
