package com.xui.cmp.showcase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xui.cmp.di.XuiActiveBottomSheet
import com.xui.cmp.di.XuiActiveDialog
import com.xui.cmp.di.XuiComponentCatalog
import com.xui.cmp.di.XuiComponentCategory
import com.xui.cmp.di.XuiComponentId
import com.xui.cmp.di.XuiComponentSpec
import com.xui.cmp.di.XuiDialogController
import com.xui.cmp.di.XuiSheetGridItem
import com.xui.cmp.di.XuiSheetListItem
import com.xui.cmp.di.XuiSkinManager
import com.xui.cmp.di.XuiTipController
import com.xui.cmp.platform.PlatformBridge
import com.xui.cmp.platform.XuiHapticType
import com.xui.cmp.tokens.XuiCatalogTemplate
import com.xui.cmp.tokens.XuiDensityPreset
import com.xui.cmp.tokens.XuiMotionTokens
import com.xui.cmp.tokens.XuiResourceManager
import com.xui.cmp.tokens.XuiSkinPreset
import com.xui.cmp.tokens.XuiStrings
import com.xui.cmp.tokens.XuiTemplateFormatter
import com.xui.cmp.tokens.XuiThemeMode
import com.xui.cmp.tokens.labelFor
import com.xui.cmp.ui.components.XuiRadiusShapeMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class XuiSwipeDemoItem(
  val id: Int,
  val title: String,
  val subtitle: String,
  val pinned: Boolean = false,
)

data class ComponentsLabUiState(
  val selectedCategory: XuiComponentCategory = XuiComponentCategory.ALL,
  val searchQuery: String = "",
  val components: List<XuiComponentSpec> = emptyList(),
  val activeDetailId: XuiComponentId? = null,
  val isPullRefreshing: Boolean = false,
  val refreshCount: Int = 0,
  val floatTags: List<String> = emptyList(),
  val swipeDemoItems: List<XuiSwipeDemoItem> = emptyList(),
  val lastActionResult: String = "",
)

