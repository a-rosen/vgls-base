package com.vgleadsheets.ui.list

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgleadsheets.composables.Content
import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ListScreen(
    stateSource: StateFlow<ListState>,
    stringProvider: StringProvider,
    actionHandler: (ListAction) -> Unit,
    titleUpdater: (String?) -> Unit,
    modifier: Modifier
) {
    val state by stateSource.collectAsStateWithLifecycle()

    val title = state.title()
    val items = state.toListItems(stringProvider, actionHandler)

    if (title != null) {
        LaunchedEffect(Unit) {
            titleUpdater(title)
        }
    }

    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() }
        ) {
            it.Content(modifier = Modifier)
        }
    }
}
