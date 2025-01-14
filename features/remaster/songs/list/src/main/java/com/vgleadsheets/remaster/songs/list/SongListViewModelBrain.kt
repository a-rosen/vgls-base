package com.vgleadsheets.remaster.songs.list

import com.vgleadsheets.appcomm.LCE
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Song
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.SongRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class SongListViewModelBrain(
    private val songRepository: SongRepository,
    private val scheduler: VglsScheduler,
    stringProvider: StringProvider,
    hatchet: Hatchet,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> startLoading()
            is Action.SongClicked -> onSongClicked(action.id)
        }
    }

    private fun startLoading() {
        showLoading()
        collectSongs()
    }

    private fun collectSongs() {
        songRepository.getAllSongs()
            .onEach(::onSongsLoaded)
            .catch { error -> showError(error) }
            .runInBackground()
    }

    private fun showLoading() {
        updateSongs(LCE.Loading(LOAD_OPERATION_NAME))
    }

    private fun showError(error: Throwable) {
        updateSongs(LCE.Error(LOAD_OPERATION_NAME, error))
    }

    private fun onSongsLoaded(songs: List<Song>) {
        updateSongs(LCE.Content(songs))
    }

    private fun updateSongs(songs: LCE<List<Song>>) {
        updateState {
            (it as State).copy(
                songs = songs
            )
        }
    }

    private fun onSongClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.SONG_DETAIL.forId(id),
                Destination.SONGS_LIST.name
            )
        )
    }

    companion object {
        private const val LOAD_OPERATION_NAME = "songs.list"
    }
}
