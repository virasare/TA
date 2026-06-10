package com.dicoding.tugas_akhir.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.tugas_akhir.ui.components.navigation.AppBottomNavigationBar
import com.dicoding.tugas_akhir.ui.screens.HomeScreen
import com.dicoding.tugas_akhir.ui.screens.booking.BookingSummaryScreen
import com.dicoding.tugas_akhir.ui.screens.booking.PassengerFormScreen
import com.dicoding.tugas_akhir.ui.screens.myticket.MyTicketScreen
import com.dicoding.tugas_akhir.ui.screens.notification.NotificationScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentSuccessScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleDetailScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleScreen
import com.dicoding.tugas_akhir.ui.screens.ticket.ETicketScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Screens.Home

    val bottomBarRoutes = listOf(
        Screens.Home,
        Screens.Schedule,
        Screens.MyTicket,
        Screens.Notification,
        Screens.Profile
    )

    val showBottomBar = currentRoute in bottomBarRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    currentRoute = currentRoute,
                    onItemClick = { item ->
                        navController.navigate(item.route) {
                            popUpTo(Screens.Home) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Home,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Home) {
                HomeScreen(
                    onSearchScheduleClick = {
                        navController.navigate(Screens.Schedule)
                    },
                    onMyTicketClick = {
                        navController.navigate(Screens.MyTicket)
                    },
                    onNotificationClick = {
                        navController.navigate(Screens.Notification)
                    }
                )
            }

            composable(Screens.Schedule) {
                ScheduleScreen(
                    onScheduleClick = {
                        navController.navigate(Screens.ScheduleDetail)
                    }
                )
            }

            composable(Screens.ScheduleDetail) {
                ScheduleDetailScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onBookTicketClick = {
                        navController.navigate(Screens.PassengerForm)
                    }
                )
            }

            composable(Screens.PassengerForm) {
                PassengerFormScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContinueClick = {
                        navController.navigate(Screens.BookingSummary)
                    }
                )
            }

            composable(Screens.BookingSummary) {
                BookingSummaryScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentClick = {
                        navController.navigate(Screens.Payment)
                    }
                )
            }

            composable(Screens.Payment) {
                PaymentScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentSuccessClick = {
                        navController.navigate(Screens.PaymentSuccess)
                    }
                )
            }

            composable(Screens.PaymentSuccess) {
                PaymentSuccessScreen(
                    onSeeTicketClick = {
                        navController.navigate(Screens.ETicket) {
                            popUpTo(Screens.Home)
                        }
                    },
                    onBackHomeClick = {
                        navController.navigate(Screens.Home) {
                            popUpTo(Screens.Home) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screens.MyTicket) {
                MyTicketScreen(
                    onTicketClick = {
                        navController.navigate(Screens.ETicket)
                    }
                )
            }

            composable(Screens.ETicket) {
                ETicketScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.Notification) {
                NotificationScreen()
            }

            composable(Screens.Profile) {
                ProfileScreen()
            }
        }
    }
}