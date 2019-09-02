package com.vgleadsheets.songs

import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.repository.Repository

class SongListViewModel @AssistedInject constructor(
    @Assisted initialState: SongListState,
    private val repository: Repository
) : MvRxViewModel<SongListState>(initialState) {
    init {
        fetchSongs()
    }

    private fun fetchSongs() = withState { state ->
        repository.getSongs(state.gameId)
            .execute { data ->
                copy(data = data)
            }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SongListState): SongListViewModel
    }

    companion object : MvRxViewModelFactory<SongListViewModel, SongListState> {
        override fun create(viewModelContext: ViewModelContext, state: SongListState): SongListViewModel? {
            val fragment: SongListFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.sheetListViewModelFactory.create(state)
        }
    }
}
