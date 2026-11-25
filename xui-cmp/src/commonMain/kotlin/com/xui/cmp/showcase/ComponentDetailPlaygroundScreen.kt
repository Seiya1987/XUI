package com.xui.cmp.showcase

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.xui.cmp.di.XuiComponentId
import com.xui.cmp.di.XuiComponentSpec
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiSkinPreset
import com.xui.cmp.tokens.XuiTemplateFormatter
import com.xui.cmp.tokens.playgroundTitleFor
import com.xui.cmp.ui.components.XuiAccessoryType
import com.xui.cmp.ui.components.XuiAlphaButton
import com.xui.cmp.ui.components.XuiBubblePopup
import com.xui.cmp.ui.components.XuiButtonStyle
import com.xui.cmp.ui.components.XuiCollapsingTopBarLayout
import com.xui.cmp.ui.components.XuiCommonListItemView
import com.xui.cmp.ui.components.XuiCountdownButton
import com.xui.cmp.ui.components.XuiEmptyState
import com.xui.cmp.ui.components.XuiEmptyView
import com.xui.cmp.ui.components.XuiFloatLayout
import com.xui.cmp.ui.components.XuiGroupListSection
import com.xui.cmp.ui.components.XuiListPopup
import com.xui.cmp.ui.components.XuiPriorityRow
import com.xui.cmp.ui.components.XuiPullRefreshLayout
import com.xui.cmp.ui.components.XuiQuickActionItem
import com.xui.cmp.ui.components.XuiQuickActionPopup
import com.xui.cmp.ui.components.XuiRoundButton
import com.xui.cmp.ui.components.XuiSlider
import com.xui.cmp.ui.components.XuiStickySection
import com.xui.cmp.ui.components.XuiStickySectionList
import com.xui.cmp.ui.components.XuiSwipeActionRow
import com.xui.cmp.ui.components.XuiSwipeActionSpec
import com.xui.cmp.ui.components.XuiSwipeBackLayout
import com.xui.cmp.ui.components.XuiTabIndicatorStyle
import com.xui.cmp.ui.components.XuiTabItem
import com.xui.cmp.ui.components.XuiTabSegment
import com.xui.cmp.ui.components.XuiTopBar
import com.xui.cmp.ui.components.XuiTopBarIconAction
import com.xui.cmp.ui.components.XuiTopBarTextAction
import kotlin.math.roundToInt

