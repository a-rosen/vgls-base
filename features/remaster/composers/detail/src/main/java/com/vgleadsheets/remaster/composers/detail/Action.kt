package com.vgleadsheets.remaster.composers.detail

import com.vgleadsheets.state.VglsAction

sealed class Action : VglsAction() {
    data class GameClicked(val id: Long) : Action()
    data class SongClicked(val id: Long) : Action()
}
