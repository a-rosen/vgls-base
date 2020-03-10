package com.vgleadsheets.features.main.list

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.vgleadsheets.components.EmptyStateListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.TitleListModel
import com.vgleadsheets.features.main.hud.parts.PartSelectorItem
import com.vgleadsheets.mvrx.MvRxViewModel
import com.vgleadsheets.resources.ResourceProvider
import timber.log.Timber

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
abstract class ListViewModel<DataType, StateType : ListState<DataType>> constructor(
    initialState: StateType,
    private val resourceProvider: ResourceProvider
) : MvRxViewModel<StateType>(initialState) {
    fun onSelectedPartUpdate(part: PartSelectorItem?) {
        setState {
            updateListState(
                selectedPart = part,
                listModels = constructList(
                    data,
                    updateTime,
                    digest,
                    selectedPart
                )
            ) as StateType
        }
    }

    fun onDigestUpdate(digest: Async<List<*>>) {
        setState {
            updateListState(
                digest = digest,
                listModels = constructList(
                    data,
                    updateTime,
                    digest,
                    selectedPart
                )
            ) as StateType
        }
    }

    fun onTimeUpdate(time: Async<*>) {
        setState {
            updateListState(
                updateTime = time,
                listModels = constructList(
                    data,
                    updateTime,
                    digest,
                    selectedPart
                )
            ) as StateType
        }
    }

    fun constructList(
        data: Async<List<DataType>>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ): List<ListModel> {
        Timber.v("Constructing list...")

        return listOf(createTitleListModel()) +
                createContentListModels(
                    data,
                    updateTime,
                    digest,
                    selectedPart
                )
    }

    abstract fun createTitleListModel(): TitleListModel

    abstract fun createFullEmptyStateListModel(): EmptyStateListModel

    abstract fun createSuccessListModels(
        data: List<DataType>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem
    ): List<ListModel>

    private fun createContentListModels(
        data: Async<List<DataType>>,
        updateTime: Async<*>,
        digest: Async<*>,
        selectedPart: PartSelectorItem?
    ) = when (data) {
        is Loading, Uninitialized -> createLoadingListModels()
        is Fail -> createErrorStateListModel(data.error)
        is Success ->
            successListModelHelper(selectedPart, data, digest, updateTime)
    }

    private fun successListModelHelper(
        selectedPart: PartSelectorItem?,
        data: Success<List<DataType>>,
        digest: Async<*>,
        updateTime: Async<*>
    ): List<ListModel> {
        return if (selectedPart == null) {
            createErrorStateListModel(
                IllegalArgumentException("No part selected.")
            )
        } else if (data().isEmpty()) {
            if (digest is Loading || updateTime is Loading) {
                createLoadingListModels()
            } else {
                listOf(
                    createFullEmptyStateListModel()
                )
            }
        } else {
            createSuccessListModels(
                data(),
                updateTime,
                digest,
                selectedPart
            )
        }
    }

    private fun createLoadingListModels(): List<ListModel> {
        val listModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            listModels.add(
                LoadingNameCaptionListModel(index)
            )
        }

        return listModels
    }

    private fun createErrorStateListModel(error: Throwable) =
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    companion object {
        const val LOADING_ITEMS = 15

        const val MAX_LENGTH_SUBTITLE_CHARS = 20
        const val MAX_LENGTH_SUBTITLE_ITEMS = 6
    }
}