@Composable
fun ComponentDetailPlaygroundScreen(
  spec: XuiComponentSpec,
  uiState: ComponentsLabUiState,
  viewModel: ComponentsLabViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val strings = XuiThemeTokens.strings
  val pg = strings.playground

  XuiSwipeBackLayout(
    onSwipeBack = onBack,
    modifier = modifier,
  ) {
    if (spec.id == XuiComponentId.COLLAPSING_TOP_BAR) {
      XuiCollapsingTopBarLayout(
        title = pg.collapsingTitle,
        expandedSubtitle = pg.collapsingSubtitle,
        onBackClick = onBack,
        rightActions = {
          XuiTopBarIconAction(
            icon = Icons.Default.ContentCopy,
            contentDescription = strings.common.copyCode,
            tint = colors.heroText,
            onClick = { viewModel.copyCodeSnippet(spec) },
          )
        },
        headerBackground = { _ ->
          Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
              color = colors.heroOverlayStrong,
              radius = size.minDimension * 0.45f,
              center = Offset(size.width * 0.85f, size.height * 0.25f),
            )
            drawCircle(
              color = colors.heroOverlaySoft,
              radius = size.minDimension * 0.30f,
              center = Offset(size.width * 0.2f, size.height * 0.15f),
            )
          }
        },
      ) { contentPadding ->
        LazyColumn(
          modifier = Modifier.fillMaxSize().background(colors.canvasBackground),
          contentPadding =
            PaddingValues(
              start = dimens.horizontalPadding,
              end = dimens.horizontalPadding,
              top = contentPadding.calculateTopPadding() + dimens.spacing14,
              bottom = dimens.spacing28,
            ),
          verticalArrangement = Arrangement.spacedBy(dimens.spacing14),
        ) {
          item { ComponentOverviewCard(spec = spec) }
          item {
            XuiGroupListSection(
              title = pg.collapsingSectionTitle,
              footer = pg.collapsingSectionFooter,
            ) {
              for (i in 1..10) {
                XuiCommonListItemView(
                  title = XuiTemplateFormatter.format(pg.collapsingRowTitleTemplate, "index" to i),
                  detailText = pg.collapsingRowDetail,
                  accessory = XuiAccessoryType.Chevron,
                  showBottomDivider = i < 10,
                  onClick = {
                    viewModel.tipController.showInfo(
                      XuiTemplateFormatter.format(pg.collapsingRowTipTemplate, "index" to i)
                    )
                  },
                )
              }
            }
          }
        }
      }
      return@XuiSwipeBackLayout
    }

    Column(modifier = Modifier.fillMaxSize().background(colors.canvasBackground).navigationBarsPadding()) {
      XuiTopBar(
        title = spec.name,
        subtitle = spec.cmpComposableName,
        onBackClick = onBack,
        rightActions = {
          XuiTopBarIconAction(
            icon = Icons.Default.ContentCopy,
            contentDescription = strings.common.copySample,
            onClick = { viewModel.copyCodeSnippet(spec) },
          )
        },
      )

      if (spec.id == XuiComponentId.PULL_REFRESH) {
        XuiPullRefreshLayout(
          isRefreshing = uiState.isPullRefreshing,
          onRefresh = { viewModel.triggerPullRefresh() },
          modifier = Modifier.fillMaxSize(),
        ) {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(dimens.horizontalPadding),
            verticalArrangement = Arrangement.spacedBy(dimens.spacing14),
          ) {
            item { ComponentOverviewCard(spec = spec) }
            item {
              Surface(
                shape = shapes.card,
                color = colors.cardSurface,
                border = BorderStroke(dimens.hairline, colors.separatorHairline),
              ) {
                Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
                  Text(
                    text = pg.pullRefreshCardTitle,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = colors.titlePrimary,
                  )
                  Spacer(modifier = Modifier.height(dimens.spacing6))
                  Text(
                    text =
                      XuiTemplateFormatter.format(
                        pg.pullRefreshStatusTemplate,
                        "count" to uiState.refreshCount,
                        "result" to uiState.lastActionResult,
                      ),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.textSecondary,
                  )
                  Spacer(modifier = Modifier.height(dimens.spacing12))
                  XuiRoundButton(
                    text = pg.pullRefreshManualBtn,
                    isLoading = uiState.isPullRefreshing,
                    onClick = { viewModel.triggerPullRefresh() },
                  )
                }
              }
            }
            item {
              XuiGroupListSection(title = pg.pullRefreshListTitle) {
                for (i in 1..6) {
                  XuiCommonListItemView(
                    title =
                      XuiTemplateFormatter.format(
                        pg.pullRefreshRowTitleTemplate,
                        "index" to (uiState.refreshCount * 6 + i),
                      ),
                    detailText = pg.pullRefreshRowDetail,
                    verticalDetail = true,
                    showBottomDivider = i < 6,
                  )
                }
              }
            }
          }
        }
        return@XuiSwipeBackLayout
      }

      if (spec.id == XuiComponentId.STICKY_SECTION) {
        val sections = remember(pg) {
          val sectionLists =
            listOf(
              pg.stickyNavItems,
              pg.stickyInputItems,
              pg.stickyListItems,
              pg.stickyFeedbackItems,
            )
          pg.stickySectionHeaders.mapIndexed { idx, header ->
            val items = sectionLists.getOrElse(idx) { emptyList() }
            XuiStickySection(
              title = header,
              badge = XuiTemplateFormatter.format(pg.stickySectionBadgeTemplate, "count" to items.size),
              items = items,
            )
          }
        }
        XuiStickySectionList(
          sections = sections,
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(bottom = dimens.spacing24),
        ) { _, item, isLast ->
          XuiCommonListItemView(
            title = item,
            detailText = pg.stickyItemDetail,
            accessory = XuiAccessoryType.Chevron,
            showBottomDivider = !isLast,
            onClick = {
              viewModel.tipController.showInfo(
                XuiTemplateFormatter.format(pg.stickyItemTipTemplate, "item" to item)
              )
            },
          )
        }
        return@XuiSwipeBackLayout
      }

      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
          modifier = Modifier.fillMaxSize().widthIn(max = dimens.contentMaxWidth),
          contentPadding = PaddingValues(dimens.horizontalPadding),
          verticalArrangement = Arrangement.spacedBy(dimens.spacing16),
        ) {
          item { ComponentOverviewCard(spec = spec) }

          item {
            Surface(
              shape = shapes.card,
              color = colors.cardSurface,
              border = BorderStroke(dimens.hairline, colors.separatorHairline),
            ) {
              Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
                Text(
                  text = pg.livePreviewTitle,
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = colors.titlePrimary,
                )
                Spacer(modifier = Modifier.height(dimens.spacing4))
                Text(
                  text = uiState.lastActionResult,
                  style = MaterialTheme.typography.bodySmall,
                  color = colors.brandPrimary,
                )
                Spacer(modifier = Modifier.height(dimens.spacing14))

                when (spec.id) {
                  XuiComponentId.TOP_BAR -> TopBarPlayground(viewModel)
                  XuiComponentId.ROUND_BUTTON -> RoundButtonPlayground(viewModel)
                  XuiComponentId.GROUP_LIST_VIEW -> GroupListPlayground(viewModel)
                  XuiComponentId.DIALOG -> DialogPlayground(viewModel)
                  XuiComponentId.TIP_DIALOG -> TipDialogPlayground(viewModel)
                  XuiComponentId.BOTTOM_SHEET -> BottomSheetPlayground(viewModel)
                  XuiComponentId.TAB_SEGMENT -> TabSegmentPlayground(viewModel)
                  XuiComponentId.POPUP -> PopupPlayground(viewModel)
                  XuiComponentId.EMPTY_VIEW -> EmptyViewPlayground(viewModel)
                  XuiComponentId.FLOAT_LAYOUT -> FloatLayoutPlayground(uiState, viewModel)
                  XuiComponentId.SLIDER -> SliderPlayground(viewModel)
                  XuiComponentId.SWIPE_ACTION -> SwipeActionPlayground(uiState, viewModel)
                  XuiComponentId.SWIPE_BACK -> SwipeBackPlayground(onBack, viewModel)
                  XuiComponentId.PRIORITY_LAYOUT -> PriorityLayoutPlayground(viewModel)
                  XuiComponentId.COLLAPSING_TOP_BAR,
                  XuiComponentId.PULL_REFRESH,
                  XuiComponentId.STICKY_SECTION -> Unit
                }
              }
            }
          }

          item {
            CodeSnippetCard(
              title = pg.codeCardTitle,
              code = spec.sampleCode,
              onCopy = { viewModel.copyCodeSnippet(spec) },
            )
          }

          item { Spacer(modifier = Modifier.height(dimens.spacing16)) }
        }
      }
    }
  }
}

@Composable
private fun ComponentOverviewCard(spec: XuiComponentSpec) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val pg = strings.playground

  Surface(
    shape = shapes.card,
    color = colors.cardSurface,
    border = BorderStroke(dimens.hairline, colors.separatorHairline),
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Surface(
          shape = shapes.badge,
          color = colors.brandContainer,
        ) {
          Text(
            text = XuiTemplateFormatter.format(pg.legacyPrefixTemplate, "legacy" to spec.legacyAndroidClass),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = colors.brandPrimary,
            modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
          )
        }
        Surface(
          shape = shapes.badge,
          color = colors.success.copy(alpha = 0.14f),
        ) {
          Text(
            text = strings.common.crossPlatformBadge,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = colors.success,
            modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
          )
        }
      }
      Spacer(modifier = Modifier.height(dimens.spacing12))
      Text(
        text = spec.summary,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = textSizes.lineHeightBody),
        color = colors.titlePrimary,
      )
      Spacer(modifier = Modifier.height(dimens.spacing10))
      Surface(
        shape = shapes.iconContainer,
        color = colors.elevatedSurface,
      ) {
        Text(
          text =
            XuiTemplateFormatter.format(
              pg.koinStrategyPrefixTemplate,
              "note" to spec.koinDecouplingNote,
            ),
          style = MaterialTheme.typography.bodySmall.copy(lineHeight = textSizes.lineHeightBodyCompact),
          color = colors.textSecondary,
          modifier = Modifier.padding(dimens.spacing12),
        )
      }
    }
  }
}

@Composable
private fun TopBarPlayground(viewModel: ComponentsLabViewModel) {
  var centerTitle by remember { mutableStateOf(true) }
  var showSubtitle by remember { mutableStateOf(true) }
  var useBrandBg by remember { mutableStateOf(false) }
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val pg = XuiThemeTokens.strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    Surface(
      shape = shapes.popup,
      border = BorderStroke(dimens.hairline, colors.separatorHairline),
    ) {
      XuiTopBar(
        title = pg.topBarPreviewTitle,
        subtitle = if (showSubtitle) pg.topBarPreviewSubtitle else null,
        centerTitle = centerTitle,
        applyStatusBarPadding = false,
        backgroundColor = if (useBrandBg) colors.brandPrimary else colors.topBarBackground,
        contentColor = if (useBrandBg) colors.heroText else colors.topBarContent,
        onBackClick = { viewModel.recordActionResult(pg.topBarBackResult) },
        rightActions = {
          XuiTopBarTextAction(
            text = pg.topBarSaveAction,
            color = if (useBrandBg) colors.heroText else colors.brandPrimary,
            onClick = {
              viewModel.tipController.showSuccess(pg.topBarSaveTip)
              viewModel.recordActionResult(pg.topBarSaveResult)
            },
          )
        },
      )
    }

    XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
      XuiRoundButton(
        text = if (centerTitle) pg.topBarTitleCentered else pg.topBarTitleStart,
        style = XuiButtonStyle.TONAL,
        onClick = { centerTitle = !centerTitle },
      )
      XuiRoundButton(
        text = if (showSubtitle) pg.topBarHideSubtitle else pg.topBarShowSubtitle,
        style = XuiButtonStyle.OUTLINED,
        onClick = { showSubtitle = !showSubtitle },
      )
      XuiRoundButton(
        text = if (useBrandBg) pg.topBarRestoreLight else pg.topBarUseBrandBg,
        style = XuiButtonStyle.FILLED,
        onClick = { useBrandBg = !useBrandBg },
      )
    }
  }
}

@Composable
private fun RoundButtonPlayground(viewModel: ComponentsLabViewModel) {
  var loading by remember { mutableStateOf(false) }
  var pillShape by remember { mutableStateOf(true) }
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val motion = XuiThemeTokens.motion
  val pg = XuiThemeTokens.strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    XuiFloatLayout(horizontalSpacing = dimens.spacing10, verticalSpacing = dimens.spacing10) {
      XuiRoundButton(
        text = pg.buttonFilled,
        style = XuiButtonStyle.FILLED,
        isRadiusAdjustBounds = pillShape,
        cornerRadius = dimens.buttonDefaultCornerRadius,
        isLoading = loading,
        onClick = {
          loading = !loading
          viewModel.recordActionResult(
            XuiTemplateFormatter.format(pg.buttonLoadingResultTemplate, "loading" to loading)
          )
        },
      )
      XuiRoundButton(
        text = pg.buttonOutlined,
        style = XuiButtonStyle.OUTLINED,
        isRadiusAdjustBounds = pillShape,
        cornerRadius = dimens.buttonDefaultCornerRadius,
        leadingIcon = Icons.Default.Palette,
        onClick = { viewModel.recordActionResult(pg.buttonOutlinedResult) },
      )
      XuiRoundButton(
        text = pg.buttonTonal,
        style = XuiButtonStyle.TONAL,
        isRadiusAdjustBounds = pillShape,
        cornerRadius = dimens.buttonDefaultCornerRadius,
        onClick = { pillShape = !pillShape },
      )
      XuiRoundButton(
        text = pg.buttonDestructive,
        style = XuiButtonStyle.DESTRUCTIVE,
        isRadiusAdjustBounds = pillShape,
        cornerRadius = dimens.buttonDefaultCornerRadius,
        onClick = { viewModel.tipController.showFail(pg.buttonDestructiveTip) },
      )
      XuiCountdownButton(
        idleText = pg.buttonVerifyIdle,
        totalSeconds = motion.demoCountdownSeconds,
        onStartCountdown = { viewModel.tipController.showSuccess(pg.buttonVerifyTip) },
      )
      XuiRoundButton(
        text = pg.buttonDisabled,
        enabled = false,
        onClick = {},
      )
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(dimens.spacing12),
    ) {
      Text(pg.alphaButtonLabel, style = MaterialTheme.typography.bodySmall, color = colors.textSecondary)
      XuiAlphaButton(
        onClick = { viewModel.tipController.showInfo(pg.alphaButtonTip) },
      ) {
        Surface(
          shape = shapes.circle,
          color = colors.brandContainer,
          modifier = Modifier.size(dimens.alphaButtonPreviewSize),
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Favorite, contentDescription = pg.alphaButtonDesc, tint = colors.brandPrimary)
          }
        }
      }
    }
  }
}

@Composable
private fun GroupListPlayground(viewModel: ComponentsLabViewModel) {
  var switchChecked by remember { mutableStateOf(true) }
  var selectedRow by remember { mutableIntStateOf(0) }
  val colors = XuiThemeTokens.colors
  val pg = XuiThemeTokens.strings.playground

  XuiGroupListSection(
    title = pg.groupListTitle,
    footer = pg.groupListFooter,
  ) {
    XuiCommonListItemView(
      title = pg.groupSwitchTitle,
      detailText = if (switchChecked) pg.groupSwitchOn else pg.groupSwitchOff,
      verticalDetail = true,
      leadingIcon = Icons.Default.Settings,
      accessory =
        XuiAccessoryType.SwitchToggle(switchChecked) {
          switchChecked = it
          viewModel.recordActionResult(
            XuiTemplateFormatter.format(pg.groupSwitchResultTemplate, "checked" to it)
          )
        },
    )
    XuiCommonListItemView(
      title = pg.groupRedDotTitle,
      detailText = pg.groupRedDotDetail,
      leadingIcon = Icons.Default.Notifications,
      leadingIconTint = colors.error,
      accessory = XuiAccessoryType.RedDot(count = 3),
      onClick = { viewModel.tipController.showInfo(pg.groupRedDotTip) },
    )
    XuiCommonListItemView(
      title = pg.groupTargetTitle,
      leadingIcon = Icons.Default.Code,
      accessory = XuiAccessoryType.TagBadge(pg.groupTargetBadge),
      onClick = { viewModel.recordActionResult(pg.groupTargetResult) },
    )
    XuiCommonListItemView(
      title = pg.groupRadioATitle,
      accessory = XuiAccessoryType.Checkmark(selectedRow == 0),
      onClick = { selectedRow = 0 },
    )
    XuiCommonListItemView(
      title = pg.groupRadioBTitle,
      accessory = XuiAccessoryType.Checkmark(selectedRow == 1),
      showBottomDivider = false,
      onClick = { selectedRow = 1 },
    )
  }
}

