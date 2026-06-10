package com.dicoding.tugas_akhir.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.dummyPorts
import com.dicoding.tugas_akhir.data.dummy.popularRoutes
import com.dicoding.tugas_akhir.ui.components.navigation.AppBackTopBar
import com.dicoding.tugas_akhir.ui.components.navigation.AppBottomNavigationBar
import com.dicoding.tugas_akhir.ui.components.navigation.AppTopBar
import com.dicoding.tugas_akhir.ui.screens.SearchResultScreen
import com.dicoding.tugas_akhir.ui.screens.home.HomeScreen
import com.dicoding.tugas_akhir.ui.screens.booking.BookingSummaryScreen
import com.dicoding.tugas_akhir.ui.screens.booking.PassengerFormScreen
import com.dicoding.tugas_akhir.ui.screens.home.PopularRouteResultScreen
import com.dicoding.tugas_akhir.ui.screens.home.PortSearchScreen
import com.dicoding.tugas_akhir.ui.screens.myticket.MyTicketScreen
import com.dicoding.tugas_akhir.ui.screens.notification.NotificationScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentSuccessScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleDetailScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleScreen
import com.dicoding.tugas_akhir.ui.screens.ticket.ETicketScreen
import com.dicoding.tugas_akhir.ui.theme.Background

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
    val showTopBar = currentRoute != Screens.Home

    val showBackTopBar = currentRoute !in bottomBarRoutes
            && currentRoute != Screens.Home

    var originPort by remember { mutableStateOf<Port?>(null) }
    var destinationPort by remember { mutableStateOf<Port?>(null) }
    var selectedDate by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Background,
        topBar = {
            if (showTopBar) {
                if (showBackTopBar) {
                    AppBackTopBar(
                        title = getTopBarTitle(currentRoute),
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    AppTopBar(
                        title = getTopBarTitle(currentRoute)
                    )
                }
            }
        },
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
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
        ) {
            composable(Screens.Home) {
                HomeScreen(
                    originPort = originPort,
                    destinationPort = destinationPort,
                    selectedDate = selectedDate,
                    onOriginClick = {
                        navController.navigate(Screens.portSearch("origin"))
                    },
                    onDestinationClick = {
                        navController.navigate(Screens.portSearch("destination"))
                    },
                    onDateSelected = { date ->
                        selectedDate = date
                    },
                    onSearchScheduleClick = {
                        navController.navigate(Screens.SearchResult)
                    },
                    onPopularRouteClick = { route ->
                        navController.navigate(Screens.popularRouteResult(route.id))
                    }
                )
            }

            composable(
                route = Screens.PortSearchRoute,
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type") ?: "origin"

                PortSearchScreen(
                    ports = dummyPorts,
                    onPortSelected = { port ->
                        if (type == "origin") {
                            originPort = port
                        } else {
                            destinationPort = port
                        }

                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.SearchResult) {
                SearchResultScreen(
                    originPort = originPort,
                    destinationPort = destinationPort,
                    selectedDate = selectedDate,
                    onScheduleClick = {
                        navController.navigate(Screens.ScheduleDetail)
                    },
                    onBackToHomeClick = {
                        navController.popBackStack()
                    },
                    onSeeAllSchedulesClick = {
                        navController.navigate(Screens.Schedule)
                    }
                )
            }

            composable(
                route = Screens.PopularRouteResult,
                arguments = listOf(
                    navArgument("routeId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val routeId = backStackEntry.arguments?.getInt("routeId")
                val selectedPopularRoute = popularRoutes.find { it.id == routeId }

                PopularRouteResultScreen(
                    popularRoute = selectedPopularRoute,
                    onScheduleClick = {
                        navController.navigate(Screens.ScheduleDetail)
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
                        navController.navigate(Screens.ETicket)
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

private fun getTopBarTitle(route: String): String {
    return when (route) {
        Screens.Home -> "Beranda"
        Screens.Schedule -> "Jadwal Kapal"
        Screens.ScheduleDetail -> "Detail Jadwal"
        Screens.PassengerForm -> "Data Penumpang"
        Screens.BookingSummary -> "Ringkasan Pesanan"
        Screens.Payment -> "Pembayaran"
        Screens.PaymentSuccess -> "Pembayaran Berhasil"
        Screens.MyTicket -> "Pesanan Saya"
        Screens.ETicket -> "E-Ticket"
        Screens.Notification -> "Notifikasi"
        Screens.Profile -> "Profil"
        Screens.PortSearchRoute -> "Pilih Pelabuhan"
        Screens.SearchResult -> "Hasil Pencarian"
        Screens.PopularRouteResult -> "Rute Populer"
        else -> ""
    }
}