class ComponentsLabViewModel(
  private val catalog: XuiComponentCatalog,
  val skinManager: XuiSkinManager,
  val tipController: XuiTipController,
  val dialogController: XuiDialogController,
  val platformBridge: PlatformBridge,
  private val resourceManager: XuiResourceManager,
) : ViewModel() {

  private val motion = XuiMotionTokens()
  private var allSpecs: List<XuiComponentSpec> = catalog.getAllComponents()
  private var hasCustomFloatTags = false
  private var hasCustomSwipeItems = false
  private var hasCustomActionResult = false

  private val _uiState =
    MutableStateFlow(
      initialState(resourceManager.state.value.strings, allSpecs)
    )
  val uiState: StateFlow<ComponentsLabUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      resourceManager.state.collect { resState ->
        val updatedSpecs = XuiCatalogTemplate.buildSpecs(resState.strings)
        allSpecs = updatedSpecs
        val strings = resState.strings
        _uiState.update { current ->
          current.copy(
            components = filterSpecs(updatedSpecs, current.selectedCategory, current.searchQuery),
            floatTags =
              if (hasCustomFloatTags) current.floatTags else strings.catalog.defaultFloatTags,
            swipeDemoItems =
              if (hasCustomSwipeItems) current.swipeDemoItems else defaultSwipeItems(strings),
            lastActionResult =
              if (hasCustomActionResult) current.lastActionResult
              else strings.catalog.defaultActionHint,
          )
        }
      }
    }
  }

  private fun currentStrings(): XuiStrings = resourceManager.state.value.strings

  fun selectCategory(category: XuiComponentCategory) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    _uiState.update { state ->
      state.copy(
        selectedCategory = category,
        components = filterSpecs(allSpecs, category, state.searchQuery),
      )
    }
  }

  fun updateSearchQuery(query: String) {
    _uiState.update { state ->
      state.copy(
        searchQuery = query,
        components = filterSpecs(allSpecs, state.selectedCategory, query),
      )
    }
  }

  fun openComponentDetail(id: XuiComponentId) {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    val strings = currentStrings()
    val spec = catalog.findById(id)
    hasCustomActionResult = true
    _uiState.update {
      it.copy(
        activeDetailId = id,
        lastActionResult =
          XuiTemplateFormatter.format(strings.catalog.loadedLabTemplate, "name" to spec.name),
      )
    }
  }

  fun closeComponentDetail() {
    _uiState.update { it.copy(activeDetailId = null) }
  }

  fun triggerPullRefresh() {
    if (_uiState.value.isPullRefreshing) return
    platformBridge.triggerHaptic(XuiHapticType.MEDIUM_IMPACT)
    viewModelScope.launch {
      _uiState.update { it.copy(isPullRefreshing = true) }
      delay(motion.pullRefreshDelayMs)
      val strings = currentStrings()
      hasCustomActionResult = true
      _uiState.update {
        val nextCount = it.refreshCount + 1
        it.copy(
          isPullRefreshing = false,
          refreshCount = nextCount,
          lastActionResult =
            XuiTemplateFormatter.format(
              strings.catalog.pullRefreshResultTemplate,
              "count" to nextCount,
            ),
        )
      }
      tipController.showSuccess(strings.catalog.pullRefreshSuccessTip)
    }
  }

  fun addFloatTag(tag: String) {
    val trimmed = tag.trim()
    if (trimmed.isEmpty()) return
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    val strings = currentStrings()
    hasCustomFloatTags = true
    hasCustomActionResult = true
    _uiState.update { state ->
      if (trimmed in state.floatTags) state
      else {
        state.copy(
          floatTags = state.floatTags + trimmed,
          lastActionResult =
            XuiTemplateFormatter.format(
              strings.catalog.floatTagAddedTemplate,
              "tag" to trimmed,
            ),
        )
      }
    }
  }

  fun removeFloatTag(tag: String) {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    val strings = currentStrings()
    hasCustomFloatTags = true
    hasCustomActionResult = true
    _uiState.update { state ->
      state.copy(
        floatTags = state.floatTags - tag,
        lastActionResult =
          XuiTemplateFormatter.format(
            strings.catalog.floatTagRemovedTemplate,
            "tag" to tag,
          ),
      )
    }
  }

  fun toggleSwipeItemPin(id: Int) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    val strings = currentStrings()
    hasCustomSwipeItems = true
    hasCustomActionResult = true
    _uiState.update { state ->
      val updated =
        state.swipeDemoItems
          .map { if (it.id == id) it.copy(pinned = !it.pinned) else it }
          .sortedByDescending { it.pinned }
      state.copy(
        swipeDemoItems = updated,
        lastActionResult =
          XuiTemplateFormatter.format(
            strings.catalog.swipePinUpdatedTemplate,
            "id" to id,
          ),
      )
    }
  }

  fun deleteSwipeItem(id: Int) {
    platformBridge.triggerHaptic(XuiHapticType.MEDIUM_IMPACT)
    val strings = currentStrings()
    hasCustomSwipeItems = true
    hasCustomActionResult = true
    _uiState.update { state ->
      state.copy(
        swipeDemoItems = state.swipeDemoItems.filterNot { it.id == id },
        lastActionResult =
          XuiTemplateFormatter.format(
            strings.catalog.swipeDeletedTemplate,
            "id" to id,
          ),
      )
    }
  }

  fun resetSwipeItems() {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    val strings = currentStrings()
    hasCustomSwipeItems = false
    hasCustomActionResult = true
    _uiState.update { state ->
      state.copy(
        swipeDemoItems = defaultSwipeItems(strings),
        lastActionResult = strings.catalog.swipeResetDone,
      )
    }
  }

  fun recordActionResult(message: String) {
    hasCustomActionResult = true
    _uiState.update { it.copy(lastActionResult = message) }
  }

  fun copyCodeSnippet(spec: XuiComponentSpec) {
    val strings = currentStrings()
    val ok = platformBridge.copyToClipboard(spec.cmpComposableName, spec.sampleCode)
    platformBridge.triggerHaptic(XuiHapticType.SUCCESS)
    if (ok) {
      tipController.showSuccess(
        XuiTemplateFormatter.format(
          strings.common.copySuccessTemplate,
          "name" to spec.cmpComposableName,
        )
      )
    } else {
      tipController.showInfo(strings.common.copyPrepared)
    }
  }

  fun showDemoMessageDialog(destructive: Boolean = false) {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    val strings = currentStrings()
    val d = strings.dialogs
    dialogController.showDialog(
      XuiActiveDialog.Message(
        title = if (destructive) d.resetSkinTitle else d.normalDialogTitle,
        message = if (destructive) d.resetSkinMessage else d.normalDialogMessage,
        positiveText = if (destructive) d.confirmResetBtn else d.gotItBtn,
        negativeText = strings.common.cancel,
        destructive = destructive,
        onConfirm = {
          if (destructive) {
            skinManager.selectSkin(XuiSkinPreset.AZURE)
            skinManager.setThemeMode(XuiThemeMode.SYSTEM)
            skinManager.setRoundedCards(true)
            tipController.showSuccess(d.resetSkinDone)
          } else {
            recordActionResult(d.normalDialogConfirmed)
          }
        },
      )
    )
  }

  fun showDemoInputDialog() {
    val d = currentStrings().dialogs
    dialogController.showDialog(
      XuiActiveDialog.Input(
        title = d.inputDialogTitle,
        placeholder = d.inputDialogPlaceholder,
        initialValue = d.inputDialogInitial,
        confirmText = d.inputDialogConfirm,
        onConfirm = { value ->
          addFloatTag(value)
          tipController.showSuccess(
            XuiTemplateFormatter.format(d.inputDialogDoneTemplate, "value" to value)
          )
        },
      )
    )
  }

  fun showDemoSingleChoiceDialog() {
    val strings = currentStrings()
    val d = strings.dialogs
    val options = XuiSkinPreset.entries.map { it.labelFor(strings) }
    val current = XuiSkinPreset.entries.indexOf(skinManager.state.value.activeSkin)
    dialogController.showDialog(
      XuiActiveDialog.SingleChoice(
        title = d.singleChoiceTitle,
        options = options,
        selectedIndex = current,
        onSelect = { idx ->
          val preset = XuiSkinPreset.entries[idx]
          skinManager.selectSkin(preset)
          tipController.showSuccess(
            XuiTemplateFormatter.format(
              d.skinSwitchedTemplate,
              "skin" to preset.labelFor(strings),
            )
          )
        },
      )
    )
  }

  fun showDemoMultiChoiceDialog() {
    val d = currentStrings().dialogs
    dialogController.showDialog(
      XuiActiveDialog.MultiChoice(
        title = d.multiChoiceTitle,
        options = d.multiChoiceTargets,
        selectedIndices = setOf(0, 1, 3),
        onConfirm = { selected ->
          recordActionResult(
            XuiTemplateFormatter.format(
              d.multiChoiceRecordedTemplate,
              "count" to selected.size,
            )
          )
          tipController.showInfo(
            XuiTemplateFormatter.format(
              d.multiChoiceTipTemplate,
              "count" to selected.size,
            )
          )
        },
      )
    )
  }

  fun showDemoListBottomSheet() {
    val strings = currentStrings()
    val d = strings.dialogs
    dialogController.showBottomSheet(
      XuiActiveBottomSheet.ListSheet(
        title = d.listSheetTitle,
        items =
          listOf(
            XuiSheetListItem(
              title = XuiThemeMode.SYSTEM.labelFor(strings),
              subtitle = d.listSheetSystemDesc,
              badge = d.listSheetRecommendBadge,
            ),
            XuiSheetListItem(
              title = XuiThemeMode.LIGHT.labelFor(strings),
              subtitle = d.listSheetLightDesc,
            ),
            XuiSheetListItem(
              title = XuiThemeMode.DARK.labelFor(strings),
              subtitle = d.listSheetDarkDesc,
              showRedDot = true,
            ),
            XuiSheetListItem(
              title = d.listSheetResetTitle,
              subtitle = d.listSheetResetDesc,
              isDestructive = true,
            ),
          ),
        selectedIndex = skinManager.state.value.themeMode.ordinal,
        onSelect = { index ->
          when (index) {
            0 -> skinManager.setThemeMode(XuiThemeMode.SYSTEM)
            1 -> skinManager.setThemeMode(XuiThemeMode.LIGHT)
            2 -> skinManager.setThemeMode(XuiThemeMode.DARK)
            3 -> {
              skinManager.selectSkin(XuiSkinPreset.AZURE)
              skinManager.setThemeMode(XuiThemeMode.SYSTEM)
            }
          }
          tipController.showSuccess(d.listSheetUpdated)
        },
      )
    )
  }

  fun showDemoGridBottomSheet() {
    val strings = currentStrings()
    val d = strings.dialogs
    dialogController.showBottomSheet(
      XuiActiveBottomSheet.GridSheet(
        title = d.gridSheetTitle,
        items =
          listOf(
            XuiSheetGridItem("share", d.gridShareLabel, "share"),
            XuiSheetGridItem("copy", d.gridCopyLabel, "copy"),
            XuiSheetGridItem("theme", d.gridThemeLabel, "theme"),
            XuiSheetGridItem("link", d.gridLinkLabel, "link"),
            XuiSheetGridItem("code", d.gridCodeLabel, "code"),
            XuiSheetGridItem("bookmark", d.gridBookmarkLabel, "bookmark"),
            XuiSheetGridItem("download", d.gridExportLabel, "download"),
            XuiSheetGridItem("cli", d.gridBuildLabel, "cli"),
          ),
        onSelect = { item ->
          platformBridge.triggerHaptic(XuiHapticType.SELECTION)
          if (item.id == "link") {
            platformBridge.copyToClipboard(strings.common.appBrand, d.gridSheetLinkUrl)
            tipController.showSuccess(d.gridSheetLinkCopied)
          } else {
            tipController.showInfo(
              XuiTemplateFormatter.format(d.gridSheetActionTemplate, "title" to item.title)
            )
          }
          recordActionResult(
            XuiTemplateFormatter.format(d.gridSheetActionTemplate, "title" to item.title)
          )
        },
      )
    )
  }

  private fun initialState(
    strings: XuiStrings,
    specs: List<XuiComponentSpec>,
  ): ComponentsLabUiState =
    ComponentsLabUiState(
      components = specs,
      floatTags = strings.catalog.defaultFloatTags,
      swipeDemoItems = defaultSwipeItems(strings),
      lastActionResult = strings.catalog.defaultActionHint,
    )

  private fun defaultSwipeItems(strings: XuiStrings): List<XuiSwipeDemoItem> =
    strings.catalog.defaultSwipeTitles.mapIndexed { idx, title ->
      XuiSwipeDemoItem(
        id = idx + 1,
        title = title,
        subtitle = strings.catalog.defaultSwipeSubtitles.getOrElse(idx) { "" },
      )
    }

  private fun filterSpecs(
    specs: List<XuiComponentSpec>,
    category: XuiComponentCategory,
    query: String,
  ): List<XuiComponentSpec> {
    val q = query.trim().lowercase()
    return specs.filter { spec ->
      val catMatch = category == XuiComponentCategory.ALL || spec.category == category
      val textMatch =
        q.isEmpty() ||
          spec.name.lowercase().contains(q) ||
          spec.legacyAndroidClass.lowercase().contains(q) ||
          spec.cmpComposableName.lowercase().contains(q) ||
          spec.summary.lowercase().contains(q)
      catMatch && textMatch
    }
  }
}

