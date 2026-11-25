package com.xui.cmp.tokens

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

enum class XuiThemeMode(val key: String) {
  SYSTEM("system"),
  LIGHT("light"),
  DARK("dark"),
}

enum class XuiDensityPreset(val id: String, val scale: Float) {
  COMPACT("compact", 0.88f),
  STANDARD("standard", 1.0f),
  COMFORTABLE("comfortable", 1.14f),
}

enum class XuiSkinPreset(
  val id: String,
  val primaryLight: Color,
  val primaryPressedLight: Color,
  val primaryContainerLight: Color,
  val primaryDark: Color,
  val primaryPressedDark: Color,
  val primaryContainerDark: Color,
  val secondaryAccent: Color,
) {
  AZURE(
    id = "xui_azure",
    primaryLight = Color(0xFF00A8E1),
    primaryPressedLight = Color(0xFF008DC0),
    primaryContainerLight = Color(0xFFE0F5FC),
    primaryDark = Color(0xFF38BDF8),
    primaryPressedDark = Color(0xFF0284C7),
    primaryContainerDark = Color(0xFF0C354D),
    secondaryAccent = Color(0xFF1B88EE),
  ),
  EMERALD(
    id = "xui_emerald",
    primaryLight = Color(0xFF07C160),
    primaryPressedLight = Color(0xFF06AD56),
    primaryContainerLight = Color(0xFFE3F9EC),
    primaryDark = Color(0xFF2DD47E),
    primaryPressedDark = Color(0xFF16A34A),
    primaryContainerDark = Color(0xFF0B3B24),
    secondaryAccent = Color(0xFF10B981),
  ),
  CORAL(
    id = "xui_coral",
    primaryLight = Color(0xFFFF5A5F),
    primaryPressedLight = Color(0xFFE0484D),
    primaryContainerLight = Color(0xFFFFEBEB),
    primaryDark = Color(0xFFFF7A7E),
    primaryPressedDark = Color(0xFFE11D48),
    primaryContainerDark = Color(0xFF4C141A),
    secondaryAccent = Color(0xFFFB923C),
  ),
  INDIGO(
    id = "xui_indigo",
    primaryLight = Color(0xFF2563EB),
    primaryPressedLight = Color(0xFF1D4ED8),
    primaryContainerLight = Color(0xFFE8F0FE),
    primaryDark = Color(0xFF60A5FA),
    primaryPressedDark = Color(0xFF3B82F6),
    primaryContainerDark = Color(0xFF172B4D),
    secondaryAccent = Color(0xFF6366F1),
  ),
}

@Immutable
data class XuiColors(
  val isDark: Boolean,
  val brandPrimary: Color,
  val brandPrimaryPressed: Color,
  val brandContainer: Color,
  val onBrandPrimary: Color,
  val secondaryAccent: Color,
  val canvasBackground: Color,
  val cardSurface: Color,
  val elevatedSurface: Color,
  val topBarBackground: Color,
  val topBarContent: Color,
  val separatorHairline: Color,
  val titlePrimary: Color,
  val textSecondary: Color,
  val textPlaceholder: Color,
  val iconMuted: Color,
  val success: Color,
  val onSuccess: Color,
  val warning: Color,
  val error: Color,
  val onError: Color,
  val onDestructive: Color,
  val tipHudBackground: Color,
  val onTipHud: Color,
  val hudDivider: Color,
  val scrim: Color,
  val swipeBackScrim: Color,
  val transparent: Color,
  val heroText: Color,
  val heroTextMuted: Color,
  val heroTextSubtle: Color,
  val heroBadgeBackground: Color,
  val heroOverlayStrong: Color,
  val heroOverlaySoft: Color,
  val codeSurface: Color,
  val codeTitle: Color,
  val codeBody: Color,
  val codeActionText: Color,
  val sliderThumbSurface: Color,
  val sliderActiveTick: Color,
  val switchThumb: Color,
  val onAccentBadge: Color,
  val swatchLightLabel: Color,
  val swatchDarkLabel: Color,
  val swatchLightBackground: Color,
  val demoBlendStart: Color,
  val demoBlendEnd: Color,
)

