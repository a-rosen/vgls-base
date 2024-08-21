package com.vgleadsheets.remaster.composers.list

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.model.Composer
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.repository.ComposerRepository
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.onEach

class ComposerListViewModelBrain(
    private val composerRepository: ComposerRepository,
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
            is VglsAction.InitNoArgs -> collectComposers()
            is Action.ComposerClicked -> onComposerClicked(action.id)
        }
    }

    private fun collectComposers() {
        composerRepository.getAllComposers()
            .onEach(::onComposersLoaded)
            .runInBackground()
    }

    private fun onComposersLoaded(composers: List<Composer>) {
        updateState {
            (it as State).copy(
                composers = composers
            )
        }
    }

    private fun onComposerClicked(id: Long) {
        emitEvent(
            VglsEvent.NavigateTo(
                Destination.COMPOSER_DETAIL.forId(id),
                Destination.COMPOSERS_LIST.name
            )
        )
    }
}
