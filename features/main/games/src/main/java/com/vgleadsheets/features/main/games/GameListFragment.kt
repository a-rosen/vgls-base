package com.vgleadsheets.features.main.games

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.game.Game
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Empty
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Network
import com.vgleadsheets.repository.Storage
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_game.*
import javax.inject.Inject

class GameListFragment : VglsFragment() {
    @Inject
    lateinit var gameListViewModelFactory: GameListViewModel.Factory

    private val hudViewModel: HudViewModel by activityViewModel()

    private val viewModel: GameListViewModel by fragmentViewModel()

    private val adapter = GameListAdapter(this)

    fun onItemClick(clickedGameId: Long) {
        showSongList(clickedGameId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()

        list_games.adapter = adapter
        list_games.layoutManager = LinearLayoutManager(context)
        list_games.setInsetListenerForPadding(topOffset = topOffset)
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, gameListState ->
        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        when (val data = gameListState.data) {
            is Fail -> showError(
                data.error.message ?: data.error::class.simpleName ?: "Unknown Error"
            )
            is Success -> showData(gameListState.data(), selectedPart)
        }
    }

    override fun getLayoutId() = R.layout.fragment_game

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun showData(data: Data<List<Game>>?, selectedPart: PartSelectorItem) {
        when (data) {
            is Empty -> showLoading()
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Network -> hideLoading()
            is Storage -> showGames(data(), selectedPart)
        }
    }

    private fun showSongList(clickedGameId: Long) {
        getFragmentRouter().showSongListForGame(clickedGameId)
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        list_games.fadeOutPartially()
    }

    private fun hideLoading() {
        list_games.fadeIn()
        progress_loading.fadeOutGone()
    }

    private fun showGames(games: List<Game>, selectedPart: PartSelectorItem) {
        val availableGames = games.map { game ->
            val availableSongs = game.songs?.filter { song ->
                song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
            }

            game.copy(songs = availableSongs)
        }.filter {
            it.songs?.isNotEmpty() ?: false
        }

        adapter.dataset = availableGames
    }

    companion object {
        fun newInstance() = GameListFragment()
    }
}