@Immutable
data class XuiDimens(
  val zero: Dp,
  val hairline: Dp,
  val borderThin: Dp,
  val borderMedium: Dp,
  val borderThick: Dp,
  val spacing1: Dp,
  val spacing2: Dp,
  val spacing3: Dp,
  val spacing4: Dp,
  val spacing5: Dp,
  val spacing6: Dp,
  val spacing7: Dp,
  val spacing8: Dp,
  val spacing10: Dp,
  val spacing11: Dp,
  val spacing12: Dp,
  val spacing13: Dp,
  val spacing14: Dp,
  val spacing16: Dp,
  val spacing18: Dp,
  val spacing20: Dp,
  val spacing22: Dp,
  val spacing24: Dp,
  val spacing28: Dp,
  val spacing32: Dp,
  val spacing48: Dp,
  val spacing64: Dp,
  val spacing88: Dp,
  val spacing96: Dp,
  val topBarHeight: Dp,
  val topBarTitleCenterPadding: Dp,
  val topBarTitleLeftStartWithBack: Dp,
  val topBarTitleLeftStartNoBack: Dp,
  val topBarTitleLeftEndPadding: Dp,
  val collapsingExpandedHeight: Dp,
  val collapsingCollapsedHeight: Dp,
  val collapsingTitleSidePadding: Dp,
  val listItemMinHeight: Dp,
  val listDividerIconIndent: Dp,
  val dialogRowMinHeight: Dp,
  val sheetRowMinHeight: Dp,
  val buttonMinHeight: Dp,
  val actionRowMinHeight: Dp,
  val tabHeight: Dp,
  val tabPillHeight: Dp,
  val tabPillCornerRadius: Dp,
  val tabPillHorizontalInset: Dp,
  val tabPillTopOffset: Dp,
  val tabIndicatorHeight: Dp,
  val tabIndicatorCorner: Dp,
  val tabIndicatorMinWidth: Dp,
  val tabIndicatorMaxWidth: Dp,
  val tabIndicatorInsetTotal: Dp,
  val tabDotSize: Dp,
  val tabDotBottomOffset: Dp,
  val redDotSize: Dp,
  val badgeDotSize: Dp,
  val iconXs: Dp,
  val iconBarSmall: Dp,
  val iconSm: Dp,
  val iconButtonLead: Dp,
  val iconListLead: Dp,
  val iconMd: Dp,
  val iconAction: Dp,
  val iconLg: Dp,
  val iconXl: Dp,
  val iconHud: Dp,
  val iconEmpty: Dp,
  val iconBoxSm: Dp,
  val iconBoxMd: Dp,
  val iconBoxHero: Dp,
  val iconBoxLg: Dp,
  val iconBoxXl: Dp,
  val alphaButtonPreviewSize: Dp,
  val avatarSize: Dp,
  val avatarOvalWidth: Dp,
  val swatchSize: Dp,
  val cardCornerRadius: Dp,
  val cardLargeRadius: Dp,
  val buttonCornerRadius: Dp,
  val buttonDefaultCornerRadius: Dp,
  val pillRadius: Dp,
  val dialogRadius: Dp,
  val sheetRadius: Dp,
  val inputRadius: Dp,
  val chipRadius: Dp,
  val badgeRadius: Dp,
  val popupRadius: Dp,
  val sliderHeight: Dp,
  val sliderBubbleHeight: Dp,
  val sliderTrackHeight: Dp,
  val sliderTrackThick: Dp,
  val sliderThumbSize: Dp,
  val sliderTickRadius: Dp,
  val sliderBubbleMinWidth: Dp,
  val swipeActionWidth: Dp,
  val swipeEdgeWidth: Dp,
  val popupArrowWidth: Dp,
  val popupArrowHeight: Dp,
  val popupMinWidth: Dp,
  val popupListMaxWidth: Dp,
  val popupMaxWidth: Dp,
  val quickActionBarHeight: Dp,
  val quickActionDividerHeight: Dp,
  val quickActionArrowWidth: Dp,
  val quickActionArrowHeight: Dp,
  val dialogMaxWidth: Dp,
  val hudMinWidth: Dp,
  val hudMaxWidth: Dp,
  val contentMaxWidth: Dp,
  val expandedBreakpoint: Dp,
  val progressRingSize: Dp,
  val progressButtonSpinnerSize: Dp,
  val emptyLoadingSize: Dp,
  val progressStrokeSm: Dp,
  val progressStrokePull: Dp,
  val progressStrokeHud: Dp,
  val progressStrokeMd: Dp,
  val progressStrokeLg: Dp,
  val shadowXs: Dp,
  val shadowSm: Dp,
  val shadowMd: Dp,
  val shadowPopover: Dp,
  val shadowLg: Dp,
  val horizontalPadding: Dp,
)

