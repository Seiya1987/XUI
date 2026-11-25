package com.xui.cmp.showcase

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SmartButton
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Tab
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VerticalAlignTop
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.xui.cmp.di.XuiComponentCategory
import com.xui.cmp.di.XuiComponentId
import com.xui.cmp.di.XuiSkinState
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiTemplateFormatter
import com.xui.cmp.tokens.labelFor
import com.xui.cmp.ui.components.XuiAccessoryType
import com.xui.cmp.ui.components.XuiCommonListItemView
import com.xui.cmp.ui.components.XuiEmptyState
import com.xui.cmp.ui.components.XuiEmptyView
import com.xui.cmp.ui.components.XuiFloatLayout
import com.xui.cmp.ui.components.XuiGroupListSection
import com.xui.cmp.ui.components.XuiTopBar
import com.xui.cmp.ui.components.XuiTopBarIconAction

@Composable
fun ComponentsCatalogScreen(
  uiState: ComponentsLabUiState,
  skinState: XuiSkinState,
  platformName: String,
  onSelectCategory: (XuiComponentCategory) -> Unit,
  onSearchChange: (String) -> Unit,
  onOpenComponent: (XuiComponentId) -> Unit,
  onQuickThemeAction: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val catalogStrings = strings.catalog

  Column(modifier = modifier.fillMaxSize().background(colors.canvasBackground)) {
    XuiTopBar(
      title = catalogStrings.topBarTitle,
      subtitle = catalogStrings.topBarSubtitle,
      centerTitle = false,
      rightActions = {
        XuiTopBarIconAction(
          icon = Icons.Default.Layers,
          contentDescription = catalogStrings.quickThemeActionDesc,
          onClick = onQuickThemeAction,
        )
      },
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
      LazyColumn(
        modifier = Modifier.fillMaxSize().widthIn(max = dimens.contentMaxWidth),
        contentPadding =
          PaddingValues(horizontal = dimens.horizontalPadding, vertical = dimens.spacing16),
        verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
      ) {
        item {
          XuiHeroBanner(
            skinName = skinState.activeSkin.labelFor(strings),
            platformName = platformName,
            totalComponents = XuiComponentId.entries.size,
          )
        }

        item {
          OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchChange,
            singleLine = true,
            placeholder = {
              Text(
                text = catalogStrings.searchPlaceholder,
                color = colors.textPlaceholder,
                fontSize = textSizes.bodyMd,
              )
            },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = colors.textPlaceholder,
              )
            },
            trailingIcon = {
              if (uiState.searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchChange("") }) {
                  Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = strings.common.clearSearchContentDescription,
                    tint = colors.textPlaceholder,
                  )
                }
              }
            },
            shape = shapes.card,
            colors =
              OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.cardSurface,
                unfocusedContainerColor = colors.cardSurface,
                focusedBorderColor = colors.brandPrimary,
                unfocusedBorderColor = colors.separatorHairline,
                focusedTextColor = colors.titlePrimary,
                unfocusedTextColor = colors.titlePrimary,
              ),
            modifier = Modifier.fillMaxWidth().testTag("components_search_input"),
          )
        }

        item {
          XuiFloatLayout(
            horizontalSpacing = dimens.spacing8,
            verticalSpacing = dimens.spacing8,
            modifier = Modifier.fillMaxWidth(),
          ) {
            XuiComponentCategory.entries.forEach { category ->
              val selected = category == uiState.selectedCategory
              Surface(
                shape = shapes.buttonPill,
                color = if (selected) colors.brandPrimary else colors.cardSurface,
                modifier =
                  Modifier.clip(shapes.buttonPill)
                    .clickable { onSelectCategory(category) }
                    .testTag("category_chip_${category.name}"),
              ) {
                Text(
                  text = category.labelFor(strings),
                  style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = textSizes.labelMd,
                  ),
                  color = if (selected) colors.onBrandPrimary else colors.textSecondary,
                  modifier = Modifier.padding(horizontal = dimens.spacing14, vertical = dimens.spacing8),
                )
              }
            }
          }
        }

        if (uiState.components.isEmpty()) {
          item {
            XuiEmptyView(
              state = XuiEmptyState.EMPTY,
              title = catalogStrings.emptyTitle,
              detail =
                XuiTemplateFormatter.format(
                  catalogStrings.emptyDetailTemplate,
                  "count" to XuiComponentId.entries.size,
                ),
              actionText = catalogStrings.emptyResetButton,
              onActionClick = {
                onSearchChange("")
                onSelectCategory(XuiComponentCategory.ALL)
              },
            )
          }
        } else {
          item {
            XuiGroupListSection(
              title =
                XuiTemplateFormatter.format(
                  catalogStrings.sectionTitleTemplate,
                  "category" to uiState.selectedCategory.labelFor(strings),
                  "count" to uiState.components.size,
                ),
              footer = catalogStrings.sectionFooter,
            ) {
              uiState.components.forEachIndexed { index, spec ->
                XuiCommonListItemView(
                  title = spec.name,
                  detailText =
                    XuiTemplateFormatter.format(
                      catalogStrings.detailMappingTemplate,
                      "legacy" to spec.legacyAndroidClass,
                      "cmp" to spec.cmpComposableName,
                    ),
                  verticalDetail = true,
                  leadingIcon = iconForComponent(spec.id),
                  leadingIconTint = accentColorForCategory(spec.category),
                  accessory = XuiAccessoryType.Chevron,
                  showBottomDivider = index < uiState.components.lastIndex,
                  testTag = "component_row_${spec.id.name}",
                  onClick = { onOpenComponent(spec.id) },
                )
              }
            }
          }
        }

        item { Spacer(modifier = Modifier.height(dimens.spacing16)) }
      }
    }
  }
}

