package com.vgleadsheets.components

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.images.SourceInfo
import com.vgleadsheets.ui.Icon

data class ImageNameListModel(
    override val dataId: Long,
    val name: String,
    val sourceInfo: SourceInfo,
    val imagePlaceholder: Icon,
    val actionableId: Long? = null,
    val clickAction: VglsAction
) : ListModel() {
    override val columns = COLUMNS_ALL
}
