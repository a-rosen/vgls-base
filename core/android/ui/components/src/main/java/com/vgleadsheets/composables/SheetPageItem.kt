package com.vgleadsheets.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.components.SheetPageListModel
import com.vgleadsheets.composables.subs.CrossfadeSheet
import com.vgleadsheets.images.LoadingIndicatorConfig

@Composable
fun SheetPageItem(
    model: SheetPageListModel,
    actionSink: ActionSink,
    fillMaxWidth: Boolean,
    showDebug: Boolean,
    modifier: Modifier,
    padding: PaddingValues,
) {
    CrossfadeSheet(
        sourceInfo = model.sourceInfo,
        loadingIndicatorConfig = LoadingIndicatorConfig(
            model.title,
            model.gameName,
            model.composers,
            model.pageNumber,
        ),
        sheetId = model.dataId,
        fillMaxWidth = fillMaxWidth,
        showDebug = showDebug,
        modifier = modifier
            .padding(padding)
    )
}
