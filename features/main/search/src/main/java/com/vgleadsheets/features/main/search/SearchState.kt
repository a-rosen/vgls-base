package com.vgleadsheets.features.main.search

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.async.AsyncListState

data class SearchState(
    val clickedSong: ImageNameCaptionListModel? = null,
    val clickedGame: ImageNameCaptionListModel? = null,
    val clickedComposer: ImageNameCaptionListModel? = null,
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem(
        "C",
        R.string.part_c,
        R.string.part_long_c,
        true
    ),
    override val listModels: List<ListModel> = emptyList(),
    override val data: SearchData = SearchData()
) : AsyncListState<SearchData>(data = data) {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        data: SearchData
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
