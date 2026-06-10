package com.dicoding.tugas_akhir.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.dicoding.tugas_akhir.ui.screens.home.SearchResultScreen
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
import com.dicoding.tugas_akhir.data.dummy.dummyShipSchedules
import com.dicoding.tugas_akhir.ui.screens.auth.AuthRequiredScreen
import com.dicoding.tugas_akhir.ui.screens.auth.LoginScreen
import com.dicoding.tugas_akhir.ui.screens.auth.RegisterScreen
import com.dicoding.tugas_akhir.ui.screens.auth.signInWithGoogle
import com.dicoding.tugas_akhir.ui.screens.onboarding.OnboardingScreen
import com.dicoding.tugas_akhir.ui.screens.splash.SplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val prefs = remember {
        context.getSharedPreferences(
            "app_preferences",
            android.content.Context.MODE_PRIVATE
        )
    }

    var hasSeenOnboarding by remember {
        mutableStateOf(prefs.getBoolean("has_seen_onboarding", false))
    }

    var isLoggedIn by remember {
        mutableStateOf(auth.currentUser != null)
    }

    var pendingProtectedRoute by remember {
        mutableStateOf<String?>(null)
    }

    val bottomBarRoutes = listOf(
        Screens.Home,
        Screens.Schedule,
        Screens.MyTicket,
        Screens.Notification,
        Screens.Profile
    )

    val hideTopBarRoutes = listOf(
        Screens.Splash,
        Screens.Onboarding,
        Screens.Login,
        Screens.Register,
        Screens.Home,
        Screens.Schedule
    )

    val protectedRoutes = listOf(
        Screens.MyTicket,
        Screens.Notification,
        Screens.Profile
    )

    val showBottomBar = currentRoute != null &&
            currentRoute in bottomBarRoutes

    val showTopBar = currentRoute != null &&
            currentRoute !in hideTopBarRoutes

    val showBackTopBar = currentRoute != null &&
            currentRoute !in bottomBarRoutes &&
            currentRoute !in hideTopBarRoutes

    var originPort by remember { mutableStateOf<Port?>(null) }
    var destinationPort by remember { mutableStateOf<Port?>(null) }
    var selectedDate by remember { mutableStateOf("") }

    fun navigateAfterAuthSuccess() {
        val targetRoute = pendingProtectedRoute ?: Screens.Home
        val fromProtectedRoute = pendingProtectedRoute != null

        pendingProtectedRoute = null

        navController.navigate(targetRoute) {
            popUpTo(
                if (fromProtectedRoute) {
                    Screens.AuthRequired
                } else {
                    Screens.Login
                }
            ) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            if (showTopBar) {
                if (showBackTopBar) {
                    AppBackTopBar(
                        title = getTopBarTitle(currentRoute.orEmpty()),
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                } else {
                    AppTopBar(
                        title = getTopBarTitle(currentRoute.orEmpty())
                    )
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    currentRoute = currentRoute.orEmpty(),
                    onItemClick = { item ->
                        if (!isLoggedIn && item.route in protectedRoutes) {
                            pendingProtectedRoute = item.route

                            navController.navigate(Screens.AuthRequired) {
                                launchSingleTop = true
                            }
                        } else {
                            navController.navigate(item.route) {
                                popUpTo(Screens.Home) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.Splash,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screens.Splash) {
                SplashScreen(
                    onSplashFinished = {
                        val nextRoute = if (hasSeenOnboarding) {
                            Screens.Home
                        } else {
                            Screens.Onboarding
                        }

                        navController.navigate(nextRoute) {
                            popUpTo(Screens.Splash) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screens.Onboarding) {
                OnboardingScreen(
                    onFinishClick = {
                        hasSeenOnboarding = true

                        prefs.edit()
                            .putBoolean("has_seen_onboarding", true)
                            .apply()

                        navController.navigate(Screens.Login) {
                            popUpTo(Screens.Onboarding) {
                                inclusive = true
                            }
                        }
                    }
                )
            }

            composable(Screens.Login) {
                LoginScreen(
                    onLoginClick = { email, password, onError ->
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                isLoggedIn = true
                                navigateAfterAuthSuccess()
                            }
                            .addOnFailureListener { exception ->
                                onError(exception.message ?: "Login gagal")
                            }
                    },
                    onGoogleLoginClick = { onError ->
                        signInWithGoogle(
                            context = context,
                            auth = auth,
                            scope = scope,
                            onSuccess = {
                                isLoggedIn = true
                                navigateAfterAuthSuccess()
                            },
                            onError = onError
                        )
                    },
                    onRegisterClick = {
                        navController.navigate(Screens.Register)
                    },
                    onContinueAsGuestClick = {
                        val fromProtectedRoute = pendingProtectedRoute != null
                        pendingProtectedRoute = null

                        navController.navigate(Screens.Home) {
                            popUpTo(
                                if (fromProtectedRoute) {
                                    Screens.AuthRequired
                                } else {
                                    Screens.Login
                                }
                            ) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screens.Register) {
                RegisterScreen(
                    onRegisterClick = { name, email, password, onError ->
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnSuccessListener {
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                auth.currentUser?.updateProfile(profileUpdates)

                                isLoggedIn = true
                                navigateAfterAuthSuccess()
                            }
                            .addOnFailureListener { exception ->
                                onError(exception.message ?: "Daftar akun gagal")
                            }
                    },
                    onLoginClick = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screens.AuthRequired) {
                AuthRequiredScreen(
                    onLoginClick = {
                        navController.navigate(Screens.Login) {
                            launchSingleTop = true
                        }
                    },
                    onBackHomeClick = {
                        pendingProtectedRoute = null

                        navController.navigate(Screens.Home) {
                            popUpTo(Screens.Home) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
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
                    onScheduleClick = { scheduleId ->
                        navController.navigate(Screens.scheduleDetail(scheduleId))
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
                    onScheduleClick = { scheduleId ->
                        navController.navigate(Screens.scheduleDetail(scheduleId))
                    }
                )
            }

            composable(Screens.Schedule) {
                ScheduleScreen(
                    onScheduleClick = { scheduleId ->
                        navController.navigate(Screens.scheduleDetail(scheduleId))
                    }
                )
            }

            composable(
                route = Screens.ScheduleDetail,
                arguments = listOf(
                    navArgument("scheduleId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val scheduleId = backStackEntry.arguments?.getInt("scheduleId")
                val schedule = dummyShipSchedules.find { it.id == scheduleId }

                ScheduleDetailScreen(
                    schedule = schedule,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onBookTicketClick = {
                        if (isLoggedIn) {
                            navController.navigate(Screens.PassengerForm)
                        } else {
                            pendingProtectedRoute = Screens.PassengerForm

                            navController.navigate(Screens.AuthRequired) {
                                launchSingleTop = true
                            }
                        }
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
                val user = auth.currentUser

                ProfileScreen(
                    name = user?.displayName ?: "Vira Sare",
                    email = user?.email ?: "virasare@gmail.com",
                    onLogoutClick = {
                        scope.launch {
                            val credentialManager = CredentialManager.create(context)

                            auth.signOut()
                            credentialManager.clearCredentialState(ClearCredentialStateRequest())

                            isLoggedIn = false
                            pendingProtectedRoute = null

                            navController.navigate(Screens.Home) {
                                popUpTo(Screens.Home) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    }
}

private fun getTopBarTitle(route: String): String {
    return when (route) {
        Screens.AuthRequired -> "Login Diperlukan"
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