data class KoinBindingInfo(
  val interfaceName: String,
  val implementationName: String,
  val scopeKind: String,
  val purpose: String,
)

data class MigrationMappingItem(
  val legacyModule: String,
  val kmpModule: String,
  val decouplingBenefit: String,
)

data class ArchitectureLabUiState(
  val platformName: String,
  val osDetail: String,
  val architectureNote: String,
  val koinBindings: List<KoinBindingInfo>,
  val migrationItems: List<MigrationMappingItem>,
)

class ArchitectureLabViewModel(
  val skinManager: XuiSkinManager,
  val resourceManager: XuiResourceManager,
  private val tipController: XuiTipController,
  private val platformBridge: PlatformBridge,
) : ViewModel() {

  private val _uiState =
    MutableStateFlow(
      buildUiState(resourceManager.state.value.strings)
    )
  val uiState: StateFlow<ArchitectureLabUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      resourceManager.state.collect { resState ->
        _uiState.value = buildUiState(resState.strings)
      }
    }
  }

  private fun buildUiState(strings: XuiStrings): ArchitectureLabUiState {
    val arch = strings.architecture
    val bindings =
      arch.bindingInterfaces.mapIndexed { idx, iface ->
        val impl =
          if (idx < arch.bindingImplementations.size) arch.bindingImplementations[idx]
          else if (platformBridge.isIos) arch.iosBridgeClass else arch.androidBridgeClass
        val scope =
          if (idx < arch.bindingImplementations.size) arch.scopeSingle else arch.scopeExpectActual
        KoinBindingInfo(
          interfaceName = iface,
          implementationName = impl,
          scopeKind = scope,
          purpose = arch.bindingDescriptions.getOrElse(idx) { "" },
        )
      }
    val migrations =
      arch.migrationLegacyModules.mapIndexed { idx, legacy ->
        MigrationMappingItem(
          legacyModule = legacy,
          kmpModule = arch.migrationKmpModules.getOrElse(idx) { "" },
          decouplingBenefit = arch.migrationBenefits.getOrElse(idx) { "" },
        )
      }
    return ArchitectureLabUiState(
      platformName = platformBridge.platformName,
      osDetail = platformBridge.osDetail,
      architectureNote = platformBridge.architectureNote,
      koinBindings = bindings,
      migrationItems = migrations,
    )
  }

  fun selectSkin(preset: XuiSkinPreset) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    skinManager.selectSkin(preset)
    val strings = resourceManager.state.value.strings
    tipController.showSuccess(
      XuiTemplateFormatter.format(
        strings.dialogs.skinSwitchedTemplate,
        "skin" to preset.labelFor(strings),
      )
    )
  }

  fun selectThemeMode(mode: XuiThemeMode) {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    skinManager.setThemeMode(mode)
  }

  fun setRoundedCards(enabled: Boolean) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    skinManager.setRoundedCards(enabled)
  }

  fun selectDensity(preset: XuiDensityPreset) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    resourceManager.selectDensity(preset)
  }

  fun applyBrandToken(brand: String) {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    resourceManager.updateBrandToken(brand)
  }

  fun toggleReplacementRule(ruleId: String) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    resourceManager.toggleRule(ruleId)
  }

  fun resetTemplateWorkshop() {
    platformBridge.triggerHaptic(XuiHapticType.MEDIUM_IMPACT)
    resourceManager.resetAll()
    tipController.showSuccess(resourceManager.state.value.strings.architecture.rulesResetTip)
  }
}

