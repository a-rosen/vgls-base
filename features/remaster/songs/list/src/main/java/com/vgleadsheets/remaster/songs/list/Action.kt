package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.appcomm.VglsAction

sealed class Action : VglsAction() {
    data class SongClicked(val id: Long) : Action()
}
