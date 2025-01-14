package com.vgleadsheets.appcomm

open class VglsEvent {
    open val source: String = "Unknown"

    data class NavigateBack(override val source: String) : VglsEvent()
    data class NavigateTo(val destination: String, override val source: String) : VglsEvent()
    data class NavigateSingleTopLevel(val destination: String, override val source: String) : VglsEvent()

    data class ShowSnackbar(
        val message: String,
        val withDismissAction: Boolean,
        val actionDetails: SnackbarActionDetails? = null,
        override val source: String,
    ) : VglsEvent() {
        data class SnackbarActionDetails(
            val actionSink: ActionSink,
            val clickAction: VglsAction,
            val clickActionLabel: String,
        )
    }

    data class UpdateTitle(
        val title: String? = null,
        val subtitle: String? = null,
        val shouldShowBack: Boolean = true,
        override val source: String,
    ) : VglsEvent()

    data object HideTopBar : VglsEvent()

    data object ShowUiChrome : VglsEvent()
    data object HideUiChrome : VglsEvent()

    data object SystemBarsBecameShown : VglsEvent()
    data object SystemBarsBecameHidden : VglsEvent()
    data object TopBarBecameShown : VglsEvent()
    data object TopBarBecameHidden : VglsEvent()
    data object NavBarBecameShown : VglsEvent()
    data object NavBarBecameHidden : VglsEvent()

    data class ClearNotif(val id: Long) : VglsEvent()

    data object RefreshDb : VglsEvent()

    data class SearchYoutubeClicked(val query: String) : VglsEvent()

    data object GiantBombLinkClicked : VglsEvent()
    data object WebsiteLinkClicked : VglsEvent()

    data object RestartApp : VglsEvent()
}
