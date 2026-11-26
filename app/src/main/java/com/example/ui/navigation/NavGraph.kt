package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.*
import com.example.viewmodel.AdminViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Apps : Screen("apps", "Apps", Icons.Default.Apps)
    object Tools : Screen("tools", "Tools", Icons.Default.Build)
    object Security : Screen("security", "Security", Icons.Default.Security)
    object Logs : Screen("logs", "Logs", Icons.Default.Terminal)
}

@Composable
fun NavGraph(viewModel: AdminViewModel) {
    val navController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val items = listOf(Screen.Dashboard, Screen.Apps, Screen.Tools, Screen.Security, Screen.Logs)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    state = state,
                    onToggleRoot = { viewModel.toggleRootStatus() },
                    onNavigateToTools = { navController.navigate(Screen.Tools.route) }
                )
            }
            composable(Screen.Apps.route) {
                AppManagerScreen(apps = state.apps)
            }
            composable(Screen.Tools.route) {
                ToolLoaderScreen(
                    tools = state.tools,
                    onRunTool = { toolId -> viewModel.runTool(toolId) }
                )
            }
            composable(Screen.Security.route) {
                SecurityAuditScreen(checks = state.securityChecks)
            }
            composable(Screen.Logs.route) {
                LogMonitorScreen(
                    logs = state.logs,
                    terminalOutput = state.terminalOutput,
                    onExecuteCommand = { cmd -> viewModel.executeCommand(cmd) }
                )
            }
        }
    }
}
