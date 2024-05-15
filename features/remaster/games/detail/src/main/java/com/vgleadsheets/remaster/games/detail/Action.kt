package com.vgleadsheets.remaster.games.detail

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class ComposerClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
}
