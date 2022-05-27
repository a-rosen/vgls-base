package com.vgleadsheets.features.main.game.better

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.FragmentRouter
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository
import com.vgleadsheets.tracking.TrackingScreen

class BetterGameViewModel @AssistedInject constructor(
    @Assisted initialState: BetterGameState,
    @Assisted private val router: FragmentRouter,
    private val repository: Repository,
) : MvRxViewModel<BetterGameState>(initialState) {
    init {
        fetchGame()
        fetchSongs()
    }

    private fun fetchGame() = withState {
        repository.getGame(it.gameId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        game = it
                    )
                )
            }
    }

    private fun fetchSongs() = withState {
        repository.getSongsForGame(it.gameId)
            .execute {
                copy(
                    contentLoad = contentLoad.copy(
                        songs = it
                    )
                )
            }
    }

    fun onSongClicked(
        id: Long,
        songName: String,
        gameName: String,
        transposition: String
    ) {
        router.showSongViewer(
            id,
            songName,
            gameName,
            transposition,
            TrackingScreen.DETAIL_GAME,
        )
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(
            initialState: BetterGameState,
            router: FragmentRouter
        ): BetterGameViewModel
    }

    companion object : MvRxViewModelFactory<BetterGameViewModel, BetterGameState> {
        override fun create(
            viewModelContext: ViewModelContext,
            state: BetterGameState
        ): BetterGameViewModel {
            val fragment: BetterGameFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state, fragment.activity as FragmentRouter)
        }
    }
}
