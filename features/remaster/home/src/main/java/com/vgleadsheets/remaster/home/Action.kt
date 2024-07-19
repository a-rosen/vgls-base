package com.vgleadsheets.remaster.home

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class MostSongsGameClicked(val gameId: Long) : Action()
    data class MostSongsComposerClicked(val composerId: Long) : VglsAction()
    data class MostPlaysGameClicked(val id: Long) : VglsAction()
}
