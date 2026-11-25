package com.xui.cmp.tokens

import androidx.compose.runtime.Immutable
import com.xui.cmp.di.XuiComponentCategory
import com.xui.cmp.di.XuiComponentId
import com.xui.cmp.di.XuiComponentSpec
import com.xui.cmp.ui.components.XuiEmptyState

@Immutable
data class XuiStringReplacementRule(
  val id: String,
  val label: String,
  val pattern: String,
  val replacement: String,
  val enabled: Boolean = true,
)

object XuiTemplateFormatter {
  fun format(template: String, vararg args: Pair<String, Any?>): String {
    var result = template
    for ((key, value) in args) {
      result = result.replace("{$key}", value?.toString().orEmpty())
    }
    return result
  }

  fun applyRules(input: String, rules: List<XuiStringReplacementRule>): String {
    var result = input
    for (rule in rules) {
      if (rule.enabled && rule.pattern.isNotEmpty()) {
        result = result.replace(rule.pattern, rule.replacement)
      }
    }
    return result
  }
}

object XuiLinkPatterns {
  const val DETECTABLE_LINK_REGEX =
    "(https?://[\\w\\-._~:/?#\\[\\]@!$&'()*+,;=%]+|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}|\\+?\\d[\\d\\-]{6,}\\d)"
}

@Immutable
data class XuiCommonStrings(
  val appBrand: String,
  val cancel: String,
  val confirm: String,
  val close: String,
  val ok: String,
  val backContentDescription: String,
  val enterContentDescription: String,
  val selectedContentDescription: String,
  val clearSearchContentDescription: String,
  val pinnedContentDescription: String,
  val reset: String,
  val copy: String,
  val copyCode: String,
  val copySample: String,
  val copySuccessTemplate: String,
  val copyPrepared: String,
  val badgeOverflow: String,
  val percentTemplate: String,
  val countdownRunningTemplate: String,
  val defaultCountdownPrefix: String,
  val hapticFeedbackTemplate: String,
  val brandTagPlatform: String,
  val brandTagKoin: String,
  val crossPlatformBadge: String,
  val pullRefreshingTemplate: String,
  val pullReleaseReady: String,
  val pullToRefresh: String,
  val brandInitials: String,
)

@Immutable
data class XuiNavStrings(
  val tabComponents: String,
  val tabArchitecture: String,
  val tabHelpers: String,
)

@Immutable
data class XuiSkinStrings(
  val azureName: String,
  val azureShort: String,
  val azureSubtitle: String,
  val emeraldName: String,
  val emeraldShort: String,
  val emeraldSubtitle: String,
  val coralName: String,
  val coralShort: String,
  val coralSubtitle: String,
  val indigoName: String,
  val indigoShort: String,
  val indigoSubtitle: String,
  val modeSystem: String,
  val modeLight: String,
  val modeDark: String,
)

@Immutable
data class XuiDensityStrings(
  val compactName: String,
  val compactDesc: String,
  val standardName: String,
  val standardDesc: String,
  val comfortableName: String,
  val comfortableDesc: String,
)

@Immutable
data class XuiCategoryStrings(
  val all: String,
  val navigation: String,
  val buttons: String,
  val feedback: String,
  val lists: String,
)

@Immutable
data class XuiComponentTemplateCopy(
  val name: String,
  val legacyAndroidClass: String,
  val cmpComposableName: String,
  val summary: String,
  val koinDecouplingNote: String,
  val sampleCode: String,
)

@Immutable
data class XuiCatalogStrings(
  val topBarTitle: String,
  val topBarSubtitle: String,
  val quickThemeActionDesc: String,
  val heroTitle: String,
  val heroSubtitleTemplate: String,
  val heroMetricComponentsLabel: String,
  val heroMetricReuseValue: String,
  val heroMetricReuseLabel: String,
  val heroMetricCouplingValue: String,
  val heroMetricCouplingLabel: String,
  val searchPlaceholder: String,
  val emptyTitle: String,
  val emptyDetailTemplate: String,
  val emptyResetButton: String,
  val sectionTitleTemplate: String,
  val sectionFooter: String,
  val detailMappingTemplate: String,
  val defaultActionHint: String,
  val loadedLabTemplate: String,
  val pullRefreshResultTemplate: String,
  val pullRefreshSuccessTip: String,
  val floatTagAddedTemplate: String,
  val floatTagRemovedTemplate: String,
  val swipePinUpdatedTemplate: String,
  val swipeDeletedTemplate: String,
  val swipeResetDone: String,
  val defaultFloatTags: List<String>,
  val defaultSwipeTitles: List<String>,
  val defaultSwipeSubtitles: List<String>,
  val componentTemplates: Map<XuiComponentId, XuiComponentTemplateCopy>,
)

@Immutable
data class XuiDialogStrings(
  val tipLoadingDefault: String,
  val tipSuccessDesc: String,
  val tipFailDesc: String,
  val tipInfoDesc: String,
  val multiChoiceConfirmTemplate: String,
  val resetSkinTitle: String,
  val resetSkinMessage: String,
  val normalDialogTitle: String,
  val normalDialogMessage: String,
  val confirmResetBtn: String,
  val gotItBtn: String,
  val resetSkinDone: String,
  val normalDialogConfirmed: String,
  val inputDialogTitle: String,
  val inputDialogPlaceholder: String,
  val inputDialogInitial: String,
  val inputDialogConfirm: String,
  val inputDialogDoneTemplate: String,
  val singleChoiceTitle: String,
  val skinSwitchedTemplate: String,
  val multiChoiceTitle: String,
  val multiChoiceTargets: List<String>,
  val multiChoiceRecordedTemplate: String,
  val multiChoiceTipTemplate: String,
  val listSheetTitle: String,
  val listSheetRecommendBadge: String,
  val listSheetSystemDesc: String,
  val listSheetLightDesc: String,
  val listSheetDarkDesc: String,
  val listSheetResetTitle: String,
  val listSheetResetDesc: String,
  val listSheetUpdated: String,
  val gridSheetTitle: String,
  val gridShareLabel: String,
  val gridCopyLabel: String,
  val gridThemeLabel: String,
  val gridLinkLabel: String,
  val gridCodeLabel: String,
  val gridBookmarkLabel: String,
  val gridExportLabel: String,
  val gridBuildLabel: String,
  val gridSheetLinkUrl: String,
  val gridSheetLinkCopied: String,
  val gridSheetActionTemplate: String,
)

@Immutable
data class XuiPlaygroundStrings(
  val collapsingTitle: String,
  val collapsingSubtitle: String,
  val collapsingSectionTitle: String,
  val collapsingSectionFooter: String,
  val collapsingRowTitleTemplate: String,
  val collapsingRowDetail: String,
  val collapsingRowTipTemplate: String,
  val pullRefreshCardTitle: String,
  val pullRefreshStatusTemplate: String,
  val pullRefreshManualBtn: String,
  val pullRefreshListTitle: String,
  val pullRefreshRowTitleTemplate: String,
  val pullRefreshRowDetail: String,
  val stickySectionHeaders: List<String>,
  val stickySectionBadgeTemplate: String,
  val stickyNavItems: List<String>,
  val stickyInputItems: List<String>,
  val stickyListItems: List<String>,
  val stickyFeedbackItems: List<String>,
  val stickyItemDetail: String,
  val stickyItemTipTemplate: String,
  val livePreviewTitle: String,
  val codeCardTitle: String,
  val legacyPrefixTemplate: String,
  val koinStrategyPrefixTemplate: String,
  val topBarPreviewTitle: String,
  val topBarPreviewSubtitle: String,
  val topBarSaveAction: String,
  val topBarBackResult: String,
  val topBarSaveTip: String,
  val topBarSaveResult: String,
  val topBarTitleCentered: String,
  val topBarTitleStart: String,
  val topBarHideSubtitle: String,
  val topBarShowSubtitle: String,
  val topBarRestoreLight: String,
  val topBarUseBrandBg: String,
  val buttonFilled: String,
  val buttonOutlined: String,
  val buttonTonal: String,
  val buttonDestructive: String,
  val buttonDisabled: String,
  val buttonVerifyIdle: String,
  val buttonVerifyTip: String,
  val buttonLoadingResultTemplate: String,
  val buttonOutlinedResult: String,
  val buttonDestructiveTip: String,
  val alphaButtonLabel: String,
  val alphaButtonTip: String,
  val alphaButtonDesc: String,
  val groupListTitle: String,
  val groupListFooter: String,
  val groupSwitchTitle: String,
  val groupSwitchOn: String,
  val groupSwitchOff: String,
  val groupSwitchResultTemplate: String,
  val groupRedDotTitle: String,
  val groupRedDotDetail: String,
  val groupRedDotTip: String,
  val groupTargetTitle: String,
  val groupTargetBadge: String,
  val groupTargetResult: String,
  val groupRadioATitle: String,
  val groupRadioBTitle: String,
  val dialogMsgBtn: String,
  val dialogDestructiveBtn: String,
  val dialogInputBtn: String,
  val dialogSingleBtn: String,
  val dialogMultiBtn: String,
  val tipLoadingBtn: String,
  val tipLoadingMsg: String,
  val tipSuccessBtn: String,
  val tipSuccessMsg: String,
  val tipFailBtn: String,
  val tipFailMsg: String,
  val tipInfoBtn: String,
  val tipInfoMsg: String,
  val sheetListBtn: String,
  val sheetGridBtn: String,
  val tabItemsFull: List<String>,
  val tabItemsCompact: List<String>,
  val tabSelectedResultTemplate: String,
  val tabModeUnderline: String,
  val tabModePill: String,
  val tabModeDot: String,
  val tabSwitchFixed: String,
  val tabSwitchScrollable: String,
  val popupListBtn: String,
  val popupListItems: List<String>,
  val popupListItem2Tip: String,
  val popupListItem3Tip: String,
  val popupListResultTemplate: String,
  val popupBubbleBtn: String,
  val popupBubbleTitle: String,
  val popupBubbleBody: String,
  val popupQuickBtn: String,
  val popupQuickCopy: String,
  val popupQuickCopyTip: String,
  val popupQuickCopyResult: String,
  val popupQuickShare: String,
  val popupQuickShareTip: String,
  val popupQuickShareResult: String,
  val popupQuickStar: String,
  val popupQuickStarTip: String,
  val popupQuickStarResult: String,
  val emptyLoadingTitle: String,
  val emptyEmptyTitle: String,
  val emptyErrorTitle: String,
  val emptyInfoTitle: String,
  val emptyDetail: String,
  val emptyRetryBtn: String,
  val emptyRetryTip: String,
  val floatRemoveDescTemplate: String,
  val floatAddTagBtn: String,
  val floatTagPrefix: String,
  val floatAlignStart: String,
  val floatAlignCenter: String,
  val floatAlignEnd: String,
  val sliderContinuousTemplate: String,
  val sliderContinuousResultTemplate: String,
  val sliderSteppedTemplate: String,
  val sliderSteppedResultTemplate: String,
  val swipeEmptyTitle: String,
  val swipeEmptyAction: String,
  val swipePinLabel: String,
  val swipeUnpinLabel: String,
  val swipeDeleteLabel: String,
  val swipeResetListBtn: String,
  val swipeBackBannerText: String,
  val swipeBackBtn: String,
  val swipeBackResult: String,
  val priorityLongTitle: String,
  val priorityShortTitle: String,
  val priorityBadgeKeep: String,
  val priorityBadgeKmp: String,
  val priorityToggleShort: String,
  val priorityToggleLong: String,
  val priorityResultTemplate: String,
)

@Immutable
data class XuiArchitectureStrings(
  val topBarTitle: String,
  val topBarSubtitle: String,
  val platformTitleTemplate: String,
  val koinActiveBadge: String,
  val skinWorkshopTitleTemplate: String,
  val skinWorkshopDesc: String,
  val appearanceTitle: String,
  val appearanceFooter: String,
  val roundedCardsTitle: String,
  val roundedCardsOnDescTemplate: String,
  val roundedCardsOffDescTemplate: String,
  val forceDarkTitle: String,
  val forceDarkDescTemplate: String,
  val templateWorkshopTitle: String,
  val templateWorkshopDesc: String,
  val densitySelectorLabel: String,
  val brandTemplateLabel: String,
  val brandInputPlaceholder: String,
  val brandApplyButton: String,
  val brandPresets: List<String>,
  val rulesListLabel: String,
  val rulePreviewTemplate: String,
  val resetRulesButton: String,
  val rulesResetTip: String,
  val koinBindingsTitleTemplate: String,
  val koinBindingsFooter: String,
  val migrationTitle: String,
  val migrationMappingArrow: String,
  val bindingArrow: String,
  val scopeSingle: String,
  val scopeExpectActual: String,
  val bindingDescriptions: List<String>,
  val bindingInterfaces: List<String>,
  val bindingImplementations: List<String>,
  val iosBridgeClass: String,
  val androidBridgeClass: String,
  val migrationLegacyModules: List<String>,
  val migrationKmpModules: List<String>,
  val migrationBenefits: List<String>,
)

@Immutable
data class XuiHelperStrings(
  val topBarTitle: String,
  val topBarSubtitle: String,
  val colorCardTitle: String,
  val colorDarkLabel: String,
  val colorLightLabel: String,
  val colorHexTemplate: String,
  val colorContrastTemplate: String,
  val radiusCardTitle: String,
  val radiusCardSubtitle: String,
  val shapeRoundedRect: String,
  val shapeCircle: String,
  val shapeOval: String,
  val progressCardTitle: String,
  val progressRunningBtn: String,
  val progressRunBtn: String,
  val progressDoneTip: String,
  val linkCardTitle: String,
  val linkDefaultHint: String,
  val linkSampleText: String,
  val linkTappedTemplate: String,
  val linkTappedTipTemplate: String,
  val linkClipLabel: String,
  val spanCardTitle: String,
  val spanSampleText: String,
  val spanHighlights: List<String>,
  val displayMetricsTemplate: String,
  val layoutExpandedLabel: String,
  val layoutCompactLabel: String,
  val hapticCardTitle: String,
)

@Immutable
data class XuiStrings(
  val common: XuiCommonStrings,
  val nav: XuiNavStrings,
  val skins: XuiSkinStrings,
  val densities: XuiDensityStrings,
  val categories: XuiCategoryStrings,
  val catalog: XuiCatalogStrings,
  val dialogs: XuiDialogStrings,
  val playground: XuiPlaygroundStrings,
  val architecture: XuiArchitectureStrings,
  val helpers: XuiHelperStrings,
)

object XuiStringTemplateDefaults {
  const val DEFAULT_BRAND_TOKEN = "XUI"
  const val LEGACY_BRAND_TOKEN = "QMUI"

  fun defaultRules(): List<XuiStringReplacementRule> =
    listOf(
      XuiStringReplacementRule(
        id = "rule_platform_cn",
        label = "术语模版：跨端 → Android/iOS 双端",
        pattern = "跨端",
        replacement = "Android/iOS 双端",
        enabled = false,
      ),
      XuiStringReplacementRule(
        id = "rule_koin_label",
        label = "依赖注入别名：Koin DI → Koin IOC 容器",
        pattern = "Koin DI",
        replacement = "Koin IOC 容器",
        enabled = false,
      ),
      XuiStringReplacementRule(
        id = "rule_legacy_origin",
        label = "保留源码前缀说明：原控件 → 迁移源",
        pattern = "原控件",
        replacement = "迁移源",
        enabled = false,
      ),
    )
}

object XuiStringResolver {
  fun resolve(
    brandToken: String,
    rules: List<XuiStringReplacementRule>,
  ): XuiStrings {
    val activeBrand = brandToken.trim().ifEmpty { XuiStringTemplateDefaults.DEFAULT_BRAND_TOKEN }
    val legacyBrand = XuiStringTemplateDefaults.LEGACY_BRAND_TOKEN
    val slug = activeBrand.lowercase().replace(" ", "-")
    val docsUrl = "https://$slug-kmp.example.com"
    val docsEmail = "$slug-kmp@example.com"

    fun t(template: String): String {
      val interpolated =
        XuiTemplateFormatter.format(
          template,
          "brand" to activeBrand,
          "legacyBrand" to legacyBrand,
          "docsUrl" to docsUrl,
          "docsEmail" to docsEmail,
        )
      return XuiTemplateFormatter.applyRules(interpolated, rules)
    }

    fun tList(items: List<String>): List<String> = items.map(::t)

    val common =
      XuiCommonStrings(
        appBrand = t("{brand}"),
        cancel = t("取消"),
        confirm = t("确定"),
        close = t("关闭"),
        ok = t("确认"),
        backContentDescription = t("返回"),
        enterContentDescription = t("进入"),
        selectedContentDescription = t("已选中"),
        clearSearchContentDescription = t("清除搜索"),
        pinnedContentDescription = t("已置顶"),
        reset = t("重置"),
        copy = t("复制"),
        copyCode = t("复制代码"),
        copySample = t("复制示例"),
        copySuccessTemplate = t("已复制 {name} 代码"),
        copyPrepared = t("已准备代码片段"),
        badgeOverflow = t("99+"),
        percentTemplate = t("{value}%"),
        countdownRunningTemplate = t("{prefix} ({seconds}s)"),
        defaultCountdownPrefix = t("重新获取"),
        hapticFeedbackTemplate = t("已触发 {platform} 触感: {type}"),
        brandTagPlatform = t("Android + iOS"),
        brandTagKoin = t("Koin 4.2"),
        crossPlatformBadge = t("Android & iOS"),
        pullRefreshingTemplate = t("{brand} 正在刷新..."),
        pullReleaseReady = t("松开立即刷新"),
        pullToRefresh = t("下拉刷新内容"),
        brandInitials = activeBrand.take(4).uppercase(),
      )

    val nav =
      XuiNavStrings(
        tabComponents = t("组件库"),
        tabArchitecture = t("Koin 架构"),
        tabHelpers = t("工具集"),
      )

    val skins =
      XuiSkinStrings(
        azureName = t("{brand} 经典蔚蓝"),
        azureShort = t("经典蔚蓝"),
        azureSubtitle = t("Classic {brand} Azure (#00A8E1)"),
        emeraldName = t("生态翠绿"),
        emeraldShort = t("生态翠绿"),
        emeraldSubtitle = t("Ecosystem Emerald (#07C160)"),
        coralName = t("珊瑚炽橙"),
        coralShort = t("珊瑚炽橙"),
        coralSubtitle = t("Vibrant Coral (#FF5A5F)"),
        indigoName = t("极夜靛蓝"),
        indigoShort = t("极夜靛蓝"),
        indigoSubtitle = t("Deep Precision Indigo (#3B82F6)"),
        modeSystem = t("跟随系统"),
        modeLight = t("浅色模式"),
        modeDark = t("深色模式"),
      )

    val densities =
      XuiDensityStrings(
        compactName = t("紧凑模版"),
        compactDesc = t("高密度信息呈现 (0.88x 间距)"),
        standardName = t("标准模版"),
        standardDesc = t("均衡跨端默认规范 (1.00x)"),
        comfortableName = t("舒展模版"),
        comfortableDesc = t("大触控宽松留白 (1.14x 间距)"),
      )

    val categories =
      XuiCategoryStrings(
        all = t("全部组件"),
        navigation = t("导航栏与折叠"),
        buttons = t("按钮与容器"),
        feedback = t("弹窗与提示"),
        lists = t("列表与流式"),
      )

    val componentTemplates =
      mapOf(
        XuiComponentId.TOP_BAR to
          XuiComponentTemplateCopy(
            name = t("{brand}TopBar 导航栏"),
            legacyAndroidClass = "{legacyBrand}TopBar / {legacyBrand}TopBarLayout".let(::t),
            cmpComposableName = t("{brand}TopBar"),
            summary = t("统一跨端顶部栏，支持左返回按钮、居中/左对齐标题副标题、右侧多文本/图标操作与分割线。"),
            koinDecouplingNote = t("通过 Koin 注入的 XuiSkinManager 动态响应换肤，无需依赖 AppCompat ActionBar 或 XML 属性。"),
            sampleCode =
              t(
                """
                XuiTopBar(
                  title = "个人中心",
                  subtitle = "{brand} Unified Header",
                  centerTitle = true,
                  onBackClick = { navigator.pop() },
                  rightActions = {
                    XuiTopBarTextAction("编辑") { tipController.showInfo("已进入编辑") }
                  }
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.COLLAPSING_TOP_BAR to
          XuiComponentTemplateCopy(
            name = t("{brand}CollapsingTopBar 折叠栏"),
            legacyAndroidClass = t("{legacyBrand}CollapsingTopBarLayout"),
            cmpComposableName = t("XuiCollapsingTopBarLayout"),
            summary = t("替代 CoordinatorLayout 的纯 CMP 折叠头部容器，支持 NestedScroll 联动视差缩放与标题平滑渐变。"),
            koinDecouplingNote = t("完全摆脱 Android View 嵌套滚动协议，在 Android 与 iOS Metal 渲染管线保持一致阻尼。"),
            sampleCode =
              t(
                """
                XuiCollapsingTopBarLayout(
                  title = "{brand} KMP 跨端库",
                  expandedSubtitle = "Compose Multiplatform",
                  headerBackground = { HeroBanner() }
                ) { padding ->
                  LazyColumn(contentPadding = padding) { ... }
                }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.ROUND_BUTTON to
          XuiComponentTemplateCopy(
            name = t("{brand}RoundButton 圆角按钮"),
            legacyAndroidClass = t("{legacyBrand}RoundButton / {legacyBrand}AlphaImageButton"),
            cmpComposableName = t("XuiRoundButton & XuiCountdownButton"),
            summary = t("支持自定义圆角/胶囊半径、描边粗细、按压态颜色与透明度反馈、内嵌加载与验证码倒计时状态。"),
            koinDecouplingNote = t("通过 CompositionLocal + Koin 皮肤状态驱动按压色阶计算 (XuiColorHelper)，告别 XML selector。"),
            sampleCode =
              t(
                """
                XuiRoundButton(
                  text = "提交工单",
                  style = XuiButtonStyle.FILLED,
                  isLoading = state.submitting,
                  onClick = { viewModel.submit() }
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.GROUP_LIST_VIEW to
          XuiComponentTemplateCopy(
            name = t("{brand}GroupListView 分组列表"),
            legacyAndroidClass = t("{legacyBrand}GroupListView / {legacyBrand}CommonListItemView"),
            cmpComposableName = t("XuiGroupListSection & XuiCommonListItemView"),
            summary = t("专为设置页与表单设计的分组列表，内置箭头、Switch、单选勾、红点、徽章标签与缩进分割线。"),
            koinDecouplingNote = t("数据状态直接与 Koin ViewModel StateFlow 绑定，消除传统 Section Builder 手动刷新痛点。"),
            sampleCode =
              t(
                """
                XuiGroupListSection(title = "通用设置", footer = "实时同步至 Android / iOS") {
                  XuiCommonListItemView(
                    title = "深色皮肤",
                    accessory = XuiAccessoryType.SwitchToggle(isDark) { skinManager.setThemeMode(...) }
                  )
                }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.DIALOG to
          XuiComponentTemplateCopy(
            name = t("{brand}Dialog 对话框套件"),
            legacyAndroidClass = t("{legacyBrand}Dialog (Message/EditText/Menu/MultiCheck)"),
            cmpComposableName = t("XuiDialogHost + XuiActiveDialog"),
            summary = t("标准化消息确认、文本输入、单选列表与多选确认弹窗，严格遵循 {brand} 留白与按钮分割线比例。"),
            koinDecouplingNote = t("由 Koin 单例 XuiDialogController 托管弹窗队列，ViewModel 无需持有 Activity Context 即可弹窗。"),
            sampleCode =
              t(
                """
                val dialogController: XuiDialogController = koinInject()
                dialogController.showDialog(
                  XuiActiveDialog.Message(
                    title = "解除 Koin 作用域",
                    message = "确认重置当前演示配置吗？",
                    destructive = true
                  )
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.TIP_DIALOG to
          XuiComponentTemplateCopy(
            name = t("{brand}TipDialog 状态浮层"),
            legacyAndroidClass = t("{legacyBrand}TipDialog"),
            cmpComposableName = t("XuiTipDialogOverlay"),
            summary = t("非阻塞轻量级 HUD 浮层，支持加载旋转动画、成功对勾、失败叉号与信息提示自动定时消失。"),
            koinDecouplingNote = t("通过 Koin 注入 XuiTipController，使用 CoroutineScope 统一调度自动隐藏与跨平台触感反馈。"),
            sampleCode =
              t(
                """
                val tipController: XuiTipController = koinInject()
                tipController.showLoading("正在同步...")
                tipController.showSuccess("已保存至本地与云端")
                """.trimIndent(),
              ),
          ),
        XuiComponentId.BOTTOM_SHEET to
          XuiComponentTemplateCopy(
            name = t("{brand}BottomSheet 底部面板"),
            legacyAndroidClass = t("{legacyBrand}BottomSheet (List & Grid)"),
            cmpComposableName = t("XuiBottomListSheet & XuiBottomGridSheet"),
            summary = t("平滑底部弹出面板，支持列表菜单样式（带副标题/红点/选中态）与九宫格分享面板样式。"),
            koinDecouplingNote = t("复用 Koin XuiDialogController 统一状态源，底层基于 ModalBottomSheet 适配手势安全区。"),
            sampleCode =
              t(
                """
                dialogController.showBottomSheet(
                  XuiActiveBottomSheet.GridSheet(
                    title = "分享到跨端渠道",
                    items = shareItems,
                    onSelect = { item -> ... }
                  )
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.TAB_SEGMENT to
          XuiComponentTemplateCopy(
            name = t("{brand}TabSegment 标签切换栏"),
            legacyAndroidClass = t("{legacyBrand}TabSegment"),
            cmpComposableName = t("XuiTabSegment"),
            summary = t("高可定制横向标签栏，支持等宽与可滚动模式、下划线/胶囊/圆点指示器平滑过渡、未读红点与图标。"),
            koinDecouplingNote = t("纯状态驱动动画指示器（animateDpAsState），无需 ViewPager 绑定即可对接任意 CMP 内容容器。"),
            sampleCode =
              t(
                """
                XuiTabSegment(
                  tabs = listOf(XuiTabItem("推荐", badgeCount = 3), XuiTabItem("KMP 架构")),
                  selectedIndex = selectedTab,
                  indicatorStyle = XuiTabIndicatorStyle.UNDERLINE,
                  onTabSelected = { selectedTab = it }
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.POPUP to
          XuiComponentTemplateCopy(
            name = t("{brand}Popup 锚点浮层套件"),
            legacyAndroidClass = t("{legacyBrand}Popup / {legacyBrand}ListPopup / {legacyBrand}QuickAction"),
            cmpComposableName = t("XuiListPopup & XuiBubblePopup & XuiQuickActionPopup"),
            summary = t("完整支持列表浮层、自定义气泡浮层与横排快捷操作栏，均带箭头指示与阴影层级。"),
            koinDecouplingNote = t("纯 Compose Popup + Canvas 箭头绘制，避免 Android PopupWindow 与 iOS UIPopover 的双端差异。"),
            sampleCode =
              t(
                """
                XuiQuickActionPopup(
                  expanded = actionBarExpanded,
                  actions = listOf(XuiQuickActionItem("复制") { ... }, XuiQuickActionItem("分享") { ... }),
                  onDismiss = { actionBarExpanded = false }
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.EMPTY_VIEW to
          XuiComponentTemplateCopy(
            name = t("{brand}EmptyView 空状态视图"),
            legacyAndroidClass = t("{legacyBrand}EmptyView"),
            cmpComposableName = t("XuiEmptyView"),
            summary = t("统一承载加载中、空数据、网络异常、自定义提示的多状态占位视图，内置一键重试按钮。"),
            koinDecouplingNote = t("直接映射 ViewModel 的密封 UI State（Loading / Empty / Error），双端展示像素一致。"),
            sampleCode =
              t(
                """
                XuiEmptyView(
                  state = XuiEmptyState.ERROR,
                  title = "网络请求超时",
                  detail = "请检查网络连接后重试",
                  actionText = "重新加载",
                  onActionClick = { viewModel.retry() }
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.FLOAT_LAYOUT to
          XuiComponentTemplateCopy(
            name = t("{brand}FloatLayout 流式标签布局"),
            legacyAndroidClass = t("{legacyBrand}FloatLayout"),
            cmpComposableName = t("XuiFloatLayout"),
            summary = t("基于 Compose Layout 自定义测量的自动换行流式布局，支持水平/垂直间距、左中右对齐与最大行数限制。"),
            koinDecouplingNote = t("零 Android ViewGroup 依赖的纯数学 MeasurePolicy，支持动态增删标签丝滑重组。"),
            sampleCode =
              t(
                """
                XuiFloatLayout(
                  horizontalSpacing = dimens.spacing8,
                  verticalSpacing = dimens.spacing8,
                  alignment = Alignment.Start,
                  maxLines = 3
                ) {
                  tags.forEach { tag -> XuiTagChip(tag) }
                }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.PULL_REFRESH to
          XuiComponentTemplateCopy(
            name = t("{brand}PullRefreshLayout 下拉刷新"),
            legacyAndroidClass = t("{legacyBrand}PullRefreshLayout"),
            cmpComposableName = t("XuiPullRefreshLayout"),
            summary = t("跨平台阻尼下拉刷新容器，内置 {brand} 弧形旋转刷新头，支持刷新完成反馈动画。"),
            koinDecouplingNote = t("基于 Compose NestedScrollConnection 与 Animatable 实现弹性阻尼，Android 与 iOS 统一手感。"),
            sampleCode =
              t(
                """
                XuiPullRefreshLayout(
                  isRefreshing = uiState.isRefreshing,
                  onRefresh = { viewModel.refresh() }
                ) {
                  LazyColumn { ... }
                }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.SLIDER to
          XuiComponentTemplateCopy(
            name = t("{brand}Slider 滑块与刻度"),
            legacyAndroidClass = t("{legacyBrand}Slider"),
            cmpComposableName = t("XuiSlider"),
            summary = t("支持连续滑动、离散步进刻度点与拖拽悬浮数值气泡的跨端滑块组件。"),
            koinDecouplingNote = t("纯 Compose Canvas + PointerInput 手势识别，脱离 Android SeekBar 绘制与双端触摸差异。"),
            sampleCode =
              t(
                """
                XuiSlider(
                  value = sliderValue,
                  valueRange = 0f..100f,
                  steps = 4,
                  showBubbleOnDrag = true,
                  onValueChange = { sliderValue = it }
                )
                """.trimIndent(),
              ),
          ),
        XuiComponentId.STICKY_SECTION to
          XuiComponentTemplateCopy(
            name = t("{brand}StickySection 吸顶分组"),
            legacyAndroidClass = t("{legacyBrand}StickySectionLayout"),
            cmpComposableName = t("XuiStickySectionList"),
            summary = t("支持分组头部滚动吸顶停靠的结构化列表，适用于通讯录、分类账单与组件索引。"),
            koinDecouplingNote = t("使用 Compose Multiplatform LazyColumn stickyHeader，告别 RecyclerView ItemDecoration 耦合。"),
            sampleCode =
              t(
                """
                XuiStickySectionList(sections = sections) { sectionIdx, item, isLast ->
                  XuiCommonListItemView(title = item, showBottomDivider = !isLast)
                }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.SWIPE_ACTION to
          XuiComponentTemplateCopy(
            name = t("{brand}SwipeAction 侧滑操作行"),
            legacyAndroidClass = t("{legacyBrand}RVItemSwipeAction"),
            cmpComposableName = t("XuiSwipeActionRow"),
            summary = t("支持左滑展开置顶、已读、删除等操作按钮的列表行容器，内置弹性回弹动画。"),
            koinDecouplingNote = t("通过 Animatable + detectHorizontalDragGestures 实现，点击事件直接路由至 Koin ViewModel。"),
            sampleCode =
              t(
                """
                XuiSwipeActionRow(
                  actions = listOf(
                    XuiSwipeActionSpec("置顶", colors.brandPrimary) { viewModel.pin(item) },
                    XuiSwipeActionSpec("删除", colors.error) { viewModel.delete(item) }
                  )
                ) { RowContent(item) }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.SWIPE_BACK to
          XuiComponentTemplateCopy(
            name = t("{brand}SwipeBack 边缘右滑返回"),
            legacyAndroidClass = t("{legacyBrand}SwipeBackLayout"),
            cmpComposableName = t("XuiSwipeBackLayout"),
            summary = t("从屏幕左侧边缘向右滑动返回上一页的跨端手势容器，兼顾 iOS 滑动返回手感与 Android 预测退场。"),
            koinDecouplingNote = t("纯 CMP 手势遮罩实现，不依赖 Android Activity 透明主题或 UIKit UINavigationController。"),
            sampleCode =
              t(
                """
                XuiSwipeBackLayout(onSwipeBack = { navigator.pop() }) {
                  DetailScreenContent()
                }
                """.trimIndent(),
              ),
          ),
        XuiComponentId.PRIORITY_LAYOUT to
          XuiComponentTemplateCopy(
            name = t("{brand}PriorityRow 优先级布局"),
            legacyAndroidClass = t("{legacyBrand}PriorityLinearLayout"),
            cmpComposableName = t("XuiPriorityRow"),
            summary = t("优先保障不可压缩子项（如徽章/按钮）完整展示，将剩余宽度动态分配给弹性标题的自适应行布局。"),
            koinDecouplingNote = t("通过 Compose ParentDataModifier + 自定义 Layout 实现单遍测量，替代 LinearLayout 测量。"),
            sampleCode =
              t(
                """
                XuiPriorityRow(spacing = dimens.spacing8) {
                  Text(longTitle, modifier = Modifier.xuiFlexible(), maxLines = 1, overflow = TextOverflow.Ellipsis)
                  TagBadge("VIP")
                }
                """.trimIndent(),
              ),
          ),
      )

    val catalog =
      XuiCatalogStrings(
        topBarTitle = t("{brand} KMP 组件库"),
        topBarSubtitle = t("Compose Multiplatform · Koin DI"),
        quickThemeActionDesc = t("快速切换主题"),
        heroTitle = t("{brand} 跨端设计体系"),
        heroSubtitleTemplate = t("以 Compose Multiplatform 统一双端渲染，通过 Koin DI 彻底解耦皮肤、弹窗与平台能力。当前运行节点：{platform}"),
        heroMetricComponentsLabel = t("核心控件"),
        heroMetricReuseValue = t("100%"),
        heroMetricReuseLabel = t("commonMain 复用"),
        heroMetricCouplingValue = t("0"),
        heroMetricCouplingLabel = t("XML / View 耦合"),
        searchPlaceholder = t("搜索组件名或原 {brand} 类名（如 {legacyBrand}TopBar）"),
        emptyTitle = t("未找到匹配的 {brand} 组件"),
        emptyDetailTemplate = t("尝试清除筛选词或切换分类查看全部 {count} 个跨端组件"),
        emptyResetButton = t("重置筛选"),
        sectionTitleTemplate = t("{category} ({count})"),
        sectionFooter = t("所有组件均位于 :xui-cmp commonMain，零 Android View 依赖，Koin 自动装配状态控制器与令牌模版。"),
        detailMappingTemplate = t("{legacy} → {cmp}"),
        defaultActionHint = t("点击上方交互控件体验跨端一致反馈"),
        loadedLabTemplate = t("已加载 {name} 交互实验室"),
        pullRefreshResultTemplate = t("第 {count} 次下拉刷新完成 (NestedScrollConnection)"),
        pullRefreshSuccessTip = t("列表已同步最新数据"),
        floatTagAddedTemplate = t("新增流式标签: {tag}"),
        floatTagRemovedTemplate = t("移除流式标签: {tag}"),
        swipePinUpdatedTemplate = t("已更新侧滑条目置顶状态 (#{id})"),
        swipeDeletedTemplate = t("已移除侧滑条目 #{id}"),
        swipeResetDone = t("已恢复默认侧滑演示列表"),
        defaultFloatTags = tList(listOf("KMP", "Compose Multiplatform", "Koin 4.2", "{brand} Azure", "iOS Skiko", "Android M3", "Token 模版")),
        defaultSwipeTitles = tList(listOf("Koin 容器解耦方案", "iOS Metal 统一渲染管线", "{brand}SwipeAction 跨端手势")),
        defaultSwipeSubtitles = tList(listOf("向左滑动体验置顶与删除操作", "Skiko 与 Android Compose 共享 UI 树", "基于 Animatable 与水平拖拽实现")),
        componentTemplates = componentTemplates,
      )

    val dialogs =
      XuiDialogStrings(
        tipLoadingDefault = t("正在加载..."),
        tipSuccessDesc = t("成功"),
        tipFailDesc = t("失败"),
        tipInfoDesc = t("提示"),
        multiChoiceConfirmTemplate = t("确定 ({count})"),
        resetSkinTitle = t("重置皮肤配置"),
        resetSkinMessage = t("确定将当前 Koin 注入的皮肤与圆角策略恢复为默认 {brand} 经典蔚蓝吗？"),
        normalDialogTitle = t("{brand} KMP 跨端对话框"),
        normalDialogMessage = t("此对话框由 commonMain 的 XuiDialogController 统一驱动，在 Android 与 iOS 上零差异渲染。"),
        confirmResetBtn = t("确认重置"),
        gotItBtn = t("知道了"),
        resetSkinDone = t("已恢复默认皮肤"),
        normalDialogConfirmed = t("确认了 MessageDialog"),
        inputDialogTitle = t("修改组件标签"),
        inputDialogPlaceholder = t("输入自定义 KMP 标签..."),
        inputDialogInitial = t("Koin + CMP"),
        inputDialogConfirm = t("添加"),
        inputDialogDoneTemplate = t("已更新: {value}"),
        singleChoiceTitle = t("选择 Koin 全局皮肤"),
        skinSwitchedTemplate = t("已切换至 {skin}"),
        multiChoiceTitle = t("启用编译目标与能力"),
        multiChoiceTargets = tList(listOf("Android (AGP 9 / Compose)", "iOS (iosArm64 / Skiko)", "iOS Simulator", "Koin Scope 验证")),
        multiChoiceRecordedTemplate = t("多选确认: 已勾选 {count} 项目标"),
        multiChoiceTipTemplate = t("已选择 {count} 个目标"),
        listSheetTitle = t("选择 {brand} 运行时主题模式"),
        listSheetRecommendBadge = t("推荐"),
        listSheetSystemDesc = t("自动响应系统浅色与深色切换"),
        listSheetLightDesc = t("高亮度纯净纸白界面"),
        listSheetDarkDesc = t("护眼暗夜模式与深邃层次"),
        listSheetResetTitle = t("恢复默认皮肤"),
        listSheetResetDesc = t("重置为 {brand} 经典蔚蓝"),
        listSheetUpdated = t("主题已更新"),
        gridSheetTitle = t("跨端操作与分享面板"),
        gridShareLabel = t("分享组件"),
        gridCopyLabel = t("复制代码"),
        gridThemeLabel = t("切换皮肤"),
        gridLinkLabel = t("复制文档"),
        gridCodeLabel = t("Koin DSL"),
        gridBookmarkLabel = t("收藏示例"),
        gridExportLabel = t("导出 Spec"),
        gridBuildLabel = t("KMP 构建"),
        gridSheetLinkUrl = t("{docsUrl}"),
        gridSheetLinkCopied = t("已复制 {brand} KMP 文档地址"),
        gridSheetActionTemplate = t("触发面板操作: {title}"),
      )

    val playground =
      XuiPlaygroundStrings(
        collapsingTitle = t("{brand}CollapsingTopBar"),
        collapsingSubtitle = t("向上滚动体验视差缩放、渐变过渡与悬停吸顶"),
        collapsingSectionTitle = t("可折叠滚动内容示例"),
        collapsingSectionFooter = t("NestedScrollConnection 自动同步顶栏折叠进度"),
        collapsingRowTitleTemplate = t("跨端折叠列表项 #{index}"),
        collapsingRowDetail = t("Android & iOS 同步滚动"),
        collapsingRowTipTemplate = t("点击了折叠项 #{index}"),
        pullRefreshCardTitle = t("下拉此区域触发 {brand}PullRefreshLayout"),
        pullRefreshStatusTemplate = t("已完成刷新次数：{count} · {result}"),
        pullRefreshManualBtn = t("手动触发刷新"),
        pullRefreshListTitle = t("可下拉列表数据"),
        pullRefreshRowTitleTemplate = t("{brand} 同步记录 #{index}"),
        pullRefreshRowDetail = t("下拉手势由 CMP PullToRefreshBox 统一驱动"),
        stickySectionHeaders = tList(listOf("A · 导航与转场", "B · 按钮与输入", "C · 列表与流式布局", "D · 弹窗与状态反馈")),
        stickySectionBadgeTemplate = t("{count} 个组件"),
        stickyNavItems = tList(listOf("{brand}TopBar", "{brand}CollapsingTopBar", "{brand}TabSegment", "{brand}SwipeBackLayout")),
        stickyInputItems = tList(listOf("{brand}RoundButton", "{brand}CountdownButton", "{brand}Slider")),
        stickyListItems = tList(listOf("{brand}GroupListSection", "{brand}FloatLayout", "{brand}StickySectionList", "{brand}SwipeActionRow", "{brand}PriorityRow")),
        stickyFeedbackItems = tList(listOf("{brand}DialogHost", "{brand}TipDialogOverlay", "{brand}BottomSheetHost", "{brand}Popup 套件", "{brand}EmptyView")),
        stickyItemDetail = t("吸顶分组演示项 · 点击触发反馈"),
        stickyItemTipTemplate = t("选中分组项: {item}"),
        livePreviewTitle = t("实时交互预览"),
        codeCardTitle = t("Compose Multiplatform + Koin 用法"),
        legacyPrefixTemplate = t("原控件: {legacy}"),
        koinStrategyPrefixTemplate = t("Koin 解耦策略：{note}"),
        topBarPreviewTitle = t("{brand} 预览顶栏"),
        topBarPreviewSubtitle = t("跨端一致沉浸式导航"),
        topBarSaveAction = t("保存"),
        topBarBackResult = t("点击了 TopBar 返回按钮"),
        topBarSaveTip = t("TopBar 右侧动作已响应"),
        topBarSaveResult = t("点击了右侧文本按钮「保存」"),
        topBarTitleCentered = t("标题：居中"),
        topBarTitleStart = t("标题：左对齐"),
        topBarHideSubtitle = t("隐藏副标题"),
        topBarShowSubtitle = t("显示副标题"),
        topBarRestoreLight = t("恢复浅色底"),
        topBarUseBrandBg = t("品牌色顶栏"),
        buttonFilled = t("实心主按钮"),
        buttonOutlined = t("描边按钮"),
        buttonTonal = t("浅底胶囊"),
        buttonDestructive = t("警示操作"),
        buttonDisabled = t("禁用态"),
        buttonVerifyIdle = t("获取验证码"),
        buttonVerifyTip = t("验证码已发送 (10s 倒计时)"),
        buttonLoadingResultTemplate = t("切换按钮加载态: {loading}"),
        buttonOutlinedResult = t("点击了描边圆角按钮"),
        buttonDestructiveTip = t("触发警示按钮演示"),
        alphaButtonLabel = t("{brand}AlphaButton 按压透明反馈："),
        alphaButtonTip = t("AlphaButton 按压透明度降至 0.5f"),
        alphaButtonDesc = t("Alpha 测试"),
        groupListTitle = t("{brand}GroupListSection 示例"),
        groupListFooter = t("支持 Chevron、SwitchToggle、RedDot、TagBadge 与 Checkmark"),
        groupSwitchTitle = t("Koin 自动注入开关"),
        groupSwitchOn = t("已开启跨端状态同步"),
        groupSwitchOff = t("已关闭"),
        groupSwitchResultTemplate = t("SwitchToggle 状态变更: {checked}"),
        groupRedDotTitle = t("消息红点提醒"),
        groupRedDotDetail = t("3 条未读"),
        groupRedDotTip = t("已读取 3 条通知"),
        groupTargetTitle = t("KMP 编译目标"),
        groupTargetBadge = t("iosArm64 + Android"),
        groupTargetResult = t("查看编译目标徽章"),
        groupRadioATitle = t("单选样式 A"),
        groupRadioBTitle = t("单选样式 B"),
        dialogMsgBtn = t("消息确认弹窗"),
        dialogDestructiveBtn = t("警示重置弹窗"),
        dialogInputBtn = t("输入文本弹窗"),
        dialogSingleBtn = t("单选菜单弹窗"),
        dialogMultiBtn = t("多选确认弹窗"),
        tipLoadingBtn = t("Loading (1.5s)"),
        tipLoadingMsg = t("正在跨端渲染..."),
        tipSuccessBtn = t("Success 提示"),
        tipSuccessMsg = t("发送成功"),
        tipFailBtn = t("Fail 提示"),
        tipFailMsg = t("网络连接失败"),
        tipInfoBtn = t("Info 提示"),
        tipInfoMsg = t("请勿重复提交"),
        sheetListBtn = t("弹出 List BottomSheet"),
        sheetGridBtn = t("弹出 Grid 分享面板"),
        tabItemsFull = tList(listOf("推荐", "组件库", "Koin 注入", "iOS 适配", "设置")),
        tabItemsCompact = tList(listOf("推荐", "组件库", "我的")),
        tabSelectedResultTemplate = t("TabSegment 切换至第 {index} 项: {title}"),
        tabModeUnderline = t("下划线模式"),
        tabModePill = t("胶囊模式"),
        tabModeDot = t("圆点模式"),
        tabSwitchFixed = t("切换等宽"),
        tabSwitchScrollable = t("切换可滚动"),
        popupListBtn = t("ListPopup 列表浮层"),
        popupListItems = tList(listOf("切换为 {brand} 经典蔚蓝", "复制当前组件说明", "触发成功 Tip")),
        popupListItem2Tip = t("已触发浮层项 #2"),
        popupListItem3Tip = t("ListPopup 交互完成"),
        popupListResultTemplate = t("{brand}ListPopup 选择了索引 {index}"),
        popupBubbleBtn = t("BubblePopup 自定义气泡"),
        popupBubbleTitle = t("{brand}BubblePopup"),
        popupBubbleBody = t("支持任意 Compose 内容插槽，在 Android 与 iOS 保持统一带箭头阴影气泡。"),
        popupQuickBtn = t("QuickAction 快捷操作条"),
        popupQuickCopy = t("复制"),
        popupQuickCopyTip = t("已复制"),
        popupQuickCopyResult = t("QuickAction: 复制"),
        popupQuickShare = t("分享"),
        popupQuickShareTip = t("分享面板已就绪"),
        popupQuickShareResult = t("QuickAction: 分享"),
        popupQuickStar = t("收藏"),
        popupQuickStarTip = t("已加入收藏"),
        popupQuickStarResult = t("QuickAction: 收藏"),
        emptyLoadingTitle = t("正在加载跨端数据..."),
        emptyEmptyTitle = t("暂无组件收藏记录"),
        emptyErrorTitle = t("网络请求超时 (504)"),
        emptyInfoTitle = t("请先选择目标平台配置"),
        emptyDetail = t("{brand}EmptyView 统一承载加载中、空态与异常重试布局"),
        emptyRetryBtn = t("立即重试"),
        emptyRetryTip = t("已触发重试回调"),
        floatRemoveDescTemplate = t("移除 {tag}"),
        floatAddTagBtn = t("添加标签"),
        floatTagPrefix = t("{brand}-Tag-"),
        floatAlignStart = t("左对齐"),
        floatAlignCenter = t("居中对齐"),
        floatAlignEnd = t("右对齐"),
        sliderContinuousTemplate = t("连续滑块（拖拽弹出数值气泡）：{value}%"),
        sliderContinuousResultTemplate = t("连续滑块当前值: {value}%"),
        sliderSteppedTemplate = t("离散刻度滑块 (5 档位)：{value}"),
        sliderSteppedResultTemplate = t("刻度滑块吸附至: {value}"),
        swipeEmptyTitle = t("侧滑演示列表已清空"),
        swipeEmptyAction = t("恢复演示数据"),
        swipePinLabel = t("置顶"),
        swipeUnpinLabel = t("取消置顶"),
        swipeDeleteLabel = t("删除"),
        swipeResetListBtn = t("重置列表"),
        swipeBackBannerText = t("当前详情页本身已启用 {brand}SwipeBackLayout！请直接从屏幕最左侧边缘向右滑动，即可体验跨端平滑滑动返回目录页。"),
        swipeBackBtn = t("代码触发返回"),
        swipeBackResult = t("触发返回"),
        priorityLongTitle = t("这是一个非常长的跨端动态标题，在宽度不足时会自动省略但绝不会挤掉右侧固定徽章"),
        priorityShortTitle = t("短标题示例"),
        priorityBadgeKeep = t("优先保留"),
        priorityBadgeKmp = t("KMP"),
        priorityToggleShort = t("切换为短标题"),
        priorityToggleLong = t("切换为超长标题"),
        priorityResultTemplate = t("切换 PriorityRow 标题长度: long={long}"),
      )

    val architecture =
      XuiArchitectureStrings(
        topBarTitle = t("Koin 解耦与 KMP 架构"),
        topBarSubtitle = t("实时皮肤 · 令牌模版 · 替换规则引擎"),
        platformTitleTemplate = t("当前运行平台：{platform}"),
        koinActiveBadge = t("Koin Active"),
        skinWorkshopTitleTemplate = t("Koin 响应式换肤工坊 (已切换 {count} 次)"),
        skinWorkshopDesc = t("修改 XuiSkinManager StateFlow，Android 与 iOS 共享组件瞬时无闪烁重组"),
        appearanceTitle = t("主题外观开关"),
        appearanceFooter = t("控制全局卡片圆角和深色模式，由 xuiCoreModule 单例托管。"),
        roundedCardsTitle = t("大圆角卡片风格"),
        roundedCardsOnDescTemplate = t("{radius}dp 现代圆角"),
        roundedCardsOffDescTemplate = t("{radius}dp 经典直角"),
        forceDarkTitle = t("强制深色模式"),
        forceDarkDescTemplate = t("当前模式：{mode}"),
        templateWorkshopTitle = t("设计模版与文案替换规则引擎"),
        templateWorkshopDesc = t("零硬编码驱动：动态切换密度尺寸模版、全局品牌词与术语替换规则，实时作用于全应用。"),
        densitySelectorLabel = t("尺寸密度模版 (XuiDimenTemplate)"),
        brandTemplateLabel = t("全局品牌令牌替换 ({brand})"),
        brandInputPlaceholder = t("输入目标品牌词，如 NovaUI"),
        brandApplyButton = t("应用"),
        brandPresets = listOf("XUI", "NovaUI", "OmniKit"),
        rulesListLabel = t("动态文案替换规则 (XuiStringReplacementRule)"),
        rulePreviewTemplate = t("模版实时效果：{sample}"),
        resetRulesButton = t("恢复默认规则"),
        rulesResetTip = t("已重置默认模版与替换规则"),
        koinBindingsTitleTemplate = t("Koin DI 容器绑定清单 ({count})"),
        koinBindingsFooter = t("通过 commonMain initXuiKoin() 注册，iOS MainViewController 与 Android Application 共享同一装配图。"),
        migrationTitle = t("原生 View → {brand} KMP 架构迁移对照"),
        migrationMappingArrow = t("  ⟶  "),
        bindingArrow = t(" → "),
        scopeSingle = t("Single"),
        scopeExpectActual = t("expect/actual"),
        bindingInterfaces =
          listOf(
            "XuiSkinManager",
            "XuiResourceManager",
            "XuiTipController",
            "XuiDialogController",
            "XuiComponentCatalog",
            "PlatformBridge",
          ),
        bindingImplementations =
          listOf(
            "DefaultXuiSkinManager",
            "DefaultXuiResourceManager",
            "DefaultXuiTipController",
            "DefaultXuiDialogController",
            "DefaultXuiComponentCatalog",
          ),
        iosBridgeClass = "IosPlatformBridge",
        androidBridgeClass = "AndroidPlatformBridge",
        bindingDescriptions =
          tList(
            listOf(
              "全局响应式皮肤与深色模式状态流，彻底取代静态 Context 换肤。",
              "零硬编码模版与文案替换规则引擎，按密度模版与规则实时解析令牌。",
              "协程驱动的 HUD 提示调度中心，ViewModel 可直接触发 TipDialog。",
              "解耦弹窗与 BottomSheet 状态托管，摆脱 Activity/Fragment 生命周期绑定。",
              "17 个 {brand} 核心组件的跨端元信息与 API 文档仓库。",
              "桥接系统剪贴板、触感反馈 (Vibrator / UIFeedbackGenerator) 与系统参数。",
            ),
          ),
        migrationLegacyModules =
          tList(
            listOf(
              "TypedArray + attr.xml / strings.xml",
              "{legacyBrand}SkinManager (View Tree)",
              "{legacyBrand}Dialog.Builder(context)",
              "CoordinatorLayout + AppBar",
              "{legacyBrand}FloatLayout (ViewGroup)",
            ),
          ),
        migrationKmpModules =
          tList(
            listOf(
              "XuiColors + XuiDimenTemplate + XuiStrings",
              "Koin XuiSkinManager & ResourceManager",
              "XuiDialogController (Koin)",
              "XuiCollapsingTopBarLayout",
              "XuiFloatLayout (Compose Layout)",
            ),
          ),
        migrationBenefits =
          tList(
            listOf(
              "编译期类型安全令牌与多套模版，零硬编码与 XML 解析开销",
              "响应式局部重组换肤与密文规则替换，无需重建 Activity",
              "业务逻辑零 Context 耦合，支持 iOS/Android",
              "纯 CMP NestedScrollConnection 跨端一致阻尼",
              "单遍测量布局，无嵌套 View 性能损耗",
            ),
          ),
      )

    val helpers =
      XuiHelperStrings(
        topBarTitle = t("{brand} 工具集与实验室"),
        topBarSubtitle = t("纯 Kotlin 跨端工具类 + 平台触感"),
        colorCardTitle = t("{brand}ColorHelper 色彩插值与对比度"),
        colorDarkLabel = t("DARK"),
        colorLightLabel = t("LIGHT"),
        colorHexTemplate = t("HEX: {hex}"),
        colorContrastTemplate = t("相对纯白对比度：{ratio} : 1 · 比例 {percent}%"),
        radiusCardTitle = t("{brand}RadiusImageView 圆角与边框裁剪"),
        radiusCardSubtitle = t("点击预览图切换选中高亮边框，支持正圆、椭圆与自定义圆角"),
        shapeRoundedRect = t("圆角矩形"),
        shapeCircle = t("正圆"),
        shapeOval = t("椭圆"),
        progressCardTitle = t("{brand}ProgressBar 线性与环形进度"),
        progressRunningBtn = t("演进中..."),
        progressRunBtn = t("运行平滑动画"),
        progressDoneTip = t("进度演示完成 100%"),
        linkCardTitle = t("{brand}LinkTextView 自动链接识别"),
        linkDefaultHint = t("点击下方链接、邮箱或电话测试识别"),
        linkSampleText = t("组件库文档：{docsUrl} ，反馈邮箱 {docsEmail} ，技术支持电话 +86-0755-86013388 ，点击任意高亮文本即可触发跨端识别与剪贴板复制。"),
        linkTappedTemplate = t("已识别并复制: {link}"),
        linkTappedTipTemplate = t("识别链接: {link}"),
        linkClipLabel = t("{brand} Link"),
        spanCardTitle = t("{brand}SpanHelper & {brand}DisplayHelper 跨端工具"),
        spanSampleText = t("通过 KMP + Compose Multiplatform 与 Koin DI，所有关键词高亮、尺寸换算与双端自适应均在 commonMain 完成。"),
        spanHighlights = tList(listOf("KMP", "Compose Multiplatform", "Koin DI", "commonMain")),
        displayMetricsTemplate = t("容器测量：{widthDp}dp ({widthPx}px) · 密度 {density}x · {layoutClass}"),
        layoutExpandedLabel = t("Expanded (>=600dp)"),
        layoutCompactLabel = t("Compact (<600dp)"),
        hapticCardTitle = t("PlatformBridge 跨端触感反馈 (expect/actual)"),
      )

    return XuiStrings(
      common = common,
      nav = nav,
      skins = skins,
      densities = densities,
      categories = categories,
      catalog = catalog,
      dialogs = dialogs,
      playground = playground,
      architecture = architecture,
      helpers = helpers,
    )
  }
}

fun XuiThemeMode.labelFor(strings: XuiStrings): String =
  when (this) {
    XuiThemeMode.SYSTEM -> strings.skins.modeSystem
    XuiThemeMode.LIGHT -> strings.skins.modeLight
    XuiThemeMode.DARK -> strings.skins.modeDark
  }

fun XuiSkinPreset.labelFor(strings: XuiStrings): String =
  when (this) {
    XuiSkinPreset.AZURE -> strings.skins.azureName
    XuiSkinPreset.EMERALD -> strings.skins.emeraldName
    XuiSkinPreset.CORAL -> strings.skins.coralName
    XuiSkinPreset.INDIGO -> strings.skins.indigoName
  }

fun XuiSkinPreset.shortLabelFor(strings: XuiStrings): String =
  when (this) {
    XuiSkinPreset.AZURE -> strings.skins.azureShort
    XuiSkinPreset.EMERALD -> strings.skins.emeraldShort
    XuiSkinPreset.CORAL -> strings.skins.coralShort
    XuiSkinPreset.INDIGO -> strings.skins.indigoShort
  }

fun XuiDensityPreset.labelFor(strings: XuiStrings): String =
  when (this) {
    XuiDensityPreset.COMPACT -> strings.densities.compactName
    XuiDensityPreset.STANDARD -> strings.densities.standardName
    XuiDensityPreset.COMFORTABLE -> strings.densities.comfortableName
  }

fun XuiDensityPreset.descFor(strings: XuiStrings): String =
  when (this) {
    XuiDensityPreset.COMPACT -> strings.densities.compactDesc
    XuiDensityPreset.STANDARD -> strings.densities.standardDesc
    XuiDensityPreset.COMFORTABLE -> strings.densities.comfortableDesc
  }

fun XuiComponentCategory.labelFor(strings: XuiStrings): String =
  when (this) {
    XuiComponentCategory.ALL -> strings.categories.all
    XuiComponentCategory.NAVIGATION -> strings.categories.navigation
    XuiComponentCategory.BUTTONS -> strings.categories.buttons
    XuiComponentCategory.FEEDBACK -> strings.categories.feedback
    XuiComponentCategory.LISTS -> strings.categories.lists
  }

fun XuiEmptyState.playgroundTitleFor(strings: XuiStrings): String =
  when (this) {
    XuiEmptyState.LOADING -> strings.playground.emptyLoadingTitle
    XuiEmptyState.EMPTY -> strings.playground.emptyEmptyTitle
    XuiEmptyState.ERROR -> strings.playground.emptyErrorTitle
    XuiEmptyState.INFO -> strings.playground.emptyInfoTitle
  }

object XuiCatalogTemplate {
  private val categoryById: Map<XuiComponentId, XuiComponentCategory> =
    mapOf(
      XuiComponentId.TOP_BAR to XuiComponentCategory.NAVIGATION,
      XuiComponentId.COLLAPSING_TOP_BAR to XuiComponentCategory.NAVIGATION,
      XuiComponentId.ROUND_BUTTON to XuiComponentCategory.BUTTONS,
      XuiComponentId.GROUP_LIST_VIEW to XuiComponentCategory.LISTS,
      XuiComponentId.DIALOG to XuiComponentCategory.FEEDBACK,
      XuiComponentId.TIP_DIALOG to XuiComponentCategory.FEEDBACK,
      XuiComponentId.BOTTOM_SHEET to XuiComponentCategory.FEEDBACK,
      XuiComponentId.TAB_SEGMENT to XuiComponentCategory.NAVIGATION,
      XuiComponentId.POPUP to XuiComponentCategory.FEEDBACK,
      XuiComponentId.EMPTY_VIEW to XuiComponentCategory.BUTTONS,
      XuiComponentId.FLOAT_LAYOUT to XuiComponentCategory.LISTS,
      XuiComponentId.PULL_REFRESH to XuiComponentCategory.LISTS,
      XuiComponentId.SLIDER to XuiComponentCategory.BUTTONS,
      XuiComponentId.STICKY_SECTION to XuiComponentCategory.LISTS,
      XuiComponentId.SWIPE_ACTION to XuiComponentCategory.LISTS,
      XuiComponentId.SWIPE_BACK to XuiComponentCategory.NAVIGATION,
      XuiComponentId.PRIORITY_LAYOUT to XuiComponentCategory.LISTS,
    )

  fun buildSpecs(strings: XuiStrings): List<XuiComponentSpec> =
    XuiComponentId.entries.map { id ->
      val copy = strings.catalog.componentTemplates.getValue(id)
      XuiComponentSpec(
        id = id,
        name = copy.name,
        legacyAndroidClass = copy.legacyAndroidClass,
        cmpComposableName = copy.cmpComposableName,
        category = categoryById.getValue(id),
        summary = copy.summary,
        koinDecouplingNote = copy.koinDecouplingNote,
        sampleCode = copy.sampleCode,
      )
    }
}
