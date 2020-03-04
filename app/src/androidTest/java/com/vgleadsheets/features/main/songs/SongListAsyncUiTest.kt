package com.vgleadsheets.features.main.songs

import com.vgleadsheets.features.main.ListUiTest
import com.vgleadsheets.features.main.hud.HudFragment
import com.vgleadsheets.features.main.viewer.viewer
import org.junit.Test

class SongListAsyncUiTest : ListUiTest() {
    override val screenId = HudFragment.TOP_LEVEL_SCREEN_ID_SONG
    override val startingTopLevelScreenSubtitleId = R.string.subtitle_all_sheets

    @Test
    fun showEmptyStateIfNoData() {
        setApiToReturnBlank()

        songList(this) {
            checkIsEmptyStateDisplayed(LABEL_EMPTY_STATE)
        }
    }

    @Test
    fun showsDataOnSuccessfulLoad() {
        songList(this) {
            checkFirstSongIs(TITLE_FIRST_ITEM)
        }
    }

    @Test
    fun clickingFirstItemShowsViewerScreen() {
        songList(this) {
            clickSongWithTitle(TITLE_FIRST_ITEM)
        }

        viewer {
            checkPageVisible(1)
        }
    }

    companion object {
        const val LABEL_EMPTY_STATE = "songs"
        const val TITLE_FIRST_ITEM = "A Lobortis"
    }
}
