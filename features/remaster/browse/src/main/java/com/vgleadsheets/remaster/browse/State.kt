package com.vgleadsheets.remaster.browse

import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.MenuItemListModel
import com.vgleadsheets.list.ListState
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.ui.Icon
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data object State : ListState() {
    override fun title() = "Browse"

    override fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel> = persistentListOf(
        MenuItemListModel(
            name = "Favorites",
            caption = null,
            icon = Icon.JAM_FILLED,
            clickAction = Action.DestinationClicked(Destination.FAVORITES.destName),
            selected = false
        ),
        MenuItemListModel(
            name = "By Game",
            caption = null,
            icon = Icon.ALBUM,
            clickAction = Action.DestinationClicked(Destination.GAMES_LIST.destName),
            selected = false
        ),
        MenuItemListModel(
            name = "By Composer",
            caption = null,
            icon = Icon.PERSON,
            clickAction = Action.DestinationClicked(Destination.COMPOSERS_LIST.destName),
            selected = false
        ),
        MenuItemListModel(
            name = "By Tag",
            caption = null,
            icon = Icon.TAG,
            clickAction = Action.DestinationClicked(Destination.TAGS_LIST.destName),
            selected = false
        ),
        MenuItemListModel(
            name = "All Sheets",
            caption = null,
            icon = Icon.DESCRIPTION,
            clickAction = Action.DestinationClicked(Destination.SONGS_LIST.destName),
            selected = false
        ),
    )
}
