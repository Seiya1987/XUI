package com.xui.cmp.showcase

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RoundedCorner
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import com.xui.cmp.di.XuiSkinState
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiDensityPreset
import com.xui.cmp.tokens.XuiResourceState
import com.xui.cmp.tokens.XuiSkinPreset
import com.xui.cmp.tokens.XuiTemplateFormatter
import com.xui.cmp.tokens.XuiThemeMode
import com.xui.cmp.tokens.descFor
import com.xui.cmp.tokens.labelFor
import com.xui.cmp.tokens.shortLabelFor
import com.xui.cmp.ui.components.XuiAccessoryType
import com.xui.cmp.ui.components.XuiButtonStyle
import com.xui.cmp.ui.components.XuiCommonListItemView
import com.xui.cmp.ui.components.XuiDivider
import com.xui.cmp.ui.components.XuiFloatLayout
import com.xui.cmp.ui.components.XuiGroupListSection
import com.xui.cmp.ui.components.XuiRoundButton
import com.xui.cmp.ui.components.XuiTopBar
import kotlin.math.roundToInt

@Composable
fun ArchitectureKoinScreen(
  uiState: ArchitectureLabUiState,
  skinState: XuiSkinState,
  resourceState: XuiResourceState,
  onSelectSkin: (XuiSkinPreset) -> Unit,
  onSelectThemeMode: (XuiThemeMode) -> Unit,
  onToggleRoundedCards: (Boolean) -> Unit,
  onSelectDensity: (XuiDensityPreset) -> Unit,
  onApplyBrandToken: (String) -> Unit,
  onToggleRule: (String) -> Unit,
  onResetRules: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val arch = strings.architecture

  var brandDraft by remember { mutableStateOf(resourceState.brandToken) }
  LaunchedEffect(resourceState.brandToken) {
    brandDraft = resourceState.brandToken
  }

  Column(modifier = modifier.fillMaxSize().background(colors.canvasBackground)) {
    XuiTopBar(
      title = arch.topBarTitle,
      subtitle = arch.topBarSubtitle,
      centerTitle = false,
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier =
                    Modifier.size(dimens.iconBoxHero)
                      .clip(shapes.iconContainer)
                      .background(colors.brandContainer),
                  contentAlignment = Alignment.Center,
                ) {
                  Icon(
                    imageVector = Icons.Default.Devices,
                    contentDescription = null,
                    tint = colors.brandPrimary,
                  )
                }
                Spacer(modifier = Modifier.width(dimens.spacing12))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text =
                      XuiTemplateFormatter.format(
                        arch.platformTitleTemplate,
                        "platform" to uiState.platformName,
                      ),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = colors.titlePrimary,
                  )
                  Text(
                    text = uiState.osDetail,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                  )
                }
                Surface(
                  shape = shapes.badge,
                  color = colors.success.copy(alpha = 0.14f),
                ) {
                  Text(
                    text = arch.koinActiveBadge,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.success,
                    modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
                  )
                }
              }
              Spacer(modifier = Modifier.height(dimens.spacing10))
              Text(
                text = uiState.architectureNote,
                style = MaterialTheme.typography.bodySmall,
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
                text =
                  XuiTemplateFormatter.format(
                    arch.skinWorkshopTitleTemplate,
                    "count" to skinState.skinSwitchCount,
                  ),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing4))
              Text(
                text = arch.skinWorkshopDesc,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing14))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(dimens.spacing10),
              ) {
                XuiSkinPreset.entries.forEach { preset ->
                  val selected = preset == skinState.activeSkin
                  Column(
                    modifier =
                      Modifier.weight(1f)
                        .clip(shapes.popup)
                        .background(
                          if (selected) colors.brandContainer else colors.elevatedSurface
                        )
                        .border(
                          width = if (selected) dimens.borderMedium else dimens.hairline,
                          color = if (selected) colors.brandPrimary else colors.separatorHairline,
                          shape = shapes.popup,
                        )
                        .clickable { onSelectSkin(preset) }
                        .padding(vertical = dimens.spacing10, horizontal = dimens.spacing6)
                        .testTag("skin_preset_${preset.id}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                  ) {
                    Box(
                      modifier =
                        Modifier.size(dimens.iconBoxSm)
                          .clip(shapes.circle)
                          .background(if (colors.isDark) preset.primaryDark else preset.primaryLight),
                      contentAlignment = Alignment.Center,
                    ) {
                      if (selected) {
                        Icon(
                          imageVector = Icons.Default.Check,
                          contentDescription = strings.common.selectedContentDescription,
                          tint = colors.onAccentBadge,
                          modifier = Modifier.size(dimens.iconSm),
                        )
                      }
                    }
                    Spacer(modifier = Modifier.height(dimens.spacing6))
                    Text(
                      text = preset.shortLabelFor(strings),
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                        fontSize = textSizes.labelSm,
                      ),
                      color = if (selected) colors.brandPrimary else colors.titlePrimary,
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(dimens.spacing14))
              XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
                XuiThemeMode.entries.forEach { mode ->
                  XuiRoundButton(
                    text = mode.labelFor(strings),
                    style =
                      if (skinState.themeMode == mode) XuiButtonStyle.FILLED
                      else XuiButtonStyle.OUTLINED,
                    onClick = { onSelectThemeMode(mode) },
                  )
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.AutoFixHigh,
                  contentDescription = null,
                  tint = colors.brandPrimary,
                  modifier = Modifier.size(dimens.iconMd),
                )
                Spacer(modifier = Modifier.width(dimens.spacing8))
                Text(
                  text = arch.templateWorkshopTitle,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = colors.titlePrimary,
                )
              }
              Spacer(modifier = Modifier.height(dimens.spacing4))
              Text(
                text = arch.templateWorkshopDesc,
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing14))

              Text(
                text = arch.densitySelectorLabel,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing8))
              XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
                XuiDensityPreset.entries.forEach { preset ->
                  val selected = preset == resourceState.densityPreset
                  XuiRoundButton(
                    text = preset.labelFor(strings),
                    style = if (selected) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
                    modifier = Modifier.testTag("density_preset_${preset.id}"),
                    onClick = { onSelectDensity(preset) },
                  )
                }
              }
              Spacer(modifier = Modifier.height(dimens.spacing4))
              Text(
                text = resourceState.densityPreset.descFor(strings),
                style = MaterialTheme.typography.bodySmall,
                color = colors.brandPrimary,
              )

              Spacer(modifier = Modifier.height(dimens.spacing16))
              XuiDivider()
              Spacer(modifier = Modifier.height(dimens.spacing14))

              Text(
                text = arch.brandTemplateLabel,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing8))
              Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(dimens.spacing8),
              ) {
                OutlinedTextField(
                  value = brandDraft,
                  onValueChange = { brandDraft = it },
                  singleLine = true,
                  placeholder = {
                    Text(arch.brandInputPlaceholder, color = colors.textPlaceholder)
                  },
                  leadingIcon = {
                    Icon(
                      imageVector = Icons.Default.TextFields,
                      contentDescription = null,
                      tint = colors.brandPrimary,
                    )
                  },
                  shape = shapes.input,
                  colors =
                    OutlinedTextFieldDefaults.colors(
                      focusedBorderColor = colors.brandPrimary,
                      unfocusedBorderColor = colors.separatorHairline,
                      focusedTextColor = colors.titlePrimary,
                      unfocusedTextColor = colors.titlePrimary,
                    ),
                  modifier = Modifier.weight(1f).testTag("brand_token_input"),
                )
                XuiRoundButton(
                  text = arch.brandApplyButton,
                  style = XuiButtonStyle.FILLED,
                  modifier = Modifier.testTag("brand_token_apply"),
                  onClick = { onApplyBrandToken(brandDraft) },
                )
              }
              Spacer(modifier = Modifier.height(dimens.spacing8))
              XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
                arch.brandPresets.forEach { presetBrand ->
                  XuiRoundButton(
                    text = presetBrand,
                    style =
                      if (resourceState.brandToken == presetBrand) XuiButtonStyle.TONAL
                      else XuiButtonStyle.OUTLINED,
                    onClick = { onApplyBrandToken(presetBrand) },
                  )
                }
              }

              Spacer(modifier = Modifier.height(dimens.spacing16))
              XuiDivider()
              Spacer(modifier = Modifier.height(dimens.spacing14))

              Text(
                text = arch.rulesListLabel,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = colors.titlePrimary,
              )
              Spacer(modifier = Modifier.height(dimens.spacing8))
              resourceState.rules.forEachIndexed { index, rule ->
                Row(
                  modifier =
                    Modifier.fillMaxWidth()
                      .clickable { onToggleRule(rule.id) }
                      .padding(vertical = dimens.spacing8)
                      .testTag("rule_toggle_${rule.id}"),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                  Text(
                    text = rule.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.titlePrimary,
                    modifier = Modifier.weight(1f).padding(end = dimens.spacing12),
                  )
                  Switch(
                    checked = rule.enabled,
                    onCheckedChange = { onToggleRule(rule.id) },
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
                if (index < resourceState.rules.lastIndex) {
                  XuiDivider()
                }
              }

              Spacer(modifier = Modifier.height(dimens.spacing10))
              Surface(
                shape = shapes.iconContainer,
                color = colors.elevatedSurface,
                modifier = Modifier.fillMaxWidth(),
              ) {
                Text(
                  text =
                    XuiTemplateFormatter.format(
                      arch.rulePreviewTemplate,
                      "sample" to strings.catalog.heroTitle,
                    ),
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                  color = colors.brandPrimary,
                  modifier = Modifier.padding(dimens.spacing12),
                )
              }
              Spacer(modifier = Modifier.height(dimens.spacing10))
              XuiRoundButton(
                text = arch.resetRulesButton,
                style = XuiButtonStyle.OUTLINED,
                leadingIcon = Icons.Default.Refresh,
                onClick = onResetRules,
              )
            }
          }
        }

        item {
          XuiGroupListSection(
            title = arch.appearanceTitle,
            footer = arch.appearanceFooter,
          ) {
            val radiusDpValue = dimens.cardCornerRadius.value.roundToInt()
            XuiCommonListItemView(
              title = arch.roundedCardsTitle,
              detailText =
                if (skinState.roundedCards) {
                  XuiTemplateFormatter.format(
                    arch.roundedCardsOnDescTemplate,
                    "radius" to radiusDpValue,
                  )
                } else {
                  XuiTemplateFormatter.format(
                    arch.roundedCardsOffDescTemplate,
                    "radius" to radiusDpValue,
                  )
                },
              verticalDetail = true,
              leadingIcon = Icons.Default.RoundedCorner,
              accessory = XuiAccessoryType.SwitchToggle(skinState.roundedCards, onToggleRoundedCards),
            )
            XuiCommonListItemView(
              title = arch.forceDarkTitle,
              detailText =
                XuiTemplateFormatter.format(
                  arch.forceDarkDescTemplate,
                  "mode" to skinState.themeMode.labelFor(strings),
                ),
              verticalDetail = true,
              leadingIcon = Icons.Default.DarkMode,
              accessory =
                XuiAccessoryType.SwitchToggle(skinState.themeMode == XuiThemeMode.DARK) { isDark ->
                  onSelectThemeMode(if (isDark) XuiThemeMode.DARK else XuiThemeMode.LIGHT)
                },
              showBottomDivider = false,
            )
          }
        }

        item {
          XuiGroupListSection(
            title =
              XuiTemplateFormatter.format(
                arch.koinBindingsTitleTemplate,
                "count" to uiState.koinBindings.size,
              ),
            footer = arch.koinBindingsFooter,
          ) {
            uiState.koinBindings.forEachIndexed { index, binding ->
              XuiCommonListItemView(
                title = "${binding.interfaceName}${arch.bindingArrow}${binding.implementationName}",
                detailText = binding.purpose,
                verticalDetail = true,
                leadingIcon = Icons.Default.Hub,
                accessory = XuiAccessoryType.TagBadge(binding.scopeKind),
                showBottomDivider = index < uiState.koinBindings.lastIndex,
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.SwapHoriz,
                  contentDescription = null,
                  tint = colors.brandPrimary,
                  modifier = Modifier.size(dimens.iconMd),
                )
                Spacer(modifier = Modifier.width(dimens.spacing8))
                Text(
                  text = arch.migrationTitle,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = colors.titlePrimary,
                )
              }
              Spacer(modifier = Modifier.height(dimens.spacing12))

              uiState.migrationItems.forEachIndexed { index, item ->
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = dimens.spacing8)) {
                  Text(
                    text = "${item.legacyModule}${arch.migrationMappingArrow}${item.kmpModule}",
                    style = MaterialTheme.typography.labelLarge.copy(
                      fontWeight = FontWeight.SemiBold,
                      fontSize = textSizes.labelMd,
                    ),
                    color = colors.titlePrimary,
                  )
                  Spacer(modifier = Modifier.height(dimens.spacing3))
                  Text(
                    text = item.decouplingBenefit,
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                  )
                }
                if (index < uiState.migrationItems.lastIndex) {
                  XuiDivider()
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
