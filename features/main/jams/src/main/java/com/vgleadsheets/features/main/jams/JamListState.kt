package com.vgleadsheets.features.main.jams

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.PersistState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.list.ListState
import com.vgleadsheets.features.main.list.R
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.perf.tracking.common.LoadStatus

data class JamListState(
    override val updateTime: Async<*> = Uninitialized,
    override val digest: Async<*> = Uninitialized,
    override val selectedPart: PartSelectorItem? = PartSelectorItem("C", R.string.part_c, true),
    override val listModels: List<ListModel> = emptyList(),
    @PersistState override val loadStatus: LoadStatus = LoadStatus(),
    override val data: Async<List<Jam>> = Uninitialized,
    val clickedCtaModel: CtaListModel? = null,
    val clickedJamModel: NameCaptionListModel? = null
) : ListState<Jam>() {
    override fun updateListState(
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?,
        listModels: List<ListModel>,
        loadStatus: LoadStatus,
        data: Async<List<Jam>>
    ) = copy(
        updateTime = updateTime,
        digest = digest,
        selectedPart = selectedPart,
        listModels = listModels,
        loadStatus = loadStatus,
        data = data
    )
}
