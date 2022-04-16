package com.vgleadsheets.features.main.songs

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.model.song.Song

data class SongListState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem(
        "C",
        R.string.part_c,
        R.string.part_long_c,
        true
    ),
    override val listModels: List<ListModel> = emptyList(),
    override val data: Async<List<Song>> = Uninitialized,
    val clickedListModel: ImageNameCaptionListModel? = null
) : ListState<Song>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        data: Async<List<Song>>
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        data = data
    )
}
