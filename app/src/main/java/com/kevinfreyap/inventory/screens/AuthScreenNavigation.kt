package com.kevinfreyap.inventory.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.kevinfreyap.auth.presentation.navigation.AuthScreens
import com.kevinfreyap.auth.presentation.navigation.LoginNavigation
import com.kevinfreyap.auth.presentation.navigation.OnboardNavigation
import com.kevinfreyap.auth.presentation.navigation.RegisterNavigation
import com.kevinfreyap.auth.presentation.screen.auth.LoginScreen
import com.kevinfreyap.auth.presentation.screen.onboard.OnboardScreen
import com.kevinfreyap.auth.presentation.screen.auth.RegisterScreen
import com.kevinfreyap.inventory.AppGraph

fun NavGraphBuilder.authGraph(
    navController: NavHostController
) {
    navigation<AppGraph.Auth>(
        startDestination = AuthScreens.Onboard
    ) {
        composable<AuthScreens.Onboard> {
            OnboardScreen(
                onNavigate = {destination ->
                    when(destination) {
                        OnboardNavigation.Login -> navController.navigate(AuthScreens.Login)
                        OnboardNavigation.Register -> navController.navigate(AuthScreens.Register)
                    }
                }
            )
        }

        composable<AuthScreens.Register> {
            RegisterScreen(
                onNavigate = {destination ->
                    when(destination) {
                        RegisterNavigation.NavigateUp -> {
                            navController.navigateUp()
                        }
                        RegisterNavigation.Login -> {
                            navController.navigate(AuthScreens.Login) {
                                popUpTo<AuthScreens.Register> {
                                    inclusive = true
                                }
                            }
                        }
                        RegisterNavigation.Dashboard -> {
                            navController.navigate(AppGraph.Main) {
                                popUpTo(AppGraph.Auth) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }

        composable<AuthScreens.Login> {
            LoginScreen(
                onNavigate = {destination->
                    when(destination) {
                        LoginNavigation.NavigateUp -> {
                            navController.navigateUp()
                        }
                        LoginNavigation.Register -> {
                            navController.navigate(AuthScreens.Register) {
                                popUpTo<AuthScreens.Login> {
                                    inclusive = true
                                }
                            }
                        }
                        LoginNavigation.Dashboard -> {
                            navController.navigate(AppGraph.Main) {
                                popUpTo(AppGraph.Auth) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}