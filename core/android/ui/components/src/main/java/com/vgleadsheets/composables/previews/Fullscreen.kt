package com.vgleadsheets.composables.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import com.vgleadsheets.ui.themes.VglsMaterial

@Composable
fun FullscreenBlack(
    content: @Composable BoxScope.() -> Unit
) {
    VglsMaterial {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black),
            content = content
        )
    }
}

@Composable
fun FullScreenOf(
    darkTheme: Boolean = false,
    count: Int = 20,
    content: @Composable (ColumnScope.(PaddingValues) -> Unit),
) {
    VglsMaterial(forceDark = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            repeat(count) {
                content(
                    PaddingValues(
                        horizontal = dimensionResource(id = com.vgleadsheets.ui.core.R.dimen.margin_side)
                    )
                )
            }
        }
    }
}
