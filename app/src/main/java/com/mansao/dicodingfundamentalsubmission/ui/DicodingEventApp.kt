package com.mansao.dicodingfundamentalsubmission.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.mansao.dicodingfundamentalsubmission.ui.navigation.BottomNavigationItems
import com.mansao.dicodingfundamentalsubmission.ui.navigation.Screen
import com.mansao.dicodingfundamentalsubmission.ui.screen.detail.DetailScreen
import com.mansao.dicodingfundamentalsubmission.ui.screen.finish.FinishedScreen
import com.mansao.dicodingfundamentalsubmission.ui.screen.home.HomeScreen
import com.mansao.dicodingfundamentalsubmission.ui.screen.upcoming.UpcomingScreen
import com.mansao.dicodingfundamentalsubmission.ui.screen.settings.SettingsScreen

@Composable
fun DicodingEventApp(
    onDarkModeChanged: (Boolean) -> Unit,
    onNotificationChanged: (Boolean) -> Unit

) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                currentDestination = currentDestination
            )
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        ) {
            NavHost(navController = navController, startDestination = Screen.Home) {
                composable<Screen.Home> {
                    HomeScreen(
                        navigateToDetail = { eventId ->
                            navController.navigate(Screen.Detail(eventId))
                        },
                        navigateToSettings = {
                            navController.navigate(Screen.Settings)
                        }
                    )
                }

                composable<Screen.Detail> {
                    val data = it.toRoute<Screen.Detail>()
                    DetailScreen(data.id)
                }

                composable<Screen.Upcoming> {
                    UpcomingScreen(
                        navigateToDetail = { eventId ->
                            navController.navigate(Screen.Detail(eventId))

                        }
                    )
                }

                composable<Screen.Finished> {
                    FinishedScreen(
                        navigateToDetail = { eventId ->
                            navController.navigate(Screen.Detail(eventId))

                        }
                    )
                }

                composable<Screen.Settings> {
                    SettingsScreen(onDarkModeChanged = onDarkModeChanged, onNotificationChanged = onNotificationChanged)
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, currentDestination: NavDestination?) {
    val bottomNavigationItems = listOf(
        BottomNavigationItems("home", Icons.Outlined.Home, Screen.Home, "Home"),
        BottomNavigationItems("upcoming", Icons.Outlined.WbSunny, Screen.Upcoming, "Upcoming"),
        BottomNavigationItems("finished", Icons.Outlined.DarkMode, Screen.Finished, "Finished"),
    )
    NavigationBar {
        bottomNavigationItems.forEach { navigationItem ->

            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.hasRoute(navigationItem.screen::class) } == true,
                label = {
                    Text(text = navigationItem.title)
                },
                icon = {
                    Icon(
                        imageVector = navigationItem.icon,
                        contentDescription = navigationItem.contentDescription
                    )
                },
                onClick = {
                    navController.navigate(navigationItem.screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                )
        }
    }

}