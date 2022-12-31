package com.vgleadsheets.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.vgleadsheets.composables.LabelDropdownListItem

data class DropdownSettingListModel(
    val settingId: String,
    val name: String,
    val selectedPosition: Int,
    val settingsLabels: List<String>,
    val onNewOptionSelected: (Int) -> Unit,
) : ListModel, ComposableModel {
    override val dataId: Long = settingId.hashCode().toLong()
    override val layoutId = R.layout.list_component_dropdown

    @Composable
    override fun Content(modifier: Modifier) {
        LabelDropdownListItem(
            model = this,
            modifier = modifier
        )
    }
}
