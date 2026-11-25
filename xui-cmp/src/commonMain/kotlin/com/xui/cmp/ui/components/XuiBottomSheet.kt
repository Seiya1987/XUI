package com.xui.cmp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xui.cmp.di.XuiActiveBottomSheet
import com.xui.cmp.di.XuiDialogController
import com.xui.cmp.di.XuiSheetGridItem
import com.xui.cmp.theme.XuiThemeTokens
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XuiBottomSheetHost(
  dialogController: XuiDialogController = koinInject(),
) {
  val activeSheet by dialogController.activeBottomSheet.collectAsStateWithLifecycle()
  val colors = XuiThemeTokens.colors
  val shapes = XuiThemeTokens.shapes

  activeSheet?.let { sheet ->
    key(sheet) {
      val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
      ModalBottomSheet(
        onDismissRequest = { dialogController.dismissBottomSheet() },
        sheetState = sheetState,
        containerColor = colors.cardSurface,
        contentColor = colors.titlePrimary,
        shape = shapes.sheetTop,
        dragHandle = null,
      ) {
        Column(modifier = Modifier.fillMaxWidth().navigationBarsPadding()) {
          when (sheet) {
            is XuiActiveBottomSheet.ListSheet -> {
              XuiBottomListSheetContent(
                sheet = sheet,
                onDismiss = { dialogController.dismissBottomSheet() },
              )
            }
            is XuiActiveBottomSheet.GridSheet -> {
              XuiBottomGridSheetContent(
                sheet = sheet,
                onDismiss = { dialogController.dismissBottomSheet() },
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun XuiBottomListSheetContent(
  sheet: XuiActiveBottomSheet.ListSheet,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings

  Column(modifier = Modifier.fillMaxWidth()) {
    if (!sheet.title.isNullOrBlank()) {
      Text(
        text = sheet.title,
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = textSizes.labelMd,
        ),
        color = colors.textSecondary,
        textAlign = TextAlign.Center,
        modifier =
          Modifier.fillMaxWidth().padding(horizontal = dimens.spacing20, vertical = dimens.spacing14),
      )
      XuiDivider()
    }

    sheet.items.forEachIndexed { index, item ->
      val selected = sheet.selectedIndex == index
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .defaultMinSize(minHeight = dimens.sheetRowMinHeight)
            .clickable {
              onDismiss()
              sheet.onSelect(index)
            }
            .padding(horizontal = dimens.spacing20, vertical = dimens.spacing12)
            .testTag("xui_sheet_list_item_$index"),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = item.title,
              style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                fontSize = textSizes.bodyLg,
              ),
              color =
                when {
                  item.isDestructive -> colors.error
                  selected -> colors.brandPrimary
                  else -> colors.titlePrimary
                },
            )
            if (item.showRedDot) {
              Spacer(modifier = Modifier.width(dimens.spacing6))
              Box(modifier = Modifier.size(dimens.badgeDotSize).clip(shapes.circle).background(colors.error))
            }
            if (!item.badge.isNullOrBlank()) {
              Spacer(modifier = Modifier.width(dimens.spacing8))
              Surface(
                shape = shapes.badgeSmall,
                color = colors.brandContainer,
              ) {
                Text(
                  text = item.badge,
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = textSizes.micro),
                  color = colors.brandPrimary,
                  modifier = Modifier.padding(horizontal = dimens.spacing6, vertical = dimens.spacing2),
                )
              }
            }
          }
          if (!item.subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(dimens.spacing2))
            Text(
              text = item.subtitle,
              style = MaterialTheme.typography.bodySmall,
              color = colors.textSecondary,
            )
          }
        }
        if (selected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = strings.common.selectedContentDescription,
            tint = colors.brandPrimary,
            modifier = Modifier.size(dimens.iconMd),
          )
        }
      }
      if (index < sheet.items.lastIndex) {
        XuiDivider(startIndent = dimens.spacing20)
      }
    }

    Box(modifier = Modifier.fillMaxWidth().height(dimens.spacing8).background(colors.canvasBackground))
    Box(
      modifier =
        Modifier.fillMaxWidth()
          .defaultMinSize(minHeight = dimens.sheetRowMinHeight)
          .clickable(onClick = onDismiss)
          .testTag("xui_sheet_cancel"),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = strings.common.cancel,
        style = MaterialTheme.typography.bodyLarge.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = textSizes.bodyLg,
        ),
        color = colors.textSecondary,
      )
    }
  }
}

@Composable
private fun XuiBottomGridSheetContent(
  sheet: XuiActiveBottomSheet.GridSheet,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val rows = sheet.items.chunked(4)

  Column(modifier = Modifier.fillMaxWidth()) {
    if (!sheet.title.isNullOrBlank()) {
      Text(
        text = sheet.title,
        style = MaterialTheme.typography.labelLarge.copy(
          fontWeight = FontWeight.SemiBold,
          fontSize = textSizes.labelMd,
        ),
        color = colors.textSecondary,
        textAlign = TextAlign.Center,
        modifier =
          Modifier.fillMaxWidth().padding(horizontal = dimens.spacing20, vertical = dimens.spacing14),
      )
      XuiDivider()
    }

    Column(
      modifier = Modifier.fillMaxWidth().padding(horizontal = dimens.spacing12, vertical = dimens.spacing16),
      verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
    ) {
      rows.forEach { rowItems ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Start,
        ) {
          rowItems.forEach { item ->
            XuiGridSheetCell(
              item = item,
              modifier = Modifier.weight(1f),
              onClick = {
                onDismiss()
                sheet.onSelect(item)
              },
            )
          }
          repeat(4 - rowItems.size) {
            Spacer(modifier = Modifier.weight(1f))
          }
        }
      }
    }

    XuiDivider()
    Box(
      modifier =
        Modifier.fillMaxWidth()
          .defaultMinSize(minHeight = dimens.sheetRowMinHeight)
          .clickable(onClick = onDismiss),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = strings.common.cancel,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        color = colors.textSecondary,
      )
    }
  }
}

@Composable
private fun XuiGridSheetCell(
  item: XuiSheetGridItem,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  Column(
    modifier =
      modifier
        .clip(shapes.popup)
        .clickable(onClick = onClick)
        .padding(vertical = dimens.spacing8, horizontal = dimens.spacing4)
        .testTag("xui_sheet_grid_${item.id}"),
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Surface(
      modifier = Modifier.size(dimens.iconBoxLg),
      shape = shapes.dialog,
      color = colors.elevatedSurface,
    ) {
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = resolveSheetIcon(item.iconKey),
          contentDescription = item.title,
          tint = colors.brandPrimary,
          modifier = Modifier.size(dimens.iconLg),
        )
      }
    }
    Spacer(modifier = Modifier.height(dimens.spacing6))
    Text(
      text = item.title,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = textSizes.labelSm),
      color = colors.textSecondary,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.Center,
    )
  }
}

private fun resolveSheetIcon(iconKey: String): ImageVector =
  when (iconKey) {
    "share" -> Icons.Default.Share
    "copy" -> Icons.Default.ContentCopy
    "link" -> Icons.Default.Link
    "code" -> Icons.Default.Code
    "theme" -> Icons.Default.Palette
    "bookmark" -> Icons.Default.Bookmark
    "download" -> Icons.Default.Download
    else -> Icons.Default.Terminal
  }
