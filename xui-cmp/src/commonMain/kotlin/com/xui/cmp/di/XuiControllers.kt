package com.xui.cmp.di

import com.xui.cmp.tokens.DefaultXuiResourceManager
import com.xui.cmp.tokens.XuiCatalogTemplate
import com.xui.cmp.tokens.XuiMotionTokens
import com.xui.cmp.tokens.XuiResourceManager
import com.xui.cmp.tokens.XuiSkinPreset
import com.xui.cmp.tokens.XuiThemeMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class XuiSkinState(
  val activeSkin: XuiSkinPreset = XuiSkinPreset.AZURE,
  val themeMode: XuiThemeMode = XuiThemeMode.SYSTEM,
  val roundedCards: Boolean = true,
  val skinSwitchCount: Int = 0,
)

interface XuiSkinManager {
  val state: StateFlow<XuiSkinState>

  fun selectSkin(preset: XuiSkinPreset)

  fun setThemeMode(mode: XuiThemeMode)

  fun setRoundedCards(enabled: Boolean)
}

class DefaultXuiSkinManager : XuiSkinManager {
  private val _state = MutableStateFlow(XuiSkinState())
  override val state: StateFlow<XuiSkinState> = _state.asStateFlow()

  override fun selectSkin(preset: XuiSkinPreset) {
    _state.update {
      it.copy(activeSkin = preset, skinSwitchCount = it.skinSwitchCount + 1)
    }
  }

  override fun setThemeMode(mode: XuiThemeMode) {
    _state.update { it.copy(themeMode = mode) }
  }

  override fun setRoundedCards(enabled: Boolean) {
    _state.update { it.copy(roundedCards = enabled) }
  }
}

enum class XuiTipType {
  LOADING,
  SUCCESS,
  FAIL,
  INFO,
}

data class XuiTipState(
  val type: XuiTipType,
  val message: String,
  val cancelable: Boolean = false,
)

interface XuiTipController {
  val tipState: StateFlow<XuiTipState?>

  fun showLoading(message: String = "", autoDismissMs: Long? = null)

  fun showSuccess(message: String, durationMs: Long = defaultMotion.tipSuccessMs)

  fun showFail(message: String, durationMs: Long = defaultMotion.tipFailMs)

  fun showInfo(message: String, durationMs: Long = defaultMotion.tipInfoMs)

  fun dismiss()

  private companion object {
    val defaultMotion = XuiMotionTokens()
  }
}

class DefaultXuiTipController(
  private val resourceManager: XuiResourceManager = DefaultXuiResourceManager(),
  private val dispatcherProvider: () -> CoroutineDispatcher = {
    Dispatchers.Main.immediate
  },
) : XuiTipController {
  private val scope by lazy { CoroutineScope(SupervisorJob() + dispatcherProvider()) }
  private var autoHideJob: Job? = null

  private val _tipState = MutableStateFlow<XuiTipState?>(null)
  override val tipState: StateFlow<XuiTipState?> = _tipState.asStateFlow()

  override fun showLoading(message: String, autoDismissMs: Long?) {
    val resolvedMessage =
      message.ifBlank { resourceManager.state.value.strings.dialogs.tipLoadingDefault }
    show(XuiTipState(XuiTipType.LOADING, resolvedMessage, cancelable = false), autoDismissMs)
  }

  override fun showSuccess(message: String, durationMs: Long) {
    show(XuiTipState(XuiTipType.SUCCESS, message, cancelable = true), durationMs)
  }

  override fun showFail(message: String, durationMs: Long) {
    show(XuiTipState(XuiTipType.FAIL, message, cancelable = true), durationMs)
  }

  override fun showInfo(message: String, durationMs: Long) {
    show(XuiTipState(XuiTipType.INFO, message, cancelable = true), durationMs)
  }

  override fun dismiss() {
    autoHideJob?.cancel()
    autoHideJob = null
    _tipState.value = null
  }

  private fun show(state: XuiTipState, durationMs: Long?) {
    autoHideJob?.cancel()
    _tipState.value = state
    if (durationMs != null && durationMs > 0L) {
      autoHideJob =
        scope.launch {
          delay(durationMs)
          _tipState.value = null
        }
    }
  }
}

sealed interface XuiActiveDialog {
  data class Message(
    val title: String,
    val message: String,
    val positiveText: String = "",
    val negativeText: String? = "",
    val destructive: Boolean = false,
    val onConfirm: () -> Unit = {},
  ) : XuiActiveDialog

