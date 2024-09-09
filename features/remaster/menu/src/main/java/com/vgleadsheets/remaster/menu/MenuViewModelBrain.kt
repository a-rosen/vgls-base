package com.vgleadsheets.remaster.menu

import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.list.ListViewModelBrain
import com.vgleadsheets.list.VglsScheduler
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.settings.DebugSettingsManager
import com.vgleadsheets.settings.GeneralSettingsManager
import com.vgleadsheets.ui.StringProvider
import kotlinx.coroutines.flow.onEach

class MenuViewModelBrain(
    private val generalSettingsManager: GeneralSettingsManager,
    private val debugSettingsManager: DebugSettingsManager,
    private val appInfo: AppInfo,
    stringProvider: StringProvider,
    hatchet: Hatchet,
    private val scheduler: VglsScheduler,
) : ListViewModelBrain(
    stringProvider,
    hatchet,
    scheduler,
) {
    override fun initialState() = State()

    override fun handleAction(action: VglsAction) {
        when (action) {
            is VglsAction.InitNoArgs -> fetchSettings()
            is VglsAction.Resume -> return
            is VglsAction.Noop -> return
            is Action.KeepScreenOnClicked -> onKeepScreenOnClicked()
            is Action.WebsiteLinkClicked -> onWebsiteLinkClicked()
            is Action.GiantBombClicked -> onGiantBombClicked()
            is Action.LicensesLinkClicked -> onLicensesLinkClicked()
            is Action.DebugDelayClicked -> onDebugDelayClicked()
            is Action.DebugShowNavSnackbarsClicked -> onDebugShowNavSnackbarsClicked()
            is Action.RestartAppClicked -> onRestartAppClicked()
            else -> throw IllegalArgumentException("Invalid action for this screen.")
        }
    }

    private fun onKeepScreenOnClicked() {
        val oldValue = (internalUiState.value as State).keepScreenOn ?: return
        generalSettingsManager.setKeepScreenOn(!oldValue)
    }

    private fun onLicensesLinkClicked() {
        navigateTo(Destination.LICENSES.noArgs())
    }

    private fun onWebsiteLinkClicked() {
        emitEvent(VglsEvent.WebsiteLinkClicked)
    }

    private fun onGiantBombClicked() {
        emitEvent(VglsEvent.GiantBombLinkClicked)
    }

    private fun onDebugDelayClicked() {
        val oldValue = (internalUiState.value as State).debugShouldDelay ?: return
        updateState { (it as State).copy(debugShouldDelay = null) }
        debugSettingsManager.setShouldDelay(!oldValue)
    }

    private fun onDebugShowNavSnackbarsClicked() {
        val oldValue = (internalUiState.value as State).debugShouldShowNavSnackbars ?: return
        updateState { (it as State).copy(debugShouldShowNavSnackbars = null) }
        debugSettingsManager.setShouldShowSnackbars(!oldValue)
    }

    private fun fetchSettings() {
        fetchKeepScreenOn()
        fetchAppInfo()
        fetchDebugShouldDelay()
        fetchDebugShouldShowNavSnackbars()
    }

    private fun fetchAppInfo() {
        updateState { (it as State).copy(appInfo = appInfo) }
    }

    private fun fetchKeepScreenOn() {
        generalSettingsManager
            .getKeepScreenOn()
            .onEach { value ->
                updateState { (it as State).copy(keepScreenOn = value) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldDelay() {
        updateState { (it as State).copy(debugShouldDelay = null) }
        debugSettingsManager
            .getShouldDelay()
            .onEach { value ->
                updateState { (it as State).copy(debugShouldDelay = value ?: false) }
            }
            .runInBackground()
    }

    private fun fetchDebugShouldShowNavSnackbars() {
        updateState { (it as State).copy(debugShouldShowNavSnackbars = null) }
        debugSettingsManager
            .getShouldShowSnackbars()
            .onEach { value ->
                updateState { (it as State).copy(debugShouldShowNavSnackbars = value ?: false) }
            }
            .runInBackground()
    }

    private fun onRestartAppClicked() {
        emitEvent(
            VglsEvent.RestartApp
        )
    }

    private fun navigateTo(destinationString: String) {
        emitEvent(
            VglsEvent.NavigateTo(
                destinationString,
                Destination.MENU.destName
            )
        )
    }
}
