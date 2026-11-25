package com.example

import androidx.compose.ui.graphics.Color
import com.xui.cmp.di.DefaultXuiComponentCatalog
import com.xui.cmp.di.DefaultXuiDialogController
import com.xui.cmp.di.DefaultXuiSkinManager
import com.xui.cmp.di.DefaultXuiTipController
import com.xui.cmp.di.XuiActiveDialog
import com.xui.cmp.di.XuiComponentCatalog
import com.xui.cmp.di.XuiComponentCategory
import com.xui.cmp.di.XuiComponentId
import com.xui.cmp.di.XuiDialogController
import com.xui.cmp.di.XuiSkinManager
import com.xui.cmp.di.XuiTipController
import com.xui.cmp.di.xuiCoreModule
import com.xui.cmp.di.xuiShowcaseModule
import com.xui.cmp.platform.PlatformBridge
import com.xui.cmp.platform.XuiHapticType
import com.xui.cmp.showcase.ArchitectureLabViewModel
import com.xui.cmp.showcase.ComponentsLabViewModel
import com.xui.cmp.showcase.HelpersLabViewModel
import com.xui.cmp.theme.XuiColorHelper
import com.xui.cmp.theme.XuiDisplayHelper
import com.xui.cmp.theme.XuiSkinPreset
import com.xui.cmp.theme.XuiSpanHelper
import com.xui.cmp.theme.XuiThemeMode
import com.xui.cmp.tokens.XuiDensityPreset
import com.xui.cmp.tokens.XuiDimenTemplate
import com.xui.cmp.tokens.XuiLinkPatterns
import com.xui.cmp.tokens.XuiPaletteTemplate
import com.xui.cmp.tokens.XuiResourceManager
import com.xui.cmp.tokens.XuiTemplateFormatter
import com.xui.cmp.ui.components.XuiRadiusShapeMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class ExampleUnitTest {

  private class FakePlatformBridge : PlatformBridge {
    override val platformName: String = "KMP Test"
    override val platformTag: String = "commonTest"
    override val osDetail: String = "JVM Unit Test"
    override val architectureNote: String = "Decoupled Koin Test"
    override val isIos: Boolean = false
    var lastHaptic: XuiHapticType? = null
    var lastClipboard: Pair<String, String>? = null

    override fun triggerHaptic(type: XuiHapticType) {
      lastHaptic = type
    }

    override fun copyToClipboard(label: String, text: String): Boolean {
      lastClipboard = label to text
      return true
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @org.junit.Before
  fun setUp() {
    Dispatchers.setMain(Dispatchers.Unconfined)
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @After
  fun tearDown() {
    stopKoin()
    Dispatchers.resetMain()
  }

  @Test
  fun koin_modules_resolve_and_decouple_xui_controllers() {
    val fakeBridge = FakePlatformBridge()
    val koin =
      startKoin {
        modules(
          xuiCoreModule,
          xuiShowcaseModule,
          module {
            single<XuiTipController> {
              DefaultXuiTipController(resourceManager = get()) { Dispatchers.Unconfined }
            }
            single<PlatformBridge> { fakeBridge }
          },
        )
      }.koin

    val skinManager = koin.get<XuiSkinManager>()
    val tipController = koin.get<XuiTipController>()
    val dialogController = koin.get<XuiDialogController>()
    val catalog = koin.get<XuiComponentCatalog>()

    assertNotNull(tipController)
    assertEquals(17, catalog.getAllComponents().size)

    skinManager.selectSkin(XuiSkinPreset.EMERALD)
    skinManager.setThemeMode(XuiThemeMode.DARK)
    assertEquals(XuiSkinPreset.EMERALD, skinManager.state.value.activeSkin)
    assertEquals(XuiThemeMode.DARK, skinManager.state.value.themeMode)
    assertEquals(1, skinManager.state.value.skinSwitchCount)

    dialogController.showDialog(
      XuiActiveDialog.Message(title = "Test", message = "Hello KMP")
    )
    assertNotNull(dialogController.activeDialog.value)
    dialogController.dismissDialog()
    assertNull(dialogController.activeDialog.value)
  }

  @Test
  fun viewmodels_handle_all_page_and_component_transitions() {
    val fakeBridge = FakePlatformBridge()
    val koin =
      startKoin {
        modules(
          xuiCoreModule,
          xuiShowcaseModule,
          module {
            single<XuiTipController> {
              DefaultXuiTipController(resourceManager = get()) { Dispatchers.Unconfined }
            }
            single<PlatformBridge> { fakeBridge }
          },
        )
      }.koin

    val componentsVm = koin.get<ComponentsLabViewModel>()
    val architectureVm = koin.get<ArchitectureLabViewModel>()
    val helpersVm = koin.get<HelpersLabViewModel>()

    XuiComponentCategory.entries.forEach { category ->
      componentsVm.selectCategory(category)
      assertEquals(category, componentsVm.uiState.value.selectedCategory)
    }

    XuiComponentId.entries.forEach { componentId ->
      componentsVm.openComponentDetail(componentId)
      assertEquals(componentId, componentsVm.uiState.value.activeDetailId)
      componentsVm.closeComponentDetail()
      assertNull(componentsVm.uiState.value.activeDetailId)
    }

    XuiSkinPreset.entries.forEach { preset ->
      architectureVm.selectSkin(preset)
      assertEquals(preset, architectureVm.skinManager.state.value.activeSkin)
    }

    val firstSwipeId = componentsVm.uiState.value.swipeDemoItems.first().id
    componentsVm.toggleSwipeItemPin(firstSwipeId)
    assertTrue(componentsVm.uiState.value.swipeDemoItems.first().pinned)
    componentsVm.deleteSwipeItem(firstSwipeId)
    assertEquals(2, componentsVm.uiState.value.swipeDemoItems.size)
    componentsVm.resetSwipeItems()
    assertEquals(3, componentsVm.uiState.value.swipeDemoItems.size)

    helpersVm.setImageShape(XuiRadiusShapeMode.OVAL)
    assertEquals(XuiRadiusShapeMode.OVAL, helpersVm.uiState.value.imageShapeMode)
    helpersVm.onLinkTapped("https://xui-kmp.example.com")
    assertEquals("https://xui-kmp.example.com", fakeBridge.lastClipboard?.second)
  }

  @Test
  fun link_regex_parses_urls_emails_and_phones() {
    val linkPattern = Regex(XuiLinkPatterns.DETECTABLE_LINK_REGEX)
    val sample =
      "组件库文档：https://xui-kmp.example.com ，反馈邮箱 xui-kmp@example.com ，技术支持电话 +86-0755-86013388"
    val matches = linkPattern.findAll(sample).map { it.value }.toList()
    assertTrue(matches.contains("https://xui-kmp.example.com"))
    assertTrue(matches.contains("xui-kmp@example.com"))
    assertTrue(matches.contains("+86-0755-86013388"))
  }

  @Test
  fun architecture_template_workshop_updates_density_brand_and_rules_reactively() {
    val fakeBridge = FakePlatformBridge()
    val koin =
      startKoin {
        modules(
          xuiCoreModule,
          xuiShowcaseModule,
          module {
            single<XuiTipController> {
              DefaultXuiTipController(resourceManager = get()) { Dispatchers.Unconfined }
            }
            single<PlatformBridge> { fakeBridge }
          },
        )
      }.koin

    val resourceManager = koin.get<XuiResourceManager>()
    val componentsVm = koin.get<ComponentsLabViewModel>()
    val architectureVm = koin.get<ArchitectureLabViewModel>()

    architectureVm.selectDensity(XuiDensityPreset.COMFORTABLE)
    assertEquals(XuiDensityPreset.COMFORTABLE, resourceManager.state.value.densityPreset)

    val compactDimens = XuiDimenTemplate.buildDimens(XuiDensityPreset.COMPACT, roundedCards = true)
    val comfortableDimens =
      XuiDimenTemplate.buildDimens(XuiDensityPreset.COMFORTABLE, roundedCards = true)
    assertTrue(compactDimens.topBarHeight < comfortableDimens.topBarHeight)

    val lightPalette = XuiPaletteTemplate.buildColors(XuiSkinPreset.INDIGO, isDark = false)
    val darkPalette = XuiPaletteTemplate.buildColors(XuiSkinPreset.INDIGO, isDark = true)
    assertTrue(darkPalette.isDark && !lightPalette.isDark)

    architectureVm.applyBrandToken("AeroUI")
    assertTrue(
      componentsVm.uiState.value.components
        .first { it.id == XuiComponentId.ROUND_BUTTON }
        .name
        .contains("AeroUI")
    )

    val firstRuleId = resourceManager.state.value.rules.first().id
    architectureVm.toggleReplacementRule(firstRuleId)
    assertTrue(resourceManager.state.value.rules.first().enabled)

    architectureVm.resetTemplateWorkshop()
    assertEquals("XUI", resourceManager.state.value.brandToken)
    assertEquals(XuiDensityPreset.STANDARD, resourceManager.state.value.densityPreset)
    assertEquals("XUI -> 17", XuiTemplateFormatter.format("{b} -> {n}", "b" to "XUI", "n" to 17))
  }

  @Test
  fun xui_color_and_display_and_span_helpers_work_accurately() {
    assertEquals("#00A8E1", XuiColorHelper.toHexString(Color(0xFF00A8E1)))
    val mid = XuiColorHelper.computeColor(Color.Black, Color.White, 0.5f)
    assertEquals(0.5f, mid.red, 0.01f)
    assertEquals(0.5f, mid.green, 0.01f)
    assertEquals(0.5f, mid.blue, 0.01f)

    val density = Density(2f)
    assertEquals(32, XuiDisplayHelper.dpToPxInt(density, 16.dp))
    assertTrue(XuiDisplayHelper.isExpandedLayout(640.dp))
    assertEquals("75%", XuiDisplayHelper.formatRatioPercent(0.75f))

    val highlighted =
      XuiSpanHelper.buildHighlightedText(
        source = "XUI KMP + Compose Multiplatform",
        highlights = listOf("XUI", "KMP"),
        highlightColor = Color(0xFF00A8E1),
      )
    assertEquals(2, highlighted.spanStyles.size)
  }

  @Test
  fun default_controllers_work_standalone() {
    val catalog = DefaultXuiComponentCatalog()
    val skin = DefaultXuiSkinManager()
    val dialogs = DefaultXuiDialogController()
    val tips = DefaultXuiTipController()

    assertEquals(17, catalog.getAllComponents().size)
    assertEquals(XuiSkinPreset.AZURE, skin.state.value.activeSkin)
    assertNull(dialogs.activeDialog.value)
    assertNull(tips.tipState.value)
  }
}