  data class Input(
    val title: String,
    val placeholder: String,
    val initialValue: String = "",
    val confirmText: String = "",
    val onConfirm: (String) -> Unit,
  ) : XuiActiveDialog

  data class SingleChoice(
    val title: String,
    val options: List<String>,
    val selectedIndex: Int,
    val onSelect: (Int) -> Unit,
  ) : XuiActiveDialog

  data class MultiChoice(
    val title: String,
    val options: List<String>,
    val selectedIndices: Set<Int>,
    val onConfirm: (Set<Int>) -> Unit,
  ) : XuiActiveDialog
}

sealed interface XuiActiveBottomSheet {
  data class ListSheet(
    val title: String?,
    val items: List<XuiSheetListItem>,
    val selectedIndex: Int? = null,
    val onSelect: (Int) -> Unit,
  ) : XuiActiveBottomSheet

  data class GridSheet(
    val title: String?,
    val items: List<XuiSheetGridItem>,
    val onSelect: (XuiSheetGridItem) -> Unit,
  ) : XuiActiveBottomSheet
}

data class XuiSheetListItem(
  val title: String,
  val subtitle: String? = null,
  val badge: String? = null,
  val isDestructive: Boolean = false,
  val showRedDot: Boolean = false,
)

data class XuiSheetGridItem(
  val id: String,
  val title: String,
  val iconKey: String,
)

interface XuiDialogController {
  val activeDialog: StateFlow<XuiActiveDialog?>
  val activeBottomSheet: StateFlow<XuiActiveBottomSheet?>

  fun showDialog(dialog: XuiActiveDialog)

  fun dismissDialog()

  fun showBottomSheet(sheet: XuiActiveBottomSheet)

  fun dismissBottomSheet()
}

class DefaultXuiDialogController : XuiDialogController {
  private val _activeDialog = MutableStateFlow<XuiActiveDialog?>(null)
  override val activeDialog: StateFlow<XuiActiveDialog?> = _activeDialog.asStateFlow()

  private val _activeBottomSheet = MutableStateFlow<XuiActiveBottomSheet?>(null)
  override val activeBottomSheet: StateFlow<XuiActiveBottomSheet?> =
    _activeBottomSheet.asStateFlow()

  override fun showDialog(dialog: XuiActiveDialog) {
    _activeDialog.value = dialog
  }

  override fun dismissDialog() {
    _activeDialog.value = null
  }

  override fun showBottomSheet(sheet: XuiActiveBottomSheet) {
    _activeBottomSheet.value = sheet
  }

  override fun dismissBottomSheet() {
    _activeBottomSheet.value = null
  }
}

enum class XuiComponentCategory {
  ALL,
  NAVIGATION,
  BUTTONS,
  FEEDBACK,
  LISTS,
}

enum class XuiComponentId {
  TOP_BAR,
  COLLAPSING_TOP_BAR,
  ROUND_BUTTON,
  GROUP_LIST_VIEW,
  DIALOG,
  TIP_DIALOG,
  BOTTOM_SHEET,
  TAB_SEGMENT,
  POPUP,
  EMPTY_VIEW,
  FLOAT_LAYOUT,
  PULL_REFRESH,
  SLIDER,
  STICKY_SECTION,
  SWIPE_ACTION,
  SWIPE_BACK,
  PRIORITY_LAYOUT,
}

data class XuiComponentSpec(
  val id: XuiComponentId,
  val name: String,
  val legacyAndroidClass: String,
  val cmpComposableName: String,
  val category: XuiComponentCategory,
  val summary: String,
  val koinDecouplingNote: String,
  val sampleCode: String,
)

interface XuiComponentCatalog {
  fun getAllComponents(): List<XuiComponentSpec>

  fun findById(id: XuiComponentId): XuiComponentSpec
}

class DefaultXuiComponentCatalog(
  private val resourceManager: XuiResourceManager = DefaultXuiResourceManager(),
) : XuiComponentCatalog {
  override fun getAllComponents(): List<XuiComponentSpec> =
    XuiCatalogTemplate.buildSpecs(resourceManager.state.value.strings)

  override fun findById(id: XuiComponentId): XuiComponentSpec {
    val all = getAllComponents()
    return all.firstOrNull { it.id == id } ?: all.first()
  }
}
