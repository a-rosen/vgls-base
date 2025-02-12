package com.vgleadsheets.composables

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.appcomm.ActionSink
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.composables.previews.PreviewActionSink
import com.vgleadsheets.ui.id
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
@Suppress("LongMethod")
fun MenuItem(
    model: MenuItemListModel,
    actionSink: ActionSink,
    padding: PaddingValues,
    modifier: Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(padding)
            .clickable { actionSink.sendAction(model.clickAction) },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val color = if (model.selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onBackground
        }

        Icon(
            painterResource(id = model.icon.id()),
            tint = color,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 4.dp)
                .padding(vertical = 12.dp)
        )

        Spacer(
            modifier = Modifier.width(12.dp)
        )

        Text(
            text = model.name,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = if (model.selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier
                .weight(1.0f)
                .padding(
                    top = 16.dp,
                    bottom = 16.dp
                )
        )
    }
}

@Preview
@Composable
private fun Selected() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
        }
    }
}

@Preview
@Composable
private fun NotSelected() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(false)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SelectedDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(true)
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun NotSelectedDark() {
    VglsMaterial {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample(false)
        }
    }
}

@Composable
private fun Sample(selected: Boolean) {
    MenuItem(
        MenuItemListModel(
            name = "Check for updates...",
            caption = "Last updated Feb 3, 1963",
            icon = com.vgleadsheets.ui.Icon.REFRESH,
            selected = selected,
            clickAction = VglsAction.Noop
        ),
        PreviewActionSink {},
        PaddingValues(),
        Modifier,
    )
}
