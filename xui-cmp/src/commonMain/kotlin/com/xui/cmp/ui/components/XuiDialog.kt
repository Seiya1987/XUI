package com.xui.cmp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xui.cmp.di.XuiActiveDialog
import com.xui.cmp.di.XuiDialogController
import com.xui.cmp.di.XuiTipController
import com.xui.cmp.di.XuiTipState
import com.xui.cmp.di.XuiTipType
import com.xui.cmp.theme.XuiThemeTokens
import com.xui.cmp.tokens.XuiTemplateFormatter
import org.koin.compose.koinInject

@Composable
fun XuiDialogHost(
  dialogController: XuiDialogController = koinInject(),
  tipController: XuiTipController = koinInject(),
) {
  val activeDialog by dialogController.activeDialog.collectAsStateWithLifecycle()
  val tipState by tipController.tipState.collectAsStateWithLifecycle()

  activeDialog?.let { dialog ->
    XuiDialogContainer(onDismissRequest = { dialogController.dismissDialog() }) {
      when (dialog) {
        is XuiActiveDialog.Message -> {
          XuiMessageDialogContent(
            dialog = dialog,
            onDismiss = { dialogController.dismissDialog() },
          )
        }
        is XuiActiveDialog.Input -> {
          XuiInputDialogContent(
            dialog = dialog,
            onDismiss = { dialogController.dismissDialog() },
          )
        }
        is XuiActiveDialog.SingleChoice -> {
          XuiSingleChoiceDialogContent(
            dialog = dialog,
            onDismiss = { dialogController.dismissDialog() },
          )
        }
        is XuiActiveDialog.MultiChoice -> {
          XuiMultiChoiceDialogContent(
            dialog = dialog,
            onDismiss = { dialogController.dismissDialog() },
          )
        }
      }
    }
  }

  XuiTipDialogOverlay(
    state = tipState,
    onDismiss = { tipController.dismiss() },
  )
}

@Composable
private fun XuiDialogContainer(
  onDismissRequest: () -> Unit,
  content: @Composable () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val motion = XuiThemeTokens.motion
  Dialog(
    onDismissRequest = onDismissRequest,
    properties = DialogProperties(usePlatformDefaultWidth = false),
  ) {
    Surface(
      modifier = Modifier.fillMaxWidth(motion.dialogWidthFraction).widthIn(max = dimens.dialogMaxWidth),
      shape = shapes.dialog,
      color = colors.cardSurface,
      border = BorderStroke(dimens.hairline, colors.separatorHairline),
      shadowElevation = dimens.shadowLg,
    ) {
      content()
    }
  }
}

@Composable
private fun XuiMessageDialogContent(
  dialog: XuiActiveDialog.Message,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  val positiveLabel = dialog.positiveText.ifEmpty { strings.common.confirm }
  val negativeLabel = dialog.negativeText?.let { it.ifEmpty { strings.common.cancel } }

  Column(modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier =
        Modifier.fillMaxWidth().padding(horizontal = dimens.spacing22, vertical = dimens.spacing22)
    ) {
      Text(
        text = dialog.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = textSizes.titleMd,
        ),
        color = colors.titlePrimary,
      )
      Spacer(modifier = Modifier.height(dimens.spacing10))
      Text(
        text = dialog.message,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontSize = textSizes.bodyMd,
          lineHeight = textSizes.lineHeightBody,
        ),
        color = colors.textSecondary,
      )
    }

    XuiDivider()

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
      if (!negativeLabel.isNullOrBlank()) {
        XuiDialogActionButton(
          text = negativeLabel,
          color = colors.textSecondary,
          modifier = Modifier.weight(1f).testTag("xui_dialog_negative"),
          onClick = onDismiss,
        )
        VerticalDivider(
          modifier = Modifier.fillMaxHeight(),
          thickness = dimens.hairline,
          color = colors.separatorHairline,
        )
      }
      XuiDialogActionButton(
        text = positiveLabel,
        color = if (dialog.destructive) colors.error else colors.brandPrimary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.weight(1f).testTag("xui_dialog_positive"),
        onClick = {
          onDismiss()
          dialog.onConfirm()
        },
      )
    }
  }
}

