package com.vgleadsheets.ui.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vgleadsheets.composables.Content
import com.vgleadsheets.list.ListAction
import com.vgleadsheets.list.ListState
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.StateFlow

@Composable
fun GridScreen(
    stateSource: StateFlow<ListState>,
    stringProvider: StringProvider,
    actionHandler: (ListAction) -> Unit,
    titleUpdater: (String?) -> Unit,
    modifier: Modifier,
    minSize: Dp = 128.dp
) {
    val state by stateSource.collectAsStateWithLifecycle()

    val title = state.title()
    val items = state.toListItems(stringProvider, actionHandler)

    if (title != null) {
        LaunchedEffect(Unit) {
            titleUpdater(title)
        }
    }

    LazyVerticalGrid(
        contentPadding = PaddingValues(horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)),
        columns = GridCells.Adaptive(minSize),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(
            items = items,
            key = { it.dataId },
            contentType = { it.layoutId() },
            span = {
                if (it.columns < 1) {
                    GridItemSpan(maxLineSpan)
                } else {
                    GridItemSpan(it.columns)
                }
            }
        ) {
            it.Content(modifier = Modifier)
        }
    }
}
