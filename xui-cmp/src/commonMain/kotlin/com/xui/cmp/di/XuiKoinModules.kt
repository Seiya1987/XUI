package com.xui.cmp.di

import com.xui.cmp.platform.platformKoinModule
import com.xui.cmp.showcase.ArchitectureLabViewModel
import com.xui.cmp.showcase.ComponentsLabViewModel
import com.xui.cmp.showcase.HelpersLabViewModel
import com.xui.cmp.tokens.DefaultXuiResourceManager
import com.xui.cmp.tokens.XuiResourceManager
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools

val xuiCoreModule: Module = module {
  singleOf(::DefaultXuiResourceManager) bind XuiResourceManager::class
  singleOf(::DefaultXuiSkinManager) bind XuiSkinManager::class
  single<XuiTipController> { DefaultXuiTipController(resourceManager = get()) }
  singleOf(::DefaultXuiDialogController) bind XuiDialogController::class
  single<XuiComponentCatalog> { DefaultXuiComponentCatalog(resourceManager = get()) }
}

val xuiShowcaseModule: Module = module {
  viewModelOf(::ComponentsLabViewModel)
  viewModelOf(::ArchitectureLabViewModel)
  viewModelOf(::HelpersLabViewModel)
}

fun allXuiModules(): List<Module> = listOf(xuiCoreModule, xuiShowcaseModule, platformKoinModule())

fun initXuiKoin(appDeclaration: KoinAppDeclaration = {}) {
  if (KoinPlatformTools.defaultContext().getOrNull() != null) return
  startKoin {
    appDeclaration()
    modules(allXuiModules())
  }
}