@Composable
private fun XuiInputDialogContent(
  dialog: XuiActiveDialog.Input,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  var textValue by remember(dialog.initialValue) { mutableStateOf(dialog.initialValue) }
  val confirmLabel = dialog.confirmText.ifEmpty { strings.common.ok }

  Column(modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier =
        Modifier.fillMaxWidth().padding(horizontal = dimens.spacing22, vertical = dimens.spacing20)
    ) {
      Text(
        text = dialog.title,
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Bold,
          fontSize = textSizes.titleMd,
        ),
        color = colors.titlePrimary,
      )
      Spacer(modifier = Modifier.height(dimens.spacing14))
      OutlinedTextField(
        value = textValue,
        onValueChange = { textValue = it },
        singleLine = true,
        placeholder = { Text(dialog.placeholder, color = colors.textPlaceholder) },
        shape = shapes.inputDialog,
        colors =
          OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colors.brandPrimary,
            unfocusedBorderColor = colors.separatorHairline,
            focusedTextColor = colors.titlePrimary,
            unfocusedTextColor = colors.titlePrimary,
          ),
        modifier = Modifier.fillMaxWidth().testTag("xui_dialog_input"),
      )
    }

    XuiDivider()

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
      XuiDialogActionButton(
        text = strings.common.cancel,
        color = colors.textSecondary,
        modifier = Modifier.weight(1f),
        onClick = onDismiss,
      )
      VerticalDivider(
        modifier = Modifier.fillMaxHeight(),
        thickness = dimens.hairline,
        color = colors.separatorHairline,
      )
      XuiDialogActionButton(
        text = confirmLabel,
        color = colors.brandPrimary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.weight(1f).testTag("xui_dialog_input_confirm"),
        onClick = {
          onDismiss()
          dialog.onConfirm(textValue)
        },
      )
    }
  }
}

@Composable
private fun XuiSingleChoiceDialogContent(
  dialog: XuiActiveDialog.SingleChoice,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = dialog.title,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = textSizes.titleMd,
      ),
      color = colors.titlePrimary,
      modifier = Modifier.padding(horizontal = dimens.spacing22, vertical = dimens.spacing18),
    )
    XuiDivider()

    dialog.options.forEachIndexed { index, option ->
      val selected = index == dialog.selectedIndex
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .defaultMinSize(minHeight = dimens.dialogRowMinHeight)
            .clickable {
              onDismiss()
              dialog.onSelect(index)
            }
            .padding(horizontal = dimens.spacing22, vertical = dimens.spacing12),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
      ) {
        Text(
          text = option,
          style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
          ),
          color = if (selected) colors.brandPrimary else colors.titlePrimary,
        )
        if (selected) {
          Icon(
            imageVector = Icons.Default.Check,
            contentDescription = strings.common.selectedContentDescription,
            tint = colors.brandPrimary,
            modifier = Modifier.size(dimens.iconMd),
          )
        }
      }
      if (index < dialog.options.lastIndex) {
        XuiDivider(startIndent = dimens.spacing22)
      }
    }

    XuiDivider()
    XuiDialogActionButton(
      text = strings.common.close,
      color = colors.textSecondary,
      modifier = Modifier.fillMaxWidth(),
      onClick = onDismiss,
    )
  }
}

@Composable
private fun XuiMultiChoiceDialogContent(
  dialog: XuiActiveDialog.MultiChoice,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val strings = XuiThemeTokens.strings
  var selectedSet by remember(dialog.selectedIndices) { mutableStateOf(dialog.selectedIndices) }

  Column(modifier = Modifier.fillMaxWidth()) {
    Text(
      text = dialog.title,
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = textSizes.titleMd,
      ),
      color = colors.titlePrimary,
      modifier = Modifier.padding(horizontal = dimens.spacing22, vertical = dimens.spacing18),
    )
    XuiDivider()

    dialog.options.forEachIndexed { index, option ->
      val checked = index in selectedSet
      Row(
        modifier =
          Modifier.fillMaxWidth()
            .defaultMinSize(minHeight = dimens.dialogRowMinHeight)
            .clickable {
              selectedSet = if (checked) selectedSet - index else selectedSet + index
            }
            .padding(horizontal = dimens.spacing22, vertical = dimens.spacing12),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        Icon(
          imageVector =
            if (checked) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
          contentDescription = null,
          tint = if (checked) colors.brandPrimary else colors.textPlaceholder,
          modifier = Modifier.size(dimens.iconMd),
        )
        Spacer(modifier = Modifier.width(dimens.spacing12))
        Text(
          text = option,
          style = MaterialTheme.typography.bodyLarge,
          color = colors.titlePrimary,
        )
      }
      if (index < dialog.options.lastIndex) {
        XuiDivider(startIndent = dimens.spacing22)
      }
    }

    XuiDivider()
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
      XuiDialogActionButton(
        text = strings.common.cancel,
        color = colors.textSecondary,
        modifier = Modifier.weight(1f),
        onClick = onDismiss,
      )
      VerticalDivider(
        modifier = Modifier.fillMaxHeight(),
        thickness = dimens.hairline,
        color = colors.separatorHairline,
      )
      XuiDialogActionButton(
        text =
          XuiTemplateFormatter.format(
            strings.dialogs.multiChoiceConfirmTemplate,
            "count" to selectedSet.size,
          ),
        color = colors.brandPrimary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.weight(1f),
        onClick = {
          onDismiss()
          dialog.onConfirm(selectedSet)
        },
      )
    }
  }
}