@Composable
private fun DialogPlayground(viewModel: ComponentsLabViewModel) {
  val dimens = XuiThemeTokens.dimens
  val pg = XuiThemeTokens.strings.playground
  XuiFloatLayout(horizontalSpacing = dimens.spacing10, verticalSpacing = dimens.spacing10) {
    XuiRoundButton(pg.dialogMsgBtn, onClick = { viewModel.showDemoMessageDialog(destructive = false) })
    XuiRoundButton(
      pg.dialogDestructiveBtn,
      style = XuiButtonStyle.DESTRUCTIVE,
      onClick = { viewModel.showDemoMessageDialog(destructive = true) },
    )
    XuiRoundButton(
      pg.dialogInputBtn,
      style = XuiButtonStyle.OUTLINED,
      onClick = { viewModel.showDemoInputDialog() },
    )
    XuiRoundButton(
      pg.dialogSingleBtn,
      style = XuiButtonStyle.TONAL,
      onClick = { viewModel.showDemoSingleChoiceDialog() },
    )
    XuiRoundButton(
      pg.dialogMultiBtn,
      style = XuiButtonStyle.OUTLINED,
      onClick = { viewModel.showDemoMultiChoiceDialog() },
    )
  }
}

@Composable
private fun TipDialogPlayground(viewModel: ComponentsLabViewModel) {
  val dimens = XuiThemeTokens.dimens
  val motion = XuiThemeTokens.motion
  val pg = XuiThemeTokens.strings.playground
  XuiFloatLayout(horizontalSpacing = dimens.spacing10, verticalSpacing = dimens.spacing10) {
    XuiRoundButton(
      text = pg.tipLoadingBtn,
      onClick = {
        viewModel.tipController.showLoading(
          message = pg.tipLoadingMsg,
          autoDismissMs = motion.tipDemoLoadingMs,
        )
      },
    )
    XuiRoundButton(
      text = pg.tipSuccessBtn,
      style = XuiButtonStyle.TONAL,
      onClick = { viewModel.tipController.showSuccess(pg.tipSuccessMsg) },
    )
    XuiRoundButton(
      text = pg.tipFailBtn,
      style = XuiButtonStyle.DESTRUCTIVE,
      onClick = { viewModel.tipController.showFail(pg.tipFailMsg) },
    )
    XuiRoundButton(
      text = pg.tipInfoBtn,
      style = XuiButtonStyle.OUTLINED,
      onClick = { viewModel.tipController.showInfo(pg.tipInfoMsg) },
    )
  }
}

@Composable
private fun BottomSheetPlayground(viewModel: ComponentsLabViewModel) {
  val dimens = XuiThemeTokens.dimens
  val pg = XuiThemeTokens.strings.playground
  XuiFloatLayout(horizontalSpacing = dimens.spacing10, verticalSpacing = dimens.spacing10) {
    XuiRoundButton(
      text = pg.sheetListBtn,
      onClick = { viewModel.showDemoListBottomSheet() },
    )
    XuiRoundButton(
      text = pg.sheetGridBtn,
      style = XuiButtonStyle.TONAL,
      onClick = { viewModel.showDemoGridBottomSheet() },
    )
  }
}

@Composable
private fun TabSegmentPlayground(viewModel: ComponentsLabViewModel) {
  var selected by remember { mutableIntStateOf(0) }
  var style by remember { mutableStateOf(XuiTabIndicatorStyle.UNDERLINE) }
  var scrollable by remember { mutableStateOf(false) }
  val dimens = XuiThemeTokens.dimens
  val pg = XuiThemeTokens.strings.playground

  val items =
    remember(scrollable, pg) {
      if (scrollable) {
        listOf(
          XuiTabItem(pg.tabItemsFull.getOrElse(0) { "" }, Icons.Default.Home, badgeCount = 5),
          XuiTabItem(pg.tabItemsFull.getOrElse(1) { "" }, Icons.Default.Star),
          XuiTabItem(pg.tabItemsFull.getOrElse(2) { "" }, showDot = true),
          XuiTabItem(pg.tabItemsFull.getOrElse(3) { "" }),
          XuiTabItem(pg.tabItemsFull.getOrElse(4) { "" }, Icons.Default.Person),
        )
      } else {
        listOf(
          XuiTabItem(pg.tabItemsCompact.getOrElse(0) { "" }, Icons.Default.Home, badgeCount = 5),
          XuiTabItem(pg.tabItemsCompact.getOrElse(1) { "" }, Icons.Default.Star, showDot = true),
          XuiTabItem(pg.tabItemsCompact.getOrElse(2) { "" }, Icons.Default.Person),
        )
      }
    }

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    XuiTabSegment(
      tabs = items,
      selectedIndex = selected,
      indicatorStyle = style,
      scrollable = scrollable,
      onTabSelected = {
        selected = it
        viewModel.recordActionResult(
          XuiTemplateFormatter.format(
            pg.tabSelectedResultTemplate,
            "index" to (it + 1),
            "title" to items[it].title,
          )
        )
      },
    )

    XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
      XuiRoundButton(
        text = pg.tabModeUnderline,
        style = if (style == XuiTabIndicatorStyle.UNDERLINE) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
        onClick = { style = XuiTabIndicatorStyle.UNDERLINE },
      )
      XuiRoundButton(
        text = pg.tabModePill,
        style = if (style == XuiTabIndicatorStyle.PILL) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
        onClick = { style = XuiTabIndicatorStyle.PILL },
      )
      XuiRoundButton(
        text = pg.tabModeDot,
        style = if (style == XuiTabIndicatorStyle.DOT) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
        onClick = { style = XuiTabIndicatorStyle.DOT },
      )
      XuiRoundButton(
        text = if (scrollable) pg.tabSwitchFixed else pg.tabSwitchScrollable,
        style = XuiButtonStyle.TONAL,
        onClick = {
          scrollable = !scrollable
          selected = 0
        },
      )
    }
  }
}

@Composable
private fun PopupPlayground(viewModel: ComponentsLabViewModel) {
  var listExpanded by remember { mutableStateOf(false) }
  var bubbleExpanded by remember { mutableStateOf(false) }
  var quickExpanded by remember { mutableStateOf(false) }
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val pg = XuiThemeTokens.strings.playground

  XuiFloatLayout(horizontalSpacing = dimens.spacing10, verticalSpacing = dimens.spacing10) {
    Box {
      XuiRoundButton(
        text = pg.popupListBtn,
        leadingIcon = Icons.Default.MoreVert,
        onClick = { listExpanded = true },
      )
      XuiListPopup(
        expanded = listExpanded,
        items = pg.popupListItems,
        onDismiss = { listExpanded = false },
        onItemClick = { idx ->
          when (idx) {
            0 -> viewModel.skinManager.selectSkin(XuiSkinPreset.AZURE)
            1 -> viewModel.tipController.showSuccess(pg.popupListItem2Tip)
            2 -> viewModel.tipController.showSuccess(pg.popupListItem3Tip)
          }
          viewModel.recordActionResult(
            XuiTemplateFormatter.format(pg.popupListResultTemplate, "index" to idx)
          )
        },
      )
    }

    Box {
      XuiRoundButton(
        text = pg.popupBubbleBtn,
        style = XuiButtonStyle.TONAL,
        onClick = { bubbleExpanded = true },
      )
      XuiBubblePopup(
        expanded = bubbleExpanded,
        onDismiss = { bubbleExpanded = false },
      ) {
        Text(
          text = pg.popupBubbleTitle,
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = colors.titlePrimary,
        )
        Spacer(modifier = Modifier.height(dimens.spacing4))
        Text(
          text = pg.popupBubbleBody,
          style = MaterialTheme.typography.bodySmall,
          color = colors.textSecondary,
        )
      }
    }

    Box {
      XuiRoundButton(
        text = pg.popupQuickBtn,
        style = XuiButtonStyle.OUTLINED,
        onClick = { quickExpanded = true },
      )
      XuiQuickActionPopup(
        expanded = quickExpanded,
        actions =
          listOf(
            XuiQuickActionItem(pg.popupQuickCopy, Icons.Default.ContentCopy) {
              viewModel.tipController.showSuccess(pg.popupQuickCopyTip)
              viewModel.recordActionResult(pg.popupQuickCopyResult)
            },
            XuiQuickActionItem(pg.popupQuickShare, Icons.Default.Share) {
              viewModel.tipController.showInfo(pg.popupQuickShareTip)
              viewModel.recordActionResult(pg.popupQuickShareResult)
            },
            XuiQuickActionItem(pg.popupQuickStar, Icons.Default.Star) {
              viewModel.tipController.showSuccess(pg.popupQuickStarTip)
              viewModel.recordActionResult(pg.popupQuickStarResult)
            },
          ),
        onDismiss = { quickExpanded = false },
      )
    }
  }
}

@Composable
private fun EmptyViewPlayground(viewModel: ComponentsLabViewModel) {
  var state by remember { mutableStateOf(XuiEmptyState.ERROR) }
  val dimens = XuiThemeTokens.dimens
  val strings = XuiThemeTokens.strings
  val pg = strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
      XuiEmptyState.entries.forEach { target ->
        XuiRoundButton(
          text = target.name,
          style = if (state == target) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
          onClick = { state = target },
        )
      }
    }

    XuiEmptyView(
      state = state,
      title = state.playgroundTitleFor(strings),
      detail = pg.emptyDetail,
      actionText = if (state == XuiEmptyState.LOADING) null else pg.emptyRetryBtn,
      onActionClick = {
        state = XuiEmptyState.LOADING
        viewModel.tipController.showInfo(pg.emptyRetryTip)
      },
    )
  }
}

