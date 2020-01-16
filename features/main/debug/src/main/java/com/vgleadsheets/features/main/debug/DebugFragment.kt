package com.vgleadsheets.features.main.debug

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Fail
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.Uninitialized
import com.airbnb.mvrx.existingViewModel
import com.airbnb.mvrx.fragmentViewModel
import com.airbnb.mvrx.withState
import com.vgleadsheets.VglsFragment
import com.vgleadsheets.components.CheckableListModel
import com.vgleadsheets.components.DropdownSettingListModel
import com.vgleadsheets.components.ErrorStateListModel
import com.vgleadsheets.components.ListModel
import com.vgleadsheets.components.LoadingNameCaptionListModel
import com.vgleadsheets.components.SectionHeaderListModel
import com.vgleadsheets.components.SingleTextListModel
import com.vgleadsheets.features.main.hud.HudViewModel
import com.vgleadsheets.recyclerview.ComponentAdapter
import com.vgleadsheets.setInsetListenerForPadding
import com.vgleadsheets.storage.Setting
import kotlinx.android.synthetic.main.fragment_debug.list_debug
import timber.log.Timber
import javax.inject.Inject

class DebugFragment : VglsFragment(), CheckableListModel.EventHandler,
    SingleTextListModel.Handler, DropdownSettingListModel.EventHandler {
    override fun onClicked(clicked: SingleTextListModel) {
        getFragmentRouter().showAbout()
    }

    override fun onClicked(clicked: CheckableListModel) {
        viewModel.setSetting(clicked.settingId, !clicked.checked)
    }

    override fun onNewOptionSelected(settingId: String, value: String) {
        Timber.w("$settingId value changed to $value")
    }

    @Inject
    lateinit var debugViewModelFactory: DebugViewModel.Factory

    private val hudViewModel: HudViewModel by existingViewModel()

    private val viewModel: DebugViewModel by fragmentViewModel()

    private val adapter = ComponentAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topOffset = resources.getDimension(R.dimen.height_search_bar).toInt() +
                resources.getDimension(R.dimen.margin_large).toInt()
        val bottomOffset = resources.getDimension(R.dimen.height_bottom_sheet_peek).toInt() +
                resources.getDimension(R.dimen.margin_medium).toInt()

        list_debug.adapter = adapter
        list_debug.layoutManager = LinearLayoutManager(context)
        list_debug.setInsetListenerForPadding(
            topOffset = topOffset,
            bottomOffset = bottomOffset
        )
    }

    override fun invalidate() = withState(viewModel) { state ->
        hudViewModel.alwaysShowBack()

        // To prevent flashing
        if (state.settings is Loading) return@withState

        val listModels = constructList(state.settings)
        adapter.submitList(listModels)
    }

    override fun getLayoutId() = R.layout.fragment_debug

    override fun getVglsFragmentTag() = this.javaClass.simpleName

    private fun constructList(settings: Async<List<Setting>>): List<ListModel> {
        return when (settings) {
            is Uninitialized -> createLoadingListModels()
            is Fail -> createErrorListModels(settings.error)
            is Success -> createSuccessListModels(settings())
            else -> throw IllegalStateException()
        }
    }

    private fun createSuccessListModels(settings: List<Setting>): List<ListModel> {
        val networkSection = createSection(settings, HEADER_ID_NETWORK)
        return networkSection
    }

    private fun createSection(
        settings: List<Setting>,
        headerId: String
    ): List<ListModel> {
        val headerModels = listOf(
            SectionHeaderListModel(
                getSectionHeaderString(headerId)
            )
        )

        val settingsModels = settings
            .filter { it.settingId.startsWith(headerId) }
            .map {
                CheckableListModel(
                    it.settingId,
                    getString(it.displayStringId),
                    it.value,
                    this
                )
            }

        val dropdownModels = listOf(
            DropdownSettingListModel(
                "testSetting",
                "Fake Setting",
                0,
                listOf("Mock", "Real"),
                listOf("api_mock", "api_real"),
                this
            )
        )

        return headerModels + settingsModels + dropdownModels
    }

    private fun getSectionHeaderString(headerId: String) = when (headerId) {
        HEADER_ID_NETWORK -> getString(R.string.section_network)
        HEADER_ID_DATABASE -> getString(R.string.section_database)
        else -> throw IllegalArgumentException()
    }

    private fun createErrorListModels(error: Throwable) =
        listOf(ErrorStateListModel(error.message ?: "Unknown Error"))

    private fun createLoadingListModels(): ArrayList<ListModel> {
        val loadingModels = ArrayList<ListModel>(LOADING_ITEMS)

        for (index in 0 until LOADING_ITEMS) {
            loadingModels.add(
                LoadingNameCaptionListModel(index)
            )
        }

        return loadingModels
    }

    companion object {
        const val LOADING_ITEMS = 4

        const val HEADER_ID_NETWORK = "DEBUG_NETWORK"
        const val HEADER_ID_DATABASE = "DEBUG_DATABASE"

        fun newInstance() = DebugFragment()
    }
}
