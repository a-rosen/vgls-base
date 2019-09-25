package com.vgleadsheets.features.main.songs

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.animation.fadeIn
import com.vgleadsheets.animation.fadeInFromZero
import com.vgleadsheets.animation.fadeOutGone
import com.vgleadsheets.animation.fadeOutPartially
import com.vgleadsheets.args.SongListArgs
import com.vgleadsheets.components.NameCaptionListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.model.song.Song
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.repository.Data
import com.vgleadsheets.repository.Empty
import com.vgleadsheets.repository.Error
import com.vgleadsheets.repository.Network
import com.vgleadsheets.repository.Storage
import com.vgleadsheets.setInsetListenerForPadding
import kotlinx.android.synthetic.main.fragment_sheet.*
import javax.inject.Inject

class SongListFragment : VglsFragment(), NameCaptionListModel.ClickListener {
    @Inject
    lateinit var sheetListViewModelFactory: SongListViewModel.Factory

    private val hudViewModel: HudViewModel by activityViewModel()

    private val viewModel: SongListViewModel by fragmentViewModel()

    private val args: SongListArgs by args()

    private val adapter = ComponentAdapter()

    override fun onClicked(clicked: NameCaptionListModel) {
        showSongViewer(clicked.dataId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_sheets.adapter = adapter
        list_sheets.layoutManager = LinearLayoutManager(context)
        list_sheets.setInsetListenerForPadding(topOffset = topOffset, bottomOffset = bottomOffset)
    }

    override fun invalidate() = withState(hudViewModel, viewModel) { hudState, songListState ->
        val selectedPart = hudState.parts?.first { it.selected }

        if (selectedPart == null) {
            showError("No part selected.")
            return@withState
        }

        when (val data = songListState.data) {
            is Fail -> showError(
                data.error.message ?: data.error::class.simpleName ?: "Unknown Error"
            )
            is Loading -> showLoading()
            is Success -> showData(songListState.data(), selectedPart)
        }
    }

    override fun getLayoutId() = R.layout.fragment_sheet

    override fun getVglsFragmentTag() = this.javaClass.simpleName + ":${args.id}"

    private fun showData(data: Data<List<Song>>?, selectedPart: PartSelectorItem) {
        when (data) {
            is Empty -> showLoading()
            is Error -> showError(data.error.message ?: "Unknown error.")
            is Network -> hideLoading()
            is Storage -> showSongs(data(), selectedPart)
        }
    }

    private fun showSongs(songs: List<Song>, selectedPart: PartSelectorItem) {
        hideLoading()
        val availableSongs = songs.filter { song ->
            song.parts?.firstOrNull { part -> part.name == selectedPart.apiId } != null
        }

        val listComponents = availableSongs.map {
            val caption = when (it.composers?.size) {
                null, 0 -> "Unknown Composer"
                1 -> it.composers?.get(0)?.name ?: "Unknown Composer"
                else -> "Various Composers"
            }

            NameCaptionListModel(
                it.id,
                it.name,
                caption,
                this
            )
        }

        adapter.submitList(listComponents)
    }

    private fun showSongViewer(clickedSongId: Long) {
        getFragmentRouter().showSongViewer(clickedSongId)
    }

    private fun showLoading() {
        progress_loading.fadeInFromZero()
        list_sheets.fadeOutPartially()
    }

    private fun hideLoading() {
        list_sheets.fadeIn()
        progress_loading.fadeOutGone()
    }

    companion object {
        fun newInstance(args: SongListArgs): SongListFragment {
            val fragment = SongListFragment()

            val argBundle = Bundle()
            argBundle.putParcelable(MvRx.KEY_ARG, args)
            fragment.arguments = argBundle

            return fragment
        }
    }
}
