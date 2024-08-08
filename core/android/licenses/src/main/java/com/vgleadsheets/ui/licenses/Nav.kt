package com.vgleadsheets.ui.licenses

import androidx.activity.compose.BackHandler
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.nav.Destination

@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.licensesScreenNavEntry(
    topBarState: TopAppBarState,
    globalModifier: Modifier,
) {
    composable(
        route = Destination.LICENSES.template(),
    ) {
        val viewModel: LicenseViewModel = hiltViewModel()

        LaunchedEffect(Unit) {
            topBarState.heightOffset = 0.0f
            topBarState.contentOffset = 0.0f
            viewModel.sendAction(VglsAction.Resume)
        }

        BackHandler(true) { viewModel.sendAction(VglsAction.DeviceBack) }

        val state by viewModel.uiState.collectAsStateWithLifecycle()

        LicenseScreen(
            state = state,
            modifier = globalModifier
        )
    }
}
