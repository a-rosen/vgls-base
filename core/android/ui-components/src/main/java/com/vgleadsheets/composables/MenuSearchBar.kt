package com.vgleadsheets.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.components.MenuSearchListModel
import com.vgleadsheets.components.R
import com.vgleadsheets.composables.subs.MenuActionIcon
import com.vgleadsheets.ui.themes.VglsMaterialMenu

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuSearchBar(
    model: MenuSearchListModel,
    modifier: Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        MenuActionIcon(
            iconId = com.vgleadsheets.vectors.R.drawable.ic_arrow_back_black_24dp,
            onClick = model.onMenuButtonClick
        )

        var text by remember { mutableStateOf(model.searchQuery) }

        TextField(
            value = text ?: "",
            onValueChange = {
                text = it
                model.onTextEntered(it)
            },
            label = { Text(stringResource(id = R.string.hint_search)) },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 4.dp)
        )

        MenuActionIcon(
            onClick = {
                text = ""
                model.onClearClick()
            },
            iconId = com.vgleadsheets.vectors.R.drawable.ic_clear_black_24dp
        )
    }
}

@Preview
@Composable
private fun EmptyState() {
    VglsMaterialMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleEmpty()
        }
    }
}

@Preview
@Composable
private fun TextEntered() {
    VglsMaterialMenu {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            SampleText()
        }
    }
}

@Composable
private fun SampleEmpty() {
    MenuSearchBar(
        MenuSearchListModel(
            "",
            {},
            {},
            {}
        ),
        Modifier
    )
}

@Composable
private fun SampleText() {
    MenuSearchBar(
        MenuSearchListModel(
            "all the bops",
            {},
            {},
            {}
        ),
        Modifier
    )
}
