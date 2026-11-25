package com.xui.cmp.tokens

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class XuiResourceState(
  val densityPreset: XuiDensityPreset = XuiDensityPreset.STANDARD,
  val brandToken: String = XuiStringTemplateDefaults.DEFAULT_BRAND_TOKEN,
  val rules: List<XuiStringReplacementRule> = XuiStringTemplateDefaults.defaultRules(),
  val strings: XuiStrings =
    XuiStringResolver.resolve(
      brandToken = XuiStringTemplateDefaults.DEFAULT_BRAND_TOKEN,
      rules = XuiStringTemplateDefaults.defaultRules(),
    ),
)

interface XuiResourceManager {
  val state: StateFlow<XuiResourceState>

  fun selectDensity(preset: XuiDensityPreset)

  fun updateBrandToken(brand: String)

  fun toggleRule(ruleId: String)

  fun upsertCustomRule(rule: XuiStringReplacementRule)

  fun resetAll()
}

class DefaultXuiResourceManager : XuiResourceManager {
  private val _state = MutableStateFlow(XuiResourceState())
  override val state: StateFlow<XuiResourceState> = _state.asStateFlow()

  override fun selectDensity(preset: XuiDensityPreset) {
    _state.update { it.copy(densityPreset = preset) }
  }

  override fun updateBrandToken(brand: String) {
    val sanitized = brand.trim().ifEmpty { XuiStringTemplateDefaults.DEFAULT_BRAND_TOKEN }
    _state.update { current ->
      current.copy(
        brandToken = sanitized,
        strings = XuiStringResolver.resolve(sanitized, current.rules),
      )
    }
  }

  override fun toggleRule(ruleId: String) {
    _state.update { current ->
      val updatedRules =
        current.rules.map { rule ->
          if (rule.id == ruleId) rule.copy(enabled = !rule.enabled) else rule
        }
      current.copy(
        rules = updatedRules,
        strings = XuiStringResolver.resolve(current.brandToken, updatedRules),
      )
    }
  }

  override fun upsertCustomRule(rule: XuiStringReplacementRule) {
    _state.update { current ->
      val exists = current.rules.any { it.id == rule.id }
      val updatedRules =
        if (exists) {
          current.rules.map { if (it.id == rule.id) rule else it }
        } else {
          current.rules + rule
        }
      current.copy(
        rules = updatedRules,
        strings = XuiStringResolver.resolve(current.brandToken, updatedRules),
      )
    }
  }

  override fun resetAll() {
    val defaultRules = XuiStringTemplateDefaults.defaultRules()
    val defaultBrand = XuiStringTemplateDefaults.DEFAULT_BRAND_TOKEN
    _state.value =
      XuiResourceState(
        densityPreset = XuiDensityPreset.STANDARD,
        brandToken = defaultBrand,
        rules = defaultRules,
        strings = XuiStringResolver.resolve(defaultBrand, defaultRules),
      )
  }
}
