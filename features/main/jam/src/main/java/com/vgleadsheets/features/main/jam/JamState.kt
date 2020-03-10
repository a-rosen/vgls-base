package com.vgleadsheets.features.main.jam

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry

data class JamState(
    val jamId: Long,
    val jam: Async<Jam> = Uninitialized,
    val jamRefresh: Async<ApiJam> = Uninitialized,
    val setlist: Async<List<SetlistEntry>> = Uninitialized,
    val setlistRefresh: Async<List<Long>> = Uninitialized,
    val deletion: Async<Unit> = Uninitialized
) : MvRxState {
    constructor(idArgs: IdArgs) : this(idArgs.id)
}