@Composable
private fun XuiDialogActionButton(
  text: String,
  color: Color,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  fontWeight: FontWeight = FontWeight.Medium,
) {
  val dimens = XuiThemeTokens.dimens
  val textSizes = XuiThemeTokens.textSizes
  val interactionSource = remember { MutableInteractionSource() }
  Box(
    modifier =
      modifier
        .defaultMinSize(minHeight = dimens.actionRowMinHeight)
        .clickable(
          interactionSource = interactionSource,
          indication = ripple(bounded = true),
          onClick = onClick,
        )
        .padding(horizontal = dimens.spacing12, vertical = dimens.spacing13),
    contentAlignment = Alignment.Center,
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelLarge.copy(
        fontSize = textSizes.bodyLg,
        fontWeight = fontWeight,
      ),
      color = color,
      maxLines = 1,
    )
  }
}

@Composable
fun XuiTipDialogOverlay(
  state: XuiTipState?,
  onDismiss: () -> Unit,
) {
  val colors = XuiThemeTokens.colors
  val dimens = XuiThemeTokens.dimens
  val shapes = XuiThemeTokens.shapes
  val textSizes = XuiThemeTokens.textSizes
  val motion = XuiThemeTokens.motion
  val strings = XuiThemeTokens.strings

  AnimatedVisibility(
    visible = state != null,
    enter = fadeIn() + scaleIn(initialScale = motion.dialogEnterScale),
    exit = fadeOut() + scaleOut(targetScale = motion.dialogExitScale),
  ) {
    if (state != null) {
      Box(
        modifier =
          Modifier.fillMaxSize()
            .then(
              if (state.cancelable) {
                Modifier.clickable(
                  interactionSource = remember { MutableInteractionSource() },
                  indication = null,
                  onClick = onDismiss,
                )
              } else Modifier
            ),
        contentAlignment = Alignment.Center,
      ) {
        Surface(
          shape = shapes.dialog,
          color = colors.tipHudBackground,
          shadowElevation = dimens.shadowPopover,
          modifier =
            Modifier.widthIn(min = dimens.hudMinWidth, max = dimens.hudMaxWidth)
              .testTag("xui_tip_hud"),
        ) {
          Column(
            modifier = Modifier.padding(horizontal = dimens.spacing24, vertical = dimens.spacing22),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            when (state.type) {
              XuiTipType.LOADING -> {
                CircularProgressIndicator(
                  color = colors.onTipHud,
                  strokeWidth = dimens.progressStrokeHud,
                  modifier = Modifier.size(dimens.iconXl),
                )
              }
              XuiTipType.SUCCESS -> {
                Icon(
                  imageVector = Icons.Default.CheckCircleOutline,
                  contentDescription = strings.dialogs.tipSuccessDesc,
                  tint = colors.onTipHud,
                  modifier = Modifier.size(dimens.iconHud),
                )
              }
              XuiTipType.FAIL -> {
                Icon(
                  imageVector = Icons.Default.ErrorOutline,
                  contentDescription = strings.dialogs.tipFailDesc,
                  tint = colors.onTipHud,
                  modifier = Modifier.size(dimens.iconHud),
                )
              }
              XuiTipType.INFO -> {
                Icon(
                  imageVector = Icons.Default.Info,
                  contentDescription = strings.dialogs.tipInfoDesc,
                  tint = colors.onTipHud,
                  modifier = Modifier.size(dimens.iconHud),
                )
              }
            }
            Spacer(modifier = Modifier.height(dimens.spacing12))
            Text(
              text = state.message,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = textSizes.bodyMd,
              ),
              color = colors.onTipHud,
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }
  }
}