@Composable
private fun XuiHeroBanner(
  skinName: String,
  platformName: String,
  totalComponents: Int,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val catalogStrings = strings.catalog

  Box(
    modifier =
      Modifier.fillMaxWidth()
        .clip(shapes.cardLarge)
        .background(
          Brush.linearGradient(
            listOf(colors.brandPrimary, colors.brandPrimaryPressed, colors.secondaryAccent)
          )
        )
        .padding(dimens.spacing20),
  ) {
    Canvas(modifier = Modifier.matchParentSize()) {
      drawCircle(
        color = colors.heroOverlayStrong,
        radius = size.minDimension * 0.55f,
        center = Offset(size.width * 0.92f, size.height * 0.15f),
      )
      drawCircle(
        color = colors.heroOverlaySoft,
        radius = size.minDimension * 0.36f,
        center = Offset(size.width * 0.72f, size.height * 0.92f),
      )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dimens.spacing8),
      ) {
        HeroPillBadge(strings.common.brandTagPlatform)
        HeroPillBadge(strings.common.brandTagKoin)
        HeroPillBadge(skinName)
      }
      Spacer(modifier = Modifier.height(dimens.spacing14))
      Text(
        text = catalogStrings.heroTitle,
        style = MaterialTheme.typography.headlineSmall.copy(
          fontWeight = FontWeight.Bold,
          fontSize = textSizes.heroTitle,
        ),
        color = colors.heroText,
      )
      Spacer(modifier = Modifier.height(dimens.spacing6))
      Text(
        text =
          XuiTemplateFormatter.format(
            catalogStrings.heroSubtitleTemplate,
            "platform" to platformName,
          ),
        style = MaterialTheme.typography.bodySmall.copy(
          fontSize = textSizes.bodySm,
          lineHeight = textSizes.lineHeightBodyCompact,
        ),
        color = colors.heroTextMuted,
      )
      Spacer(modifier = Modifier.height(dimens.spacing14))
      Row(horizontalArrangement = Arrangement.spacedBy(dimens.spacing16)) {
        HeroMetric("$totalComponents", catalogStrings.heroMetricComponentsLabel)
        HeroMetric(catalogStrings.heroMetricReuseValue, catalogStrings.heroMetricReuseLabel)
        HeroMetric(catalogStrings.heroMetricCouplingValue, catalogStrings.heroMetricCouplingLabel)
      }
    }
  }
}

@Composable
private fun HeroPillBadge(text: String) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  Surface(
    shape = shapes.buttonPill,
    color = colors.heroBadgeBackground,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.SemiBold,
        fontSize = textSizes.labelSm,
      ),
      color = colors.heroText,
      modifier = Modifier.padding(horizontal = dimens.spacing10, vertical = dimens.spacing4),
    )
  }
}

@Composable
private fun HeroMetric(value: String, label: String) {
  val colors = XuiThemeTokens.colors
  val textSizes = XuiThemeTokens.textSizes
  Column {
    Text(
      text = value,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = textSizes.titleLg,
      ),
      color = colors.heroText,
    )
    Text(
      text = label,
      style = MaterialTheme.typography.labelSmall.copy(fontSize = textSizes.labelSm),
      color = colors.heroTextSubtle,
    )
  }
}

private fun iconForComponent(id: XuiComponentId): ImageVector =
  when (id) {
    XuiComponentId.TOP_BAR -> Icons.Default.VerticalAlignTop
    XuiComponentId.COLLAPSING_TOP_BAR -> Icons.Default.AspectRatio
    XuiComponentId.ROUND_BUTTON -> Icons.Default.SmartButton
    XuiComponentId.GROUP_LIST_VIEW -> Icons.AutoMirrored.Filled.ViewList
    XuiComponentId.DIALOG -> Icons.Default.CheckBox
    XuiComponentId.TIP_DIALOG -> Icons.Default.NotificationsActive
    XuiComponentId.BOTTOM_SHEET -> Icons.AutoMirrored.Filled.MenuOpen
    XuiComponentId.TAB_SEGMENT -> Icons.Default.Tab
    XuiComponentId.POPUP -> Icons.Default.AddCircleOutline
    XuiComponentId.EMPTY_VIEW -> Icons.Default.HourglassEmpty
    XuiComponentId.FLOAT_LAYOUT -> Icons.Default.DashboardCustomize
    XuiComponentId.PULL_REFRESH -> Icons.Default.Refresh
    XuiComponentId.SLIDER -> Icons.Default.Tune
    XuiComponentId.STICKY_SECTION -> Icons.Default.ViewAgenda
    XuiComponentId.SWIPE_ACTION -> Icons.Default.SwapHoriz
    XuiComponentId.SWIPE_BACK -> Icons.AutoMirrored.Filled.KeyboardReturn
    XuiComponentId.PRIORITY_LAYOUT -> Icons.Default.ViewWeek
  }

@Composable
private fun accentColorForCategory(category: XuiComponentCategory): Color {
  val colors = XuiThemeTokens.colors
  return when (category) {
    XuiComponentCategory.NAVIGATION -> colors.brandPrimary
    XuiComponentCategory.BUTTONS -> colors.success
    XuiComponentCategory.FEEDBACK -> colors.warning
    XuiComponentCategory.LISTS -> colors.secondaryAccent
    XuiComponentCategory.ALL -> colors.brandPrimary
  }
}
