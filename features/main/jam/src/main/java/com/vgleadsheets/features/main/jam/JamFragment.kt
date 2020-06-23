package com.vgleadsheets.features.main.jam

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.args.IdArgs
import com.vgleadsheets.components.CtaListModel
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ImageNameCaptionListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.NetworkRefreshingListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.features.main.hud.parts.getPartMatchingSelection
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.Jam
import com.vgleadsheets.model.jam.SetlistEntry
import com.vgleadsheets.model.jam.SongHistoryEntry
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_jam.list_jam_options
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@Suppress("TooManyFunctions")
class JamFragment : VglsFragment(),
    ImageNameCaptionListModel.EventHandler,
    CtaListModel.EventHandler {
    @Inject
    lateinit var jamViewModelFactory: JamViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: JamViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    private var refreshLauncher: Disposable? = null

    override fun onClicked(clicked: ImageNameCaptionListModel) {
        val songId = clicked.actionableId

        if (songId != null) {
            getFragmentRouter().showSongViewer(songId)
        } else {
            showError("Unable to show sheet.")
        }
    }

    override fun clearClicked() {
        TODO("Not yet implemented")
    }

    override fun onClicked(clicked: CtaListModel) {
        when (clicked.iconId) {
            R.drawable.ic_playlist_play_black_24dp -> followJam()
            R.drawable.ic_refresh_24dp -> refreshJam()
            R.drawable.ic_delete_black_24dp -> deleteJam()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_jam_options.adapter = adapter
        list_jam_options.layoutManager = LinearLayoutManager(context)
        list_jam_options.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )

        refreshLauncher = viewModel.asyncSubscribe(
            JamState::jam,
            deliveryMode = uniqueOnly("jam")
        ) {
            viewModel.refreshJam(it.id, it.name)
            refreshLauncher?.dispose()
        }

        viewModel.asyncSubscribe(
            JamState::deletion,
            deliveryMode = uniqueOnly("deletion")
        ) {
            activity?.onBackPressed()
        }
    }

    override fun invalidate() = withState(viewModel, hudViewModel) { jamState, hudState ->
        hudViewModel.dontAlwaysShowBack()

        val selectedPart = hudState.parts.first { it.selected }

        val jam = jamState.jam
        val jamRefresh = jamState.jamRefresh
        val setlist = jamState.setlist
        val setlistRefresh = jamState.setlistRefresh

        val listModels = constructList(jam, jamRefresh, setlist, setlistRefresh, selectedPart)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_jam

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(
        jam: Async<Jam>,
        jamRefresh: Async<ApiJam>,
        setlist: Async<List<SetlistEntry>>,
        setlistRefresh: Async<List<Long>>,
        selectedPart: PartSelectorItem
    ) = createTitleListModel(jam) + createContentListModels(
        jam,
        jamRefresh,
        setlist,
        setlistRefresh,
        selectedPart
    )

    private fun createTitleListModel(jam: Async<Jam>) = when (jam) {
        // TODO Loading state for title items
        is Loading, Uninitialized -> createLoadingListModels("Title")
        is Fail -> createErrorStateListModel(jam.error)
        is Success -> listOf(
            TitleListModel(
                R.string.subtitle_jam.toLong(),
                jam().name.toTitleCase(),
                getString(R.string.subtitle_jam)
            )
        )
    }

    private fun createContentListModels(
        jam: Async<Jam>,
        jamRefresh: Async<ApiJam>,
        setlist: Async<List<SetlistEntry>>,
        setlistRefresh: Async<List<Long>>,
        selectedPart: PartSelectorItem
    ) = createCtaListModels(jam) +
            createJamListModels(jam, jamRefresh, selectedPart) +
            createSetlistListModels(setlist, setlistRefresh, selectedPart) +
            createSongHistoryListModels(jam, selectedPart)

    private fun createJamListModels(
        jam: Async<Jam>,
        jamRefresh: Async<ApiJam>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val refreshingListModels = if (jamRefresh is Loading) {
            listOf(NetworkRefreshingListModel("jam"))
        } else emptyList()

        val jamListModels = when (jam) {
            is Loading, Uninitialized -> createLoadingListModels("JamList")
            is Fail -> createErrorStateListModel(jam.error)
            is Success -> createSuccessListModels(jam(), selectedPart)
        }

        return refreshingListModels + jamListModels
    }

    private fun createSuccessListModels(
        jam: Jam,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val thumbUrl = jam.currentSong
            .parts
            ?.getPartMatchingSelection(selectedPart)
            ?.pages
            ?.first()
            ?.imageUrl

        return listOf(
            SectionHeaderListModel(
                getString(R.string.jam_current_song)
            ),
            ImageNameCaptionListModel(
                Long.MAX_VALUE,
                jam.currentSong.name,
                jam.currentSong.gameName,
                thumbUrl,
                R.drawable.placeholder_sheet,
                this,
                jam.currentSong.id
            )
        )
    }

    private fun createSetlistListModels(
        setlist: Async<List<SetlistEntry>>,
        setlistRefresh: Async<List<Long>>,
        selectedPart: PartSelectorItem
    ): List<ListModel> {
        val refreshingListModels = if (setlistRefresh is Loading) {
            listOf(NetworkRefreshingListModel("setlist"))
        } else emptyList()

        if (setlistRefresh is Fail) {
            showError(setlistRefresh.error)
        }

        val jamListModels = when (setlist) {
            is Loading, Uninitialized -> createLoadingListModels("SetList")
            is Fail -> createErrorStateListModel(setlist.error)
            is Success -> createSuccessListModels(setlist(), selectedPart)
        }

        return refreshingListModels + jamListModels
    }

    private fun createSuccessListModels(
        setlist: List<SetlistEntry>,
        selectedPart: PartSelectorItem
    ) = listOf(
        SectionHeaderListModel(
            getString(R.string.jam_setlist)
        )
    ) + if (setlist.isEmpty()) {
        listOf(
            EmptyStateListModel(
                R.drawable.ic_list_black_24dp,
                getString(R.string.empty_setlist)
            )
        )
    } else {
        setlist.map { entry ->
            val thumbUrl = entry.song
                ?.parts
                ?.getPartMatchingSelection(selectedPart)
                ?.pages
                ?.first()
                ?.imageUrl

            ImageNameCaptionListModel(
                entry.id,
                entry.songName,
                entry.gameName,
                thumbUrl,
                R.drawable.placeholder_sheet,
                this,
                entry.song?.id
            )
        }
    }

    private fun createSongHistoryListModels(
        jam: Async<Jam>,
        selectedPart: PartSelectorItem
    ) = when (jam) {
        is Loading, Uninitialized -> createLoadingListModels("SongHistory")
        is Fail -> createErrorStateListModel(jam.error)
        is Success -> createSuccessListModelsForSongHistory(jam().songHistory!!, selectedPart)
    }

    private fun createSuccessListModelsForSongHistory(
        songHistory: List<SongHistoryEntry>,
        selectedPart: PartSelectorItem
    ) = if (songHistory.isEmpty()) {
        emptyList()
    } else {
        listOf(
            SectionHeaderListModel(
                getString(R.string.jam_song_history)
            )
        ) + songHistory.map { entry ->
            val thumbUrl = entry.song
                .parts
                ?.getPartMatchingSelection(selectedPart)
                ?.pages
                ?.first()
                ?.imageUrl

            ImageNameCaptionListModel(
                entry.id,
                entry.song.name,
                entry.song.gameName,
                thumbUrl,
                R.drawable.placeholder_sheet,
                this,
                entry.song.id
            )
        }
    }

    private fun createLoadingListModels(sectionId: String): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            Timber.i("Creating loading item $index")
            listModels.add(
                LoadingNameCaptionListModel(sectionId, index)
            )
        }

        return listModels
    }

    private fun createCtaListModels(jam: Async<Jam>) = when (jam) {
        // TODO Loading state for title items
        is Loading, Uninitialized -> createLoadingListModels("Cta")
        is Fail, is Success -> listOf(
            CtaListModel(
                R.drawable.ic_playlist_play_black_24dp,
                getString(R.string.cta_follow_jam),
                this
            ),
            CtaListModel(
                R.drawable.ic_refresh_24dp,
                getString(R.string.cta_refresh_jam),
                this
            ),
            CtaListModel(
                R.drawable.ic_delete_black_24dp,
                getString(R.string.cta_delete_jam),
                this
            )
        )
    }

    private fun createErrorStateListModel(error: Throwable) =
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun followJam() = withState(viewModel) {
        getFragmentRouter().showJamViewer(it.jamId)
    }

    private fun refreshJam() = withState(viewModel) {
        if (it.jam is Success) {
            viewModel.refreshJam(it.jamId, it.jam()?.name!!)
        } else {
            showError("Can't refresh Jam.")
        }
    }

    private fun deleteJam() {
        viewModel.deleteJam()
    }

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("DefaultLocale")
    private fun String.toTitleCase() = this
        .replace("_", " ")
        .split(" ")
        .map {
            if (it != "the") {
                it.capitalize(Locale.getDefault())
            } else {
                it
            }
        }
        .joinToString(" ")

    companion object {
        const val LOADING_ITEMS = 2

        fun newInstance(idArgs: IdArgs): JamFragment {
            val fragment = JamFragment()

            val args = Bundle()
            args.putParcelable(MvRx.KEY_ARG, idArgs)
            fragment.arguments = args

            return fragment
        }
    }
}
