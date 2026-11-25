package com.xui.cmp.showcase

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Widgets
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xui.cmp.di.XuiComponentCatalog
import com.xui.cmp.di.XuiSkinManager
import com.xui.cmp.di.XuiSkinState
import com.xui.cmp.platform.PlatformBackHandler
import com.xui.cmp.theme.XuiTheme
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiResourceManager
import com.xui.cmp.tokens.XuiResourceState
import com.xui.cmp.tokens.XuiStrings
import com.xui.cmp.ui.components.XuiBottomSheetHost
import com.xui.cmp.ui.components.XuiDialogHost
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

enum class XuiRootTab(
  val activeIcon: ImageVector,
  val inactiveIcon: ImageVector,
) {
  COMPONENTS(Icons.Filled.Widgets, Icons.Outlined.Widgets),
  ARCHITECTURE(Icons.Filled.Hub, Icons.Outlined.Hub),
  HELPERS(Icons.Filled.Build, Icons.Outlined.Build),
}

private fun XuiRootTab.labelFor(strings: XuiStrings): String =
  when (this) {
    XuiRootTab.COMPONENTS -> strings.nav.tabComponents
    XuiRootTab.ARCHITECTURE -> strings.nav.tabArchitecture
    XuiRootTab.HELPERS -> strings.nav.tabHelpers
  }

@Composable
fun XuiCmpRootApp(
  typography: Typography = MaterialTheme.typography,
) {
  val skinManager: XuiSkinManager = koinInject()
  val resourceManager: XuiResourceManager = koinInject()
  val catalog: XuiComponentCatalog = koinInject()
  val skinState by skinManager.state.collectAsStateWithLifecycle()
  val resourceState by resourceManager.state.collectAsStateWithLifecycle()

  val componentsVm: ComponentsLabViewModel = koinViewModel()
  val architectureVm: ArchitectureLabViewModel = koinViewModel()
  val helpersVm: HelpersLabViewModel = koinViewModel()

  val componentsState by componentsVm.uiState.collectAsStateWithLifecycle()
  val architectureState by architectureVm.uiState.collectAsStateWithLifecycle()
  val helpersState by helpersVm.uiState.collectAsStateWithLifecycle()

  var currentTabName by rememberSaveable { mutableStateOf(XuiRootTab.COMPONENTS.name) }
  val currentTab = XuiRootTab.entries.firstOrNull { it.name == currentTabName } ?: XuiRootTab.COMPONENTS

  XuiTheme(
    skinPreset = skinState.activeSkin,
    themeMode = skinState.themeMode,
    densityPreset = resourceState.densityPreset,
    roundedCards = skinState.roundedCards,
    strings = resourceState.strings,
    typography = typography,
  ) {
    val colors = XuiThemeTokens.colors
    val dimens = XuiThemeTokens.dimens
    val strings = XuiThemeTokens.strings
    val activeDetailId = componentsState.activeDetailId

    if (activeDetailId != null) {
      PlatformBackHandler(enabled = true) {
        componentsVm.closeComponentDetail()
      }
    }

    Box(modifier = Modifier.fillMaxSize().background(colors.canvasBackground)) {
      if (activeDetailId != null) {
        val spec = catalog.findById(activeDetailId)
        ComponentDetailPlaygroundScreen(
          spec = spec,
          uiState = componentsState,
          viewModel = componentsVm,
          onBack = { componentsVm.closeComponentDetail() },
        )
      } else {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
          val isExpanded = maxWidth >= dimens.expandedBreakpoint

          if (isExpanded) {
            Row(modifier = Modifier.fillMaxSize()) {
              NavigationRail(
                containerColor = colors.cardSurface,
                contentColor = colors.titlePrimary,
                modifier = Modifier.fillMaxHeight(),
              ) {
                Spacer(modifier = Modifier.weight(1f))
                XuiRootTab.entries.forEach { tab ->
                  val selected = currentTab == tab
                  val tabLabel = tab.labelFor(strings)
                  NavigationRailItem(
                    selected = selected,
                    onClick = { currentTabName = tab.name },
                    icon = {
                      Icon(
                        imageVector = if (selected) tab.activeIcon else tab.inactiveIcon,
                        contentDescription = tabLabel,
                      )
                    },
                    label = {
                      Text(
                        text = tabLabel,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                      )
                    },
                    colors =
                      NavigationRailItemDefaults.colors(
                        selectedIconColor = colors.brandPrimary,
                        selectedTextColor = colors.brandPrimary,
                        indicatorColor = colors.brandContainer,
                        unselectedIconColor = colors.iconMuted,
                        unselectedTextColor = colors.textSecondary,
                      ),
                    modifier = Modifier.testTag("nav_rail_${tab.name}"),
                  )
                }
                Spacer(modifier = Modifier.weight(1f))
              }

              Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                RootTabContent(
                  currentTab = currentTab,
                  componentsState = componentsState,
                  architectureState = architectureState,
                  helpersState = helpersState,
                  skinState = skinState,
                  resourceState = resourceState,
                  componentsVm = componentsVm,
                  architectureVm = architectureVm,
                  helpersVm = helpersVm,
                  onNavigateToArchitecture = { currentTabName = XuiRootTab.ARCHITECTURE.name },
                )
              }
            }
          } else {
            Scaffold(
              containerColor = colors.canvasBackground,
              contentWindowInsets = WindowInsets(0, 0, 0, 0),
              bottomBar = {
                NavigationBar(
                  containerColor = colors.cardSurface,
                  contentColor = colors.titlePrimary,
                  windowInsets = WindowInsets.navigationBars,
                ) {
                  XuiRootTab.entries.forEach { tab ->
                    val selected = currentTab == tab
                    val tabLabel = tab.labelFor(strings)
                    NavigationBarItem(
                      selected = selected,
                      onClick = { currentTabName = tab.name },
                      icon = {
                        Icon(
                          imageVector = if (selected) tab.activeIcon else tab.inactiveIcon,
                          contentDescription = tabLabel,
                        )
                      },
                      label = {
                        Text(
                          text = tabLabel,
                          fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        )
                      },
                      colors =
                        NavigationBarItemDefaults.colors(
                          selectedIconColor = colors.brandPrimary,
                          selectedTextColor = colors.brandPrimary,
                          indicatorColor = colors.brandContainer,
                          unselectedIconColor = colors.iconMuted,
                          unselectedTextColor = colors.textSecondary,
                        ),
                      modifier = Modifier.testTag("nav_bar_${tab.name}"),
                    )
                  }
                }
              },
            ) { innerPadding ->
              Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                RootTabContent(
                  currentTab = currentTab,
                  componentsState = componentsState,
                  architectureState = architectureState,
                  helpersState = helpersState,
                  skinState = skinState,
                  resourceState = resourceState,
                  componentsVm = componentsVm,
                  architectureVm = architectureVm,
                  helpersVm = helpersVm,
                  onNavigateToArchitecture = { currentTabName = XuiRootTab.ARCHITECTURE.name },
                )
              }
            }
          }
        }
      }

      XuiDialogHost()
      XuiBottomSheetHost()
    }
  }
}