@Immutable
data class XuiShapeTokens(
  val card: Shape,
  val cardLarge: Shape,
  val buttonDefault: Shape,
  val buttonPill: Shape,
  val dialog: Shape,
  val sheetTop: Shape,
  val input: Shape,
  val inputDialog: Shape,
  val chip: Shape,
  val badge: Shape,
  val badgeSmall: Shape,
  val popup: Shape,
  val quickAction: Shape,
  val iconContainer: Shape,
  val emptyIcon: Shape,
  val circle: Shape,
)

@Immutable
data class XuiTextSizes(
  val heroTitle: TextUnit,
  val headline: TextUnit,
  val titleLg: TextUnit,
  val titleMd: TextUnit,
  val titleSm: TextUnit,
  val bodyLg: TextUnit,
  val bodyMd: TextUnit,
  val bodySm: TextUnit,
  val bodyXs: TextUnit,
  val labelLg: TextUnit,
  val labelMd: TextUnit,
  val labelSm: TextUnit,
  val micro: TextUnit,
  val lineHeightBody: TextUnit,
  val lineHeightBodyCompact: TextUnit,
  val lineHeightLink: TextUnit,
  val lineHeightCode: TextUnit,
  val letterSpacingLabel: TextUnit,
  val letterSpacingMonogram: TextUnit,
)

@Immutable
data class XuiMotionTokens(
  val tipSuccessMs: Long = 1600L,
  val tipFailMs: Long = 1800L,
  val tipInfoMs: Long = 1600L,
  val tipDemoLoadingMs: Long = 1500L,
  val pullRefreshDelayMs: Long = 1100L,
  val progressAnimStepMs: Long = 55L,
  val progressStepCount: Int = 20,
  val progressAnimDurationMs: Int = 360,
  val countdownStepMs: Long = 1000L,
  val defaultCountdownSeconds: Int = 30,
  val demoCountdownSeconds: Int = 10,
  val badgeOverflowThreshold: Int = 99,
  val defaultBlendFraction: Float = 0.35f,
  val defaultProgressValue: Float = 0.64f,
  val defaultSliderContinuous: Float = 42f,
  val defaultSliderStepped: Float = 50f,
  val defaultSliderMax: Float = 100f,
  val defaultImageBorderDp: Float = 2f,
  val swipeBackThresholdFraction: Float = 0.28f,
  val swipeBackRevealMinRatio: Float = 0.01f,
  val swipeActionThresholdFraction: Float = 0.42f,
  val swipeActionCloseThresholdPx: Float = 4f,
  val pullRefreshMinFraction: Float = 0.05f,
  val headerParallaxFactor: Float = 0.35f,
  val collapsingSubtitleFadeRate: Float = 1.45f,
  val collapsingTitleStartFraction: Float = 0.55f,
  val collapsingTitleFadeSpan: Float = 0.45f,
  val collapsingDividerFraction: Float = 0.92f,
  val pressedScale: Float = 0.97f,
  val pressedAlpha: Float = 0.5f,
  val disabledAlpha: Float = 0.45f,
  val disabledButtonAlpha: Float = 0.4f,
  val disabledBorderAlpha: Float = 0.35f,
  val dialogWidthFraction: Float = 0.86f,
  val dialogEnterScale: Float = 0.92f,
  val dialogExitScale: Float = 0.94f,
  val tabUnderlineWidthRatio: Float = 0.42f,
  val sliderThumbInnerRatio: Float = 0.42f,
)

