package com.vgleadsheets.list

import com.vgleadsheets.appcomm.VglsState
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingItemListModel
import com.vgleadsheets.components.LoadingType
import com.vgleadsheets.components.TitleBarModel
import com.vgleadsheets.ui.StringProvider
import kotlinx.collections.immutable.ImmutableList

abstract class ListState : VglsState {
    abstract fun title(stringProvider: StringProvider): TitleBarModel
    abstract fun toListItems(stringProvider: StringProvider): ImmutableList<ListModel>

    fun toActual(stringProvider: StringProvider): ListStateActual {
        return ListStateActual(
            title = title(stringProvider),
            listItems = toListItems(stringProvider),
        )
    }

    protected fun loading(
        operationName: String,
        loadingType: LoadingType,
        itemCount: Int,
        withHeader: Boolean = false,
    ) = if (withHeader) {
        listOf(
            LoadingItemListModel(
                loadingType = LoadingType.SECTION_HEADER,
                loadOperationName = "$operationName.section.header",
                loadPositionOffset = 0
            )
        )
    } else {
        emptyList()
    } + List(itemCount) { index ->
        LoadingItemListModel(
            loadingType = loadingType,
            loadOperationName = operationName,
            loadPositionOffset = index
        )
    }

    protected fun error(operationName: String, error: Throwable) = listOf(
        ErrorStateListModel(
            failedOperationName = operationName,
            errorString = error.message ?: "Unknown error."
        )
    )
}