data class HelpersLabUiState(
  val blendFraction: Float,
  val progressValue: Float,
  val isAutoRunningProgress: Boolean = false,
  val imageShapeMode: XuiRadiusShapeMode = XuiRadiusShapeMode.ROUNDED_RECT,
  val imageSelected: Boolean = false,
  val imageBorderDp: Float,
  val lastLinkClicked: String,
)

class HelpersLabViewModel(
  private val tipController: XuiTipController,
  private val platformBridge: PlatformBridge,
  private val resourceManager: XuiResourceManager,
) : ViewModel() {

  private val motion = XuiMotionTokens()
  private var hasCustomLinkTapped = false

  private val _uiState =
    MutableStateFlow(
      HelpersLabUiState(
        blendFraction = motion.defaultBlendFraction,
        progressValue = motion.defaultProgressValue,
        imageBorderDp = motion.defaultImageBorderDp,
        lastLinkClicked = resourceManager.state.value.strings.helpers.linkDefaultHint,
      )
    )
  val uiState: StateFlow<HelpersLabUiState> = _uiState.asStateFlow()

  init {
    viewModelScope.launch {
      resourceManager.state.collect { resState ->
        if (!hasCustomLinkTapped) {
          _uiState.update { it.copy(lastLinkClicked = resState.strings.helpers.linkDefaultHint) }
        }
      }
    }
  }

  fun updateBlendFraction(fraction: Float) {
    _uiState.update { it.copy(blendFraction = fraction.coerceIn(0f, 1f)) }
  }

  fun updateProgress(value: Float) {
    _uiState.update { it.copy(progressValue = value.coerceIn(0f, 1f)) }
  }

  fun runAnimatedProgress() {
    if (_uiState.value.isAutoRunningProgress) return
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    viewModelScope.launch {
      _uiState.update { it.copy(isAutoRunningProgress = true, progressValue = 0f) }
      val steps = motion.progressStepCount
      for (step in 1..steps) {
        delay(motion.progressAnimStepMs)
        _uiState.update { it.copy(progressValue = step.toFloat() / steps.toFloat()) }
      }
      _uiState.update { it.copy(isAutoRunningProgress = false) }
      platformBridge.triggerHaptic(XuiHapticType.SUCCESS)
      tipController.showSuccess(resourceManager.state.value.strings.helpers.progressDoneTip)
    }
  }

  fun setImageShape(mode: XuiRadiusShapeMode) {
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    _uiState.update { it.copy(imageShapeMode = mode) }
  }

  fun toggleImageSelected() {
    platformBridge.triggerHaptic(XuiHapticType.LIGHT_IMPACT)
    _uiState.update { it.copy(imageSelected = !it.imageSelected) }
  }

  fun onLinkTapped(link: String) {
    val strings = resourceManager.state.value.strings
    platformBridge.triggerHaptic(XuiHapticType.SELECTION)
    platformBridge.copyToClipboard(strings.helpers.linkClipLabel, link)
    hasCustomLinkTapped = true
    _uiState.update {
      it.copy(
        lastLinkClicked =
          XuiTemplateFormatter.format(strings.helpers.linkTappedTemplate, "link" to link)
      )
    }
    tipController.showInfo(
      XuiTemplateFormatter.format(strings.helpers.linkTappedTipTemplate, "link" to link)
    )
  }

  fun testHaptic(type: XuiHapticType) {
    val strings = resourceManager.state.value.strings
    platformBridge.triggerHaptic(type)
    tipController.showInfo(
      XuiTemplateFormatter.format(
        strings.common.hapticFeedbackTemplate,
        "platform" to platformBridge.platformName,
        "type" to type.name,
      )
    )
  }
}