object XuiPaletteTemplate {
  private val lightCanvas = Color(0xFFF2F4F8)
  private val lightCard = Color(0xFFFFFFFF)
  private val lightElevated = Color(0xFFF8FAFC)
  private val lightSeparator = Color(0xFFE2E8F0)
  private val lightTitle = Color(0xFF191F28)
  private val lightTextSecondary = Color(0xFF5D6778)
  private val lightTextPlaceholder = Color(0xFF94A3B8)
  private val lightIconMuted = Color(0xFF64748B)
  private val lightSuccess = Color(0xFF07C160)
  private val lightWarning = Color(0xFFF59E0B)
  private val lightError = Color(0xFFFA5151)
  private val lightHudBg = Color(0xE61F242D)
  private val lightScrim = Color(0x730F172A)

  private val darkCanvas = Color(0xFF0D1117)
  private val darkCard = Color(0xFF161B22)
  private val darkElevated = Color(0xFF1F2631)
  private val darkSeparator = Color(0xFF2B3340)
  private val darkTitle = Color(0xFFF1F5F9)
  private val darkTextSecondary = Color(0xFF94A3B8)
  private val darkTextPlaceholder = Color(0xFF64748B)
  private val darkSuccess = Color(0xFF2DD47E)
  private val darkWarning = Color(0xFFFBBF24)
  private val darkError = Color(0xFFFF6B6B)
  private val darkHudBg = Color(0xEB222834)
  private val darkScrim = Color(0x99000000)

  private val pureWhite = Color(0xFFFFFFFF)
  private val pureBlack = Color(0xFF000000)
  private val darkOnBrand = Color(0xFF0B1120)
  private val codePanelBg = Color(0xFF141923)
  private val codePanelTitle = Color(0xFF93C5FD)
  private val codePanelBody = Color(0xFFE2E8F0)

