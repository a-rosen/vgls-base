package com.vgleadsheets.components

import com.vgleadsheets.ui.Icon

data class ImageNameListModel(
    override val dataId: Long,
    val name: String,
    val imageUrl: String?,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val onClick: () -> Unit
) : ListModel() {
    override val columns = ListModel.COLUMNS_ALL
}
