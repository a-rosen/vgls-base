package com.vgleadsheets.features.main.jams

import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.features.main.list.ListItemClicks

class Clicks(
    private val router: FragmentRouter,
) : ListItemClicks {
    fun findJam() {
        router.showFindJamDialog()
    }

    fun jam(id: Long) {
        router.showJamDetailViewer(id)
    }
}