  fun buildColors(skin: XuiSkinPreset, isDark: Boolean): XuiColors =
    if (isDark) {
      XuiColors(
        isDark = true,
        brandPrimary = skin.primaryDark,
        brandPrimaryPressed = skin.primaryPressedDark,
        brandContainer = skin.primaryContainerDark,
        onBrandPrimary = darkOnBrand,
        secondaryAccent = skin.secondaryAccent,
        canvasBackground = darkCanvas,
        cardSurface = darkCard,
        elevatedSurface = darkElevated,
        topBarBackground = darkCard,
        topBarContent = darkTitle,
        separatorHairline = darkSeparator,
        titlePrimary = darkTitle,
        textSecondary = darkTextSecondary,
        textPlaceholder = darkTextPlaceholder,
        iconMuted = darkTextSecondary,
        success = darkSuccess,
        onSuccess = pureWhite,
        warning = darkWarning,
        error = darkError,
        onError = pureWhite,
        onDestructive = pureWhite,
        tipHudBackground = darkHudBg,
        onTipHud = pureWhite,
        hudDivider = pureWhite.copy(alpha = 0.25f),
        scrim = darkScrim,
        swipeBackScrim = pureBlack,
        transparent = Color.Transparent,
        heroText = pureWhite,
        heroTextMuted = pureWhite.copy(alpha = 0.86f),
        heroTextSubtle = pureWhite.copy(alpha = 0.8f),
        heroBadgeBackground = pureWhite.copy(alpha = 0.18f),
        heroOverlayStrong = pureWhite.copy(alpha = 0.14f),
        heroOverlaySoft = pureWhite.copy(alpha = 0.08f),
        codeSurface = codePanelBg,
        codeTitle = codePanelTitle,
        codeBody = codePanelBody,
        codeActionText = pureWhite.copy(alpha = 0.85f),
        sliderThumbSurface = pureWhite,
        sliderActiveTick = pureWhite,
        switchThumb = pureWhite,
        onAccentBadge = pureWhite,
        swatchLightLabel = pureWhite,
        swatchDarkLabel = darkOnBrand,
        swatchLightBackground = pureWhite,
        demoBlendStart = skin.primaryDark,
        demoBlendEnd = darkSuccess,
      )
    } else {
      XuiColors(
        isDark = false,
        brandPrimary = skin.primaryLight,
        brandPrimaryPressed = skin.primaryPressedLight,
        brandContainer = skin.primaryContainerLight,
        onBrandPrimary = pureWhite,
        secondaryAccent = skin.secondaryAccent,
        canvasBackground = lightCanvas,
        cardSurface = lightCard,
        elevatedSurface = lightElevated,
        topBarBackground = lightCard,
        topBarContent = lightTitle,
        separatorHairline = lightSeparator,
        titlePrimary = lightTitle,
        textSecondary = lightTextSecondary,
        textPlaceholder = lightTextPlaceholder,
        iconMuted = lightIconMuted,
        success = lightSuccess,
        onSuccess = pureWhite,
        warning = lightWarning,
        error = lightError,
        onError = pureWhite,
        onDestructive = pureWhite,
        tipHudBackground = lightHudBg,
        onTipHud = pureWhite,
        hudDivider = pureWhite.copy(alpha = 0.25f),
        scrim = lightScrim,
        swipeBackScrim = pureBlack,
        transparent = Color.Transparent,
        heroText = pureWhite,
        heroTextMuted = pureWhite.copy(alpha = 0.9f),
        heroTextSubtle = pureWhite.copy(alpha = 0.8f),
        heroBadgeBackground = pureWhite.copy(alpha = 0.18f),
        heroOverlayStrong = pureWhite.copy(alpha = 0.14f),
        heroOverlaySoft = pureWhite.copy(alpha = 0.08f),
        codeSurface = codePanelBg,
        codeTitle = codePanelTitle,
        codeBody = codePanelBody,
        codeActionText = pureWhite.copy(alpha = 0.85f),
        sliderThumbSurface = pureWhite,
        sliderActiveTick = pureWhite,
        switchThumb = pureWhite,
        onAccentBadge = pureWhite,
        swatchLightLabel = pureWhite,
        swatchDarkLabel = lightTitle,
        swatchLightBackground = pureWhite,
        demoBlendStart = skin.primaryLight,
        demoBlendEnd = lightSuccess,
      )
    }
}

