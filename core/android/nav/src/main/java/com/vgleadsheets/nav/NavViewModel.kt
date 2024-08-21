package com.vgleadsheets.nav

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.vgleadsheets.appcomm.EventDispatcher
import com.vgleadsheets.appcomm.Hacks
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.appcomm.VglsEvent
import com.vgleadsheets.appinfo.AppInfo
import com.vgleadsheets.coroutines.VglsDispatchers
import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.notif.NotifManager
import com.vgleadsheets.repository.UpdateManager
import com.vgleadsheets.ui.StringId
import com.vgleadsheets.viewmodel.VglsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@HiltViewModel
class NavViewModel @Inject constructor(
    override val dispatchers: VglsDispatchers,
    override val hatchet: Hatchet,
    override val eventDispatcher: EventDispatcher,
    private val notifManager: NotifManager,
    private val updateManager: UpdateManager,
    private val appInfo: AppInfo,
) : VglsViewModel<NavState>() {
    init {
        eventDispatcher.addEventSink(this)
        sendAction(VglsAction.InitNoArgs)
    }

    lateinit var navController: NavController
    lateinit var snackbarScope: CoroutineScope
    lateinit var snackbarHostState: SnackbarHostState

    private val activityEventChannel = Channel<ActivityEvent>()
    val activityEvents: ReceiveChannel<ActivityEvent> = activityEventChannel

    override fun initialState() = NavState()

    override fun handleAction(action: VglsAction) {
        viewModelScope.launch(dispatchers.main) {
            hatchet.d("${this.javaClass.simpleName} - Handling action: $action")
        }
    }

    override fun handleEvent(event: VglsEvent) {
        viewModelScope.launch(dispatchers.main) {
            hatchet.d("${this@NavViewModel.javaClass.simpleName} - Handling event: $event")
            when (event) {
                is VglsEvent.NavigateTo -> navigateTo(event.destination)
                is VglsEvent.NavigateBack -> navigateBack()
                is VglsEvent.ShowSnackbar -> showSnackbar(event)
                is VglsEvent.HideUiChrome -> hideSystemUi()
                is VglsEvent.ShowUiChrome -> showSystemUi()
                is VglsEvent.ClearNotif -> clearNotif(event.id)
                is VglsEvent.RefreshDb -> refreshDb()
                is VglsEvent.GiantBombLinkClicked -> launchWebsite(URL_GB_WEBSITE)
                is VglsEvent.SearchYoutubeClicked -> launchWebsite(getYoutubeSearchUrlForQuery(event.query))
                is VglsEvent.WebsiteLinkClicked -> launchWebsite(URL_VGLS_WEBSITE)
                is VglsEvent.RestartApp -> restartApp()
            }
        }
    }

    fun startWatchingBackstack() {
        navController
            .currentBackStackEntryFlow
            .onEach { printBackstackStatus() }
            .launchIn(viewModelScope)
    }

    private fun restartApp() {
        viewModelScope.launch {
            activityEventChannel.send(ActivityEvent.Restart)
        }
    }

    private fun clearNotif(notifId: Long) {
        notifManager.removeNotif(notifId)
    }

    private fun refreshDb() {
        updateManager.refresh()
        notifManager.removeNotif(id = StringId.ERROR_DB_UPDATE.hashCode().toLong())
        notifManager.removeNotif(id = StringId.ERROR_API_UPDATE.hashCode().toLong())
    }

    private fun launchWebsite(url: String) {
        val launcher = Intent(Intent.ACTION_VIEW)
        launcher.data = Uri.parse(url)

        val event = ActivityEvent.LaunchIntent(launcher)
        viewModelScope.launch {
            activityEventChannel.send(event)
        }
    }

    private fun showSnackbar(snackbarEvent: VglsEvent.ShowSnackbar) {
        snackbarScope.launch {
            val actionDetails = snackbarEvent.actionDetails
            val result = snackbarHostState.showSnackbar(
                message = snackbarEvent.message,
                actionLabel = actionDetails?.clickActionLabel,
                withDismissAction = snackbarEvent.withDismissAction,
                duration = SnackbarDuration.Short
            )

            if (actionDetails == null) {
                return@launch
            }

            val sink = actionDetails.actionSink

            when (result) {
                SnackbarResult.ActionPerformed -> {
                    sink.sendAction(VglsAction.SnackbarActionClicked(actionDetails.clickAction))
                }

                SnackbarResult.Dismissed -> {
                    sink.sendAction(VglsAction.SnackbarDismissed(actionDetails.clickAction))
                }
            }
        }
    }

    @Suppress("SwallowedException")
    private fun navigateTo(destination: String) {
        try {
            val message = "Navigating to $destination"

            hatchet.v(message)
            navController.navigate(destination)

            if (appInfo.isDebug) {
                showSnackbar(
                    VglsEvent.ShowSnackbar(message, false, source = "Navigation")
                )
            }
        } catch (ex: IllegalArgumentException) {
            sendEvent(
                VglsEvent.ShowSnackbar(
                    message = "Unimplemented screen: $destination",
                    withDismissAction = true,
                    source = "Navigation"
                )
            )
        }
    }

    private fun navigateBack() {
        val oldRoute = navController.currentDestination?.route
        val success = navController.popBackStack()

        if (!success) {
            viewModelScope.launch { activityEventChannel.send(ActivityEvent.Finish) }
            return
        }

        val newRoute = navController.currentDestination?.route
        val message = "Popping stack from $oldRoute to $newRoute"
        if (appInfo.isDebug) {
            showSnackbar(
                VglsEvent.ShowSnackbar(message, false, source = "Navigation")
            )
        }
    }

    @SuppressLint("RestrictedApi")
    private fun printBackstackStatus() {
        if (appInfo.isDebug) {
            hatchet.d("Nav backstack updated.")
            navController.currentBackStack.value.forEach { entry ->
                if (entry.destination.route == null) {
                    return@forEach
                }
                hatchet.v("Dest: ${entry.destination.route} State: ${entry.maxLifecycle} Args: ${entry.arguments}")
            }
        }
    }

    private fun showSystemUi() {
        hatchet.d("Showing system UI.")
        updateState {
            it.copy(visibility = SystemUiVisibility.VISIBLE)
        }
        emitEvent(VglsEvent.UiChromeBecameShown)
    }

    private fun hideSystemUi() {
        hatchet.d("Hiding system UI.")
        viewModelScope.launch {
            delay(Hacks.UI_VISIBILITY_ANIM_DELAY)
            updateState {
                it.copy(visibility = SystemUiVisibility.HIDDEN)
            }
            emitEvent(VglsEvent.UiChromeBecameHidden)
        }
    }

    companion object {
        private const val URL_VGLS_WEBSITE = "https://www.vgleadsheets.com/"
        private const val URL_GB_WEBSITE = "https://www.giantbomb.com/"
    }
}
