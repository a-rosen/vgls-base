package com.vgleadsheets.features.main.composers

import com.airbnb.mvrx.UniqueOnly
import com.airbnb.mvrx.fragmentViewModel
import com.vgleadsheets.features.main.list.ListFragment
import com.vgleadsheets.model.composer.Composer
import javax.inject.Inject

class ComposerListFragment : ListFragment<Composer, ComposerListState>() {
    @Inject
    lateinit var composerListViewModelFactory: ComposerListViewModel.Factory

    override val viewModel: ComposerListViewModel by fragmentViewModel()

    private var apiAvailableErrorShown = false

    override fun subscribeToViewEvents() {
        viewModel.selectSubscribe(ComposerListState::clickedGbListModel) {
            val clickedComposerId = it?.dataId

            if (clickedComposerId != null) {
                showSongList(clickedComposerId)
                viewModel.clearClicked()
            }
        }

        viewModel.selectSubscribe(ComposerListState::gbApiNotAvailable, deliveryMode = UniqueOnly("api")) {
            if (it == true) {
                if (!apiAvailableErrorShown) {
                    apiAvailableErrorShown = true
                    showError(getString(R.string.error_no_gb_api))
                }
            }
        }
    }

    private fun showSongList(clickedComposerId: Long) {
        getFragmentRouter().showSongListForComposer(clickedComposerId)
    }

    companion object {
        fun newInstance() = ComposerListFragment()
    }
}