object XuiDimenTemplate {
  fun buildDimens(density: XuiDensityPreset, roundedCards: Boolean): XuiDimens {
    val s = density.scale
    fun scaled(base: Float): Dp = (base * s).dp
    val cardCorner = if (roundedCards) scaled(14f) else 4.dp
    val cardLargeCorner = if (roundedCards) scaled(18f) else 6.dp
    return XuiDimens(
      zero = 0.dp,
      hairline = 0.5.dp,
      borderThin = 1.dp,
      borderMedium = 1.5.dp,
      borderThick = 3.dp,
      spacing1 = 1.dp,
      spacing2 = scaled(2f),
      spacing3 = scaled(3f),
      spacing4 = scaled(4f),
      spacing5 = scaled(5f),
      spacing6 = scaled(6f),
      spacing7 = scaled(7f),
      spacing8 = scaled(8f),
      spacing10 = scaled(10f),
      spacing11 = scaled(11f),
      spacing12 = scaled(12f),
      spacing13 = scaled(13f),
      spacing14 = scaled(14f),
      spacing16 = scaled(16f),
      spacing18 = scaled(18f),
      spacing20 = scaled(20f),
      spacing22 = scaled(22f),
      spacing24 = scaled(24f),
      spacing28 = scaled(28f),
      spacing32 = scaled(32f),
      spacing48 = 48.dp,
      spacing64 = 64.dp,
      spacing88 = 88.dp,
      spacing96 = 96.dp,
      topBarHeight = scaled(52f),
      topBarTitleCenterPadding = 88.dp,
      topBarTitleLeftStartWithBack = 48.dp,
      topBarTitleLeftStartNoBack = 12.dp,
      topBarTitleLeftEndPadding = 96.dp,
      collapsingExpandedHeight = 216.dp,
      collapsingCollapsedHeight = 56.dp,
      collapsingTitleSidePadding = 64.dp,
      listItemMinHeight = scaled(54f),
      listDividerIconIndent = scaled(62f),
      dialogRowMinHeight = scaled(50f),
      sheetRowMinHeight = scaled(52f),
      buttonMinHeight = 44.dp,
      actionRowMinHeight = 48.dp,
      tabHeight = scaled(46f),
      tabPillHeight = scaled(34f),
      tabPillCornerRadius = scaled(17f),
      tabPillHorizontalInset = 4.dp,
      tabPillTopOffset = 6.dp,
      tabIndicatorHeight = 3.dp,
      tabIndicatorCorner = 2.dp,
      tabIndicatorMinWidth = 20.dp,
      tabIndicatorMaxWidth = 48.dp,
      tabIndicatorInsetTotal = 28.dp,
      tabDotSize = 6.dp,
      tabDotBottomOffset = (-4).dp,
      redDotSize = 8.dp,
      badgeDotSize = 7.dp,
      iconXs = 14.dp,
      iconBarSmall = 15.dp,
      iconSm = 16.dp,
      iconButtonLead = 18.dp,
      iconListLead = 19.dp,
      iconMd = 20.dp,
      iconAction = 22.dp,
      iconLg = 24.dp,
      iconXl = 34.dp,
      iconHud = 36.dp,
      iconEmpty = 32.dp,
      iconBoxSm = 30.dp,
      iconBoxMd = 34.dp,
      iconBoxHero = 38.dp,
      iconBoxLg = 52.dp,
      iconBoxXl = 64.dp,
      alphaButtonPreviewSize = 44.dp,
      avatarSize = 88.dp,
      avatarOvalWidth = 118.dp,
      swatchSize = 64.dp,
      cardCornerRadius = cardCorner,
      cardLargeRadius = cardLargeCorner,
      buttonCornerRadius = 22.dp,
      buttonDefaultCornerRadius = 10.dp,
      pillRadius = 20.dp,
      dialogRadius = 16.dp,
      sheetRadius = 20.dp,
      inputRadius = 12.dp,
      chipRadius = 18.dp,
      badgeRadius = 8.dp,
      popupRadius = 12.dp,
      sliderHeight = 40.dp,
      sliderBubbleHeight = 62.dp,
      sliderTrackHeight = 4.dp,
      sliderTrackThick = 8.dp,
      sliderThumbSize = 22.dp,
      sliderTickRadius = 2.dp,
      sliderBubbleMinWidth = 44.dp,
      swipeActionWidth = 76.dp,
      swipeEdgeWidth = 28.dp,
      popupArrowWidth = 16.dp,
      popupArrowHeight = 8.dp,
      popupMinWidth = 160.dp,
      popupListMaxWidth = 228.dp,
      popupMaxWidth = 264.dp,
      quickActionBarHeight = 44.dp,
      quickActionDividerHeight = 18.dp,
      quickActionArrowWidth = 14.dp,
      quickActionArrowHeight = 7.dp,
      dialogMaxWidth = 400.dp,
      hudMinWidth = 136.dp,
      hudMaxWidth = 232.dp,
      contentMaxWidth = 760.dp,
      expandedBreakpoint = 600.dp,
      progressRingSize = 76.dp,
      progressButtonSpinnerSize = 16.dp,
      emptyLoadingSize = 38.dp,
      progressStrokeSm = 2.dp,
      progressStrokePull = 2.2.dp,
      progressStrokeHud = 2.6.dp,
      progressStrokeMd = 3.dp,
      progressStrokeLg = 6.dp,
      shadowXs = 3.dp,
      shadowSm = 4.dp,
      shadowMd = 6.dp,
      shadowPopover = 8.dp,
      shadowLg = 12.dp,
      horizontalPadding = scaled(16f),
    )
  }

