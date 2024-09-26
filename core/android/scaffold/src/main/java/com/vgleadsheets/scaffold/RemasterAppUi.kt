package com.vgleadsheets.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vgleadsheets.appcomm.VglsAction
import com.vgleadsheets.bottombar.NavBarItem
import com.vgleadsheets.bottombar.NavBarViewModel
import com.vgleadsheets.nav.Destination
import com.vgleadsheets.nav.NavState
import com.vgleadsheets.nav.NavViewModel
import com.vgleadsheets.nav.SystemUiVisibility
import com.vgleadsheets.search.searchScreenNavEntry
import com.vgleadsheets.topbar.RemasterTopBar
import com.vgleadsheets.topbar.TopBarViewModel
import com.vgleadsheets.ui.licenses.licensesScreenNavEntry
import com.vgleadsheets.ui.list.listScreenEntry
import com.vgleadsheets.ui.viewer.viewerScreenNavEntry

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("MaxLineLength")
@Composable
fun RemasterAppUi(
    showSystemBars: () -> Unit,
    hideSystemBars: () -> Unit,
    modifier: Modifier
) {
    val navController = rememberNavController()

    val snackbarScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topBarState)

    val navViewModel = hiltViewModel<NavViewModel>()

    navViewModel.navController = navController
    navViewModel.snackbarScope = snackbarScope
    navViewModel.snackbarHostState = snackbarHostState

    LaunchedEffect(Unit) { navViewModel.startWatchingBackstack() }

    val navState by navViewModel.uiState.collectAsState()
    handleSystemBars(navState, showSystemBars, hideSystemBars)

    val topBarViewModel: TopBarViewModel = hiltViewModel()
    val topBarVmState by topBarViewModel.uiState.collectAsState()
    val topBarActionHandler = remember { { action: VglsAction -> topBarViewModel.sendAction(action) } }

    val navBarViewModel: NavBarViewModel = hiltViewModel()
    val bottomBarVmState by navBarViewModel.uiState.collectAsState()

    AppContent(
        navController = navController,
        mainContent = { innerPadding -> MainContent(navController, innerPadding, topBarState) },
        topBarContent = { RemasterTopBar(state = topBarVmState, scrollBehavior = scrollBehavior, handleAction = topBarActionHandler) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    )
}

@Composable
fun AppContent(
    navController: NavHostController = rememberNavController(),
    mainContent: @Composable (PaddingValues) -> Unit,
    topBarContent: @Composable () -> Unit,
    snackbarHost: @Composable () -> Unit,
    modifier: Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val orientation = LocalConfiguration.current.orientation
    val scaffoldInsetPadding = calculateCutoutInset()

    NavigationSuiteScaffold(
        modifier = scaffoldInsetPadding,
        layoutType = calculateNavSuiteType(
            currentWindowAdaptiveInfo(),
            orientation
        ),

        navigationSuiteItems = {
            NavBarItem.entries.forEach { navItem ->
                item(
                    icon = { Icon(navItem.icon, contentDescription = navItem.name) },
                    label = { Text(navItem.label) },
                    selected = currentRoute == navItem.route,
                    onClick = {
                        navController.navigate(navItem.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                )
            }
        },
        content = {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = topBarContent,
                snackbarHost = snackbarHost,
                content = { innerPadding -> mainContent(innerPadding) },
                modifier = modifier,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainContent(
    navController: NavHostController,
    innerPadding: PaddingValues,
    topBarState: TopAppBarState
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = Destination.HOME.template(),
        enterTransition = { enterTransition() },
        exitTransition = { exitTransition() },
        popEnterTransition = { popEnterTransition() },
        popExitTransition = { popExitTransition() },
    ) {
        val globalModifier = Modifier.fillMaxSize()

        Destination.entries.forEach { destination ->
            if (!destination.isImplemented) {
                return@forEach
            }

            listScreenEntry(
                destination = destination,
                topBarState = topBarState,
                globalModifier = globalModifier,
            )
        }

        searchScreenNavEntry(
            topBarState = topBarState,
            globalModifier = globalModifier,
        )

        viewerScreenNavEntry(
            topBarState = topBarState,
            globalModifier = globalModifier,
        )

        licensesScreenNavEntry(
            topBarState = topBarState,
            globalModifier = globalModifier,
        )
    }
}

private fun handleSystemBars(
    navState: NavState,
    showSystemBars: () -> Unit,
    hideSystemBars: () -> Unit
) {
    when (navState.visibility) {
        SystemUiVisibility.VISIBLE -> showSystemBars()
        SystemUiVisibility.HIDDEN -> hideSystemBars()
    }
}
