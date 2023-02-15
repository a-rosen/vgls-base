package com.vgleadsheets.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.EmptyListIndicator

data class EmptyStateListModel(
    @DrawableRes val iconId: Int,
    val explanation: String,
    val showCrossOut: Boolean = true
) : ListModel {
    override val dataId = explanation.hashCode().toLong()
    override val layoutId = R.layout.list_component_empty_state

    @Composable
    override fun Content(modifier: Modifier) {
        EmptyListIndicator(
            model = this,
            modifier = modifier
        )
    }
}