  fun buildShapes(dimens: XuiDimens): XuiShapeTokens =
    XuiShapeTokens(
      card = RoundedCornerShape(dimens.cardCornerRadius),
      cardLarge = RoundedCornerShape(dimens.cardLargeRadius),
      buttonDefault = RoundedCornerShape(dimens.buttonDefaultCornerRadius),
      buttonPill = CircleShape,
      dialog = RoundedCornerShape(dimens.dialogRadius),
      sheetTop = RoundedCornerShape(topStart = dimens.sheetRadius, topEnd = dimens.sheetRadius),
      input = RoundedCornerShape(dimens.inputRadius),
      inputDialog = RoundedCornerShape(dimens.buttonDefaultCornerRadius),
      chip = RoundedCornerShape(dimens.chipRadius),
      badge = RoundedCornerShape(dimens.badgeRadius),
      badgeSmall = RoundedCornerShape(dimens.spacing6),
      popup = RoundedCornerShape(dimens.popupRadius),
      quickAction = RoundedCornerShape(dimens.buttonDefaultCornerRadius),
      iconContainer = RoundedCornerShape(dimens.buttonDefaultCornerRadius),
      emptyIcon = RoundedCornerShape(dimens.sheetRadius),
      circle = CircleShape,
    )

  fun buildTextSizes(density: XuiDensityPreset): XuiTextSizes {
    val s = if (density == XuiDensityPreset.COMPACT) 0.96f else 1f
    return XuiTextSizes(
      heroTitle = (22f * s).sp,
      headline = (24f * s).sp,
      titleLg = (18f * s).sp,
      titleMd = (17f * s).sp,
      titleSm = (16f * s).sp,
      bodyLg = (15f * s).sp,
      bodyMd = (14f * s).sp,
      bodySm = (13f * s).sp,
      bodyXs = (12f * s).sp,
      labelLg = (14f * s).sp,
      labelMd = (13f * s).sp,
      labelSm = (11f * s).sp,
      micro = (10f * s).sp,
      lineHeightBody = (21f * s).sp,
      lineHeightBodyCompact = (18f * s).sp,
      lineHeightLink = (22f * s).sp,
      lineHeightCode = (18f * s).sp,
      letterSpacingLabel = 0.3.sp,
      letterSpacingMonogram = 1.sp,
    )
  }
}

object XuiColorHelper {
  fun setColorAlpha(color: Color, alpha: Float): Color = color.copy(alpha = alpha.coerceIn(0f, 1f))

  fun computeColor(fromColor: Color, toColor: Color, fraction: Float): Color {
    val t = fraction.coerceIn(0f, 1f)
    return Color(
      red = fromColor.red + (toColor.red - fromColor.red) * t,
      green = fromColor.green + (toColor.green - fromColor.green) * t,
      blue = fromColor.blue + (toColor.blue - fromColor.blue) * t,
      alpha = fromColor.alpha + (toColor.alpha - fromColor.alpha) * t,
    )
  }

  fun toHexString(color: Color, includeAlpha: Boolean = false): String {
    val a = (color.alpha * 255f).roundToInt().coerceIn(0, 255)
    val r = (color.red * 255f).roundToInt().coerceIn(0, 255)
    val g = (color.green * 255f).roundToInt().coerceIn(0, 255)
    val b = (color.blue * 255f).roundToInt().coerceIn(0, 255)
    return if (includeAlpha) {
      "#${hexByte(a)}${hexByte(r)}${hexByte(g)}${hexByte(b)}"
    } else {
      "#${hexByte(r)}${hexByte(g)}${hexByte(b)}"
    }
  }

  fun isDarkColor(color: Color): Boolean = color.luminance() < 0.45f

  fun contrastRatio(foreground: Color, background: Color): Float {
    val l1 = foreground.luminance() + 0.05f
    val l2 = background.luminance() + 0.05f
    return if (l1 > l2) l1 / l2 else l2 / l1
  }

  private fun hexByte(value: Int): String {
    val hex = value.toString(16).uppercase()
    return if (hex.length == 1) "0$hex" else hex
  }
}