@Composable
private fun FloatLayoutPlayground(
  uiState: ComponentsLabUiState,
  viewModel: ComponentsLabViewModel,
) {
  var alignment by remember { mutableStateOf(Alignment.Start) }
  var counter by remember { mutableIntStateOf(1) }
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val pg = XuiThemeTokens.strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    XuiFloatLayout(
      horizontalSpacing = dimens.spacing8,
      verticalSpacing = dimens.spacing8,
      alignment = alignment,
      modifier = Modifier.fillMaxWidth(),
    ) {
      uiState.floatTags.forEach { tag ->
        Surface(
          shape = shapes.chip,
          color = colors.brandContainer,
          modifier = Modifier.clip(shapes.chip).clickable { viewModel.removeFloatTag(tag) },
        ) {
          Row(
            modifier = Modifier.padding(horizontal = dimens.spacing12, vertical = dimens.spacing7),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = tag,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
              color = colors.brandPrimary,
            )
            Spacer(modifier = Modifier.width(dimens.spacing4))
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = XuiTemplateFormatter.format(pg.floatRemoveDescTemplate, "tag" to tag),
              tint = colors.brandPrimary,
              modifier = Modifier.size(dimens.iconXs),
            )
          }
        }
      }
    }

    XuiFloatLayout(horizontalSpacing = dimens.spacing8, verticalSpacing = dimens.spacing8) {
      XuiRoundButton(
        text = pg.floatAddTagBtn,
        leadingIcon = Icons.Default.Add,
        onClick = {
          viewModel.addFloatTag("${pg.floatTagPrefix}$counter")
          counter++
        },
      )
      XuiRoundButton(
        text = pg.floatAlignStart,
        style = if (alignment == Alignment.Start) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
        onClick = { alignment = Alignment.Start },
      )
      XuiRoundButton(
        text = pg.floatAlignCenter,
        style =
          if (alignment == Alignment.CenterHorizontally) XuiButtonStyle.FILLED
          else XuiButtonStyle.OUTLINED,
        onClick = { alignment = Alignment.CenterHorizontally },
      )
      XuiRoundButton(
        text = pg.floatAlignEnd,
        style = if (alignment == Alignment.End) XuiButtonStyle.FILLED else XuiButtonStyle.OUTLINED,
        onClick = { alignment = Alignment.End },
      )
    }
  }
}

@Composable
private fun SliderPlayground(viewModel: ComponentsLabViewModel) {
  val motion = XuiThemeTokens.motion
  var continuousValue by remember { mutableFloatStateOf(motion.defaultSliderContinuous) }
  var steppedValue by remember { mutableFloatStateOf(motion.defaultSliderStepped) }
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val strings = XuiThemeTokens.strings
  val pg = strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    Text(
      text =
        XuiTemplateFormatter.format(
          pg.sliderContinuousTemplate,
          "value" to continuousValue.roundToInt(),
        ),
      style = MaterialTheme.typography.bodySmall,
      color = colors.textSecondary,
    )
    XuiSlider(
      value = continuousValue,
      valueRange = 0f..motion.defaultSliderMax,
      onValueChange = {
        continuousValue = it
        viewModel.recordActionResult(
          XuiTemplateFormatter.format(
            pg.sliderContinuousResultTemplate,
            "value" to it.roundToInt(),
          )
        )
      },
      valueFormatter = {
        XuiTemplateFormatter.format(strings.common.percentTemplate, "value" to it.roundToInt())
      },
    )

    Text(
      text =
        XuiTemplateFormatter.format(
          pg.sliderSteppedTemplate,
          "value" to steppedValue.roundToInt(),
        ),
      style = MaterialTheme.typography.bodySmall,
      color = colors.textSecondary,
    )
    XuiSlider(
      value = steppedValue,
      valueRange = 0f..motion.defaultSliderMax,
      steps = 3,
      onValueChange = {
        steppedValue = it
        viewModel.recordActionResult(
          XuiTemplateFormatter.format(
            pg.sliderSteppedResultTemplate,
            "value" to it.roundToInt(),
          )
        )
      },
    )
  }
}

