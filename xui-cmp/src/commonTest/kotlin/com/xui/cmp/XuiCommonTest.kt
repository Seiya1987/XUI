package com.xui.cmp

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.xui.cmp.di.DefaultXuiComponentCatalog
import com.xui.cmp.di.DefaultXuiSkinManager
import com.xui.cmp.di.XuiComponentId
import com.xui.cmp.theme.XuiColorHelper
import com.xui.cmp.theme.XuiDisplayHelper
import com.xui.cmp.theme.XuiSkinPreset
import com.xui.cmp.theme.XuiSpanHelper
import com.xui.cmp.tokens.DefaultXuiResourceManager
import com.xui.cmp.tokens.XuiDensityPreset
import com.xui.cmp.tokens.XuiDimenTemplate
import com.xui.cmp.tokens.XuiPaletteTemplate
import com.xui.cmp.tokens.XuiStringReplacementRule
import com.xui.cmp.tokens.XuiTemplateFormatter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class XuiCommonTest {
  @Test
  fun catalog_contains_all_17_xui_components() {
    val catalog = DefaultXuiComponentCatalog()
    val all = catalog.getAllComponents()
    assertEquals(17, all.size)
    XuiComponentId.entries.forEach { id ->
      assertEquals(id, catalog.findById(id).id)
    }
  }

  @Test
  fun skin_and_helpers_work_in_common_code() {
    val skinManager = DefaultXuiSkinManager()
    skinManager.selectSkin(XuiSkinPreset.CORAL)
    assertEquals(XuiSkinPreset.CORAL, skinManager.state.value.activeSkin)
    assertEquals(1, skinManager.state.value.skinSwitchCount)

    assertEquals("#00A8E1", XuiColorHelper.toHexString(Color(0xFF00A8E1)))
    assertTrue(XuiDisplayHelper.isCompactLayout(400.dp))
    assertEquals(24f, XuiDisplayHelper.dpToPx(Density(2f), 12.dp))

    val span = XuiSpanHelper.buildHighlightedText("Hello XUI KMP", listOf("XUI"), Color.Blue)
    assertEquals(1, span.spanStyles.size)
  }

  @Test
  fun template_formatter_and_replacement_rules_apply_cleanly() {
    val rendered =
      XuiTemplateFormatter.format(
        "{brand} -> {count} items",
        "brand" to "XUI",
        "count" to 17,
      )
    assertEquals("XUI -> 17 items", rendered)

    val rules =
      listOf(
        XuiStringReplacementRule("r1", "Rule 1", "TopBar", "NavBar", enabled = true),
        XuiStringReplacementRule("r2", "Rule 2", "NavBar", "Header", enabled = false),
      )
    assertEquals("XUI NavBar", XuiTemplateFormatter.applyRules("XUI TopBar", rules))
  }

  @Test
  fun resource_manager_updates_brand_rules_and_density_templates() {
    val resourceManager = DefaultXuiResourceManager()
    val catalog = DefaultXuiComponentCatalog(resourceManager)

    resourceManager.selectDensity(XuiDensityPreset.COMPACT)
    assertEquals(XuiDensityPreset.COMPACT, resourceManager.state.value.densityPreset)

    val compactDimens = XuiDimenTemplate.buildDimens(XuiDensityPreset.COMPACT, roundedCards = true)
    val standardDimens = XuiDimenTemplate.buildDimens(XuiDensityPreset.STANDARD, roundedCards = true)
    val comfortableDimens =
      XuiDimenTemplate.buildDimens(XuiDensityPreset.COMFORTABLE, roundedCards = true)
    assertTrue(compactDimens.spacing16 < standardDimens.spacing16)
    assertTrue(standardDimens.spacing16 < comfortableDimens.spacing16)

    val lightAzure = XuiPaletteTemplate.buildColors(XuiSkinPreset.AZURE, isDark = false)
    val darkAzure = XuiPaletteTemplate.buildColors(XuiSkinPreset.AZURE, isDark = true)
    assertNotEquals(lightAzure.canvasBackground, darkAzure.canvasBackground)
    assertFalse(lightAzure.isDark)
    assertTrue(darkAzure.isDark)

    resourceManager.updateBrandToken("NovaUI")
    assertTrue(resourceManager.state.value.strings.common.appBrand.contains("NovaUI"))
    assertTrue(catalog.findById(XuiComponentId.TOP_BAR).name.contains("NovaUI"))

    val firstRuleId = resourceManager.state.value.rules.first().id
    resourceManager.toggleRule(firstRuleId)
    assertTrue(resourceManager.state.value.rules.first().enabled)

    resourceManager.resetAll()
    assertEquals("XUI", resourceManager.state.value.brandToken)
    assertEquals(XuiDensityPreset.STANDARD, resourceManager.state.value.densityPreset)
  }
}