@Composable
private fun RootTabContent(
  currentTab: XuiRootTab,
  componentsState: ComponentsLabUiState,
  architectureState: ArchitectureLabUiState,
  helpersState: HelpersLabUiState,
  skinState: XuiSkinState,
  resourceState: XuiResourceState,
  componentsVm: ComponentsLabViewModel,
  architectureVm: ArchitectureLabViewModel,
  helpersVm: HelpersLabViewModel,
  onNavigateToArchitecture: () -> Unit,
) {
  when (currentTab) {
    XuiRootTab.COMPONENTS -> {
      ComponentsCatalogScreen(
        uiState = componentsState,
        skinState = skinState,
        platformName = architectureState.platformName,
        onSelectCategory = { componentsVm.selectCategory(it) },
        onSearchChange = { componentsVm.updateSearchQuery(it) },
        onOpenComponent = { componentsVm.openComponentDetail(it) },
        onQuickThemeAction = onNavigateToArchitecture,
      )
    }
    XuiRootTab.ARCHITECTURE -> {
      ArchitectureKoinScreen(
        uiState = architectureState,
        skinState = skinState,
        resourceState = resourceState,
        onSelectSkin = { architectureVm.selectSkin(it) },
        onSelectThemeMode = { architectureVm.selectThemeMode(it) },
        onToggleRoundedCards = { architectureVm.setRoundedCards(it) },
        onSelectDensity = { architectureVm.selectDensity(it) },
        onApplyBrandToken = { architectureVm.applyBrandToken(it) },
        onToggleRule = { architectureVm.toggleReplacementRule(it) },
        onResetRules = { architectureVm.resetTemplateWorkshop() },
      )
    }
    XuiRootTab.HELPERS -> {
      HelpersLabScreen(
        uiState = helpersState,
        viewModel = helpersVm,
      )
    }
  }
}