@Composable
private fun SwipeActionPlayground(
  uiState: ComponentsLabUiState,
  viewModel: ComponentsLabViewModel,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val pg = strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing10)) {
    if (uiState.swipeDemoItems.isEmpty()) {
      XuiEmptyView(
        state = XuiEmptyState.EMPTY,
        title = pg.swipeEmptyTitle,
        actionText = pg.swipeEmptyAction,
        onActionClick = { viewModel.resetSwipeItems() },
      )
    } else {
      uiState.swipeDemoItems.forEach { item ->
        Surface(
          shape = shapes.popup,
          border = BorderStroke(dimens.hairline, colors.separatorHairline),
        ) {
          XuiSwipeActionRow(
            actions =
              listOf(
                XuiSwipeActionSpec(
                  label = if (item.pinned) pg.swipeUnpinLabel else pg.swipePinLabel,
                  containerColor = colors.brandPrimary,
                  onClick = { viewModel.toggleSwipeItemPin(item.id) },
                ),
                XuiSwipeActionSpec(
                  label = pg.swipeDeleteLabel,
                  containerColor = colors.error,
                  onClick = { viewModel.deleteSwipeItem(item.id) },
                ),
              ),
          ) {
            Row(
              modifier = Modifier.fillMaxWidth().padding(horizontal = dimens.spacing14, vertical = dimens.spacing12),
              verticalAlignment = Alignment.CenterVertically,
            ) {
              if (item.pinned) {
                Icon(
                  imageVector = Icons.Default.PushPin,
                  contentDescription = strings.common.pinnedContentDescription,
                  tint = colors.brandPrimary,
                  modifier = Modifier.size(dimens.iconSm),
                )
                Spacer(modifier = Modifier.width(dimens.spacing8))
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = item.title,
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    fontSize = textSizes.bodyLg,
                  ),
                  color = colors.titlePrimary,
                )
                Spacer(modifier = Modifier.height(dimens.spacing2))
                Text(
                  text = item.subtitle,
                  style = MaterialTheme.typography.bodySmall,
                  color = colors.textSecondary,
                )
              }
            }
          }
        }
      }
      XuiRoundButton(
        text = pg.swipeResetListBtn,
        style = XuiButtonStyle.OUTLINED,
        leadingIcon = Icons.Default.Refresh,
        onClick = { viewModel.resetSwipeItems() },
      )
    }
  }
}

@Composable
private fun SwipeBackPlayground(
  onBack: () -> Unit,
  viewModel: ComponentsLabViewModel,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val pg = XuiThemeTokens.strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    Surface(
      shape = shapes.popup,
      color = colors.brandContainer,
    ) {
      Text(
        text = pg.swipeBackBannerText,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = textSizes.lineHeightBody),
        color = colors.brandPrimary,
        modifier = Modifier.padding(dimens.spacing14),
      )
    }
    XuiRoundButton(
      text = pg.swipeBackBtn,
      style = XuiButtonStyle.TONAL,
      onClick = {
        viewModel.recordActionResult(pg.swipeBackResult)
        onBack()
      },
    )
  }
}

@Composable
private fun PriorityLayoutPlayground(viewModel: ComponentsLabViewModel) {
  var longTitle by remember { mutableStateOf(true) }
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val pg = XuiThemeTokens.strings.playground

  Column(verticalArrangement = Arrangement.spacedBy(dimens.spacing12)) {
    Surface(
      shape = shapes.popup,
      color = colors.elevatedSurface,
      border = BorderStroke(dimens.hairline, colors.separatorHairline),
    ) {
      XuiPriorityRow(
        spacing = dimens.spacing8,
        modifier = Modifier.padding(horizontal = dimens.spacing14, vertical = dimens.spacing12),
      ) {
        Text(
          text = if (longTitle) pg.priorityLongTitle else pg.priorityShortTitle,
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
          color = colors.titlePrimary,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          modifier = Modifier.xuiFlexible(),
        )
        Surface(
          shape = shapes.badgeSmall,
          color = colors.brandPrimary,
        ) {
          Text(
            text = pg.priorityBadgeKeep,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = colors.onBrandPrimary,
            modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
          )
        }
        Surface(
          shape = shapes.badgeSmall,
          color = colors.success.copy(alpha = 0.16f),
        ) {
          Text(
            text = pg.priorityBadgeKmp,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = colors.success,
            modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
          )
        }
      }
    }

    XuiRoundButton(
      text = if (longTitle) pg.priorityToggleShort else pg.priorityToggleLong,
      style = XuiButtonStyle.OUTLINED,
      onClick = {
        longTitle = !longTitle
        viewModel.recordActionResult(
          XuiTemplateFormatter.format(pg.priorityResultTemplate, "long" to longTitle)
        )
      },
    )
  }
}

@Composable
private fun CodeSnippetCard(
  title: String,
  code: String,
  onCopy: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val common = XuiThemeTokens.strings.common

  Surface(
    shape = shapes.card,
    color = colors.codeSurface,
  ) {
    Column(modifier = Modifier.fillMaxWidth().padding(dimens.spacing16)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Text(
          text = title,
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
          color = colors.codeTitle,
        )
        XuiAlphaButton(onClick = onCopy) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = dimens.spacing8, vertical = dimens.spacing4),
          ) {
            Icon(
              imageVector = Icons.Default.ContentCopy,
              contentDescription = common.copyCode,
              tint = colors.codeActionText,
              modifier = Modifier.size(dimens.iconBarSmall),
            )
            Spacer(modifier = Modifier.width(dimens.spacing4))
            Text(
              text = common.copy,
              style = MaterialTheme.typography.labelSmall,
              color = colors.codeActionText,
            )
          }
        }
      }
      Spacer(modifier = Modifier.height(dimens.spacing10))
      Text(
        text = code,
        style = MaterialTheme.typography.bodySmall.copy(
          fontFamily = FontFamily.Monospace,
          fontSize = textSizes.bodyXs,
          lineHeight = textSizes.lineHeightCode,
        ),
        color = colors.codeBody,
      )
    }
  }
}
