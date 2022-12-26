package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import com.vgleadsheets.composables.EmptyListIndicator

data class MenuErrorStateListModel(
    val failedOperationName: String,
    val errorString: String,
) : ListModel, ComposableModel {
    override val dataId = errorString.hashCode().toLong()
    override val layoutId = R.layout.composable_list_item

    @Composable
    override fun Content() {
        EmptyListIndicator(
            model = this
        )
    }
}

