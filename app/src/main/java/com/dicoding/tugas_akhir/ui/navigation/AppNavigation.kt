package com.dicoding.tugas_akhir.ui.navigation

import android.content.Context
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
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dicoding.tugas_akhir.data.dummy.ETicketData
import com.dicoding.tugas_akhir.data.dummy.PassengerData
import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.data.dummy.TicketClassOption
import com.dicoding.tugas_akhir.data.dummy.dummyNotifications
import com.dicoding.tugas_akhir.data.dummy.dummyPorts
import com.dicoding.tugas_akhir.data.dummy.popularRoutes
import com.dicoding.tugas_akhir.ui.components.dialog.navigation.AppBackTopBar
import com.dicoding.tugas_akhir.ui.components.dialog.navigation.AppBottomNavigationBar
import com.dicoding.tugas_akhir.ui.components.dialog.navigation.AppTopBar
import com.dicoding.tugas_akhir.ui.screens.auth.AuthRequiredScreen
import com.dicoding.tugas_akhir.ui.screens.auth.LoginScreen
import com.dicoding.tugas_akhir.ui.screens.auth.RegisterScreen
import com.dicoding.tugas_akhir.ui.screens.auth.signInWithGoogle
import com.dicoding.tugas_akhir.ui.screens.booking.BookingSummaryScreen
import com.dicoding.tugas_akhir.ui.screens.booking.PassengerFormScreen
import com.dicoding.tugas_akhir.ui.screens.booking.SelectTicketScreen
import com.dicoding.tugas_akhir.ui.screens.home.HomeScreen
import com.dicoding.tugas_akhir.ui.screens.home.PopularRouteResultScreen
import com.dicoding.tugas_akhir.ui.screens.home.PortSearchScreen
import com.dicoding.tugas_akhir.ui.screens.home.SearchResultScreen
import com.dicoding.tugas_akhir.ui.screens.myticket.ETicketScreen
import com.dicoding.tugas_akhir.ui.screens.myticket.MyTicketScreen
import com.dicoding.tugas_akhir.ui.screens.notification.NotificationDetailScreen
import com.dicoding.tugas_akhir.ui.screens.notification.NotificationScreen
import com.dicoding.tugas_akhir.ui.screens.onboarding.OnboardingScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentFailedScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentSuccessScreen
import com.dicoding.tugas_akhir.ui.screens.payment.PaymentWaitingScreen
import com.dicoding.tugas_akhir.ui.screens.profile.AboutAppScreen
import com.dicoding.tugas_akhir.ui.screens.profile.EditProfileScreen
import com.dicoding.tugas_akhir.ui.screens.profile.HelpDetailScreen
import com.dicoding.tugas_akhir.ui.screens.profile.HelpScreen
import com.dicoding.tugas_akhir.ui.screens.profile.LanguageSettingScreen
import com.dicoding.tugas_akhir.ui.screens.profile.PassengerDataScreen
import com.dicoding.tugas_akhir.ui.screens.profile.PassengerProfileFormScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileScreen
import com.dicoding.tugas_akhir.ui.screens.profile.SettingsScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ThemeSettingScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleDetailScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleScreen
import com.dicoding.tugas_akhir.ui.screens.splash.SplashScreen
import com.dicoding.tugas_akhir.ui.theme.Background
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.launch

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
            Context.MODE_PRIVATE
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

    var originPort by remember {
        mutableStateOf<Port?>(null)
    }

    var destinationPort by remember {
        mutableStateOf<Port?>(null)
    }

    var selectedDate by remember {
        mutableStateOf("")
    }

    var selectedBookingSchedule by remember {
        mutableStateOf<ShipSchedule?>(null)
    }

    var selectedTicketClass by remember {
        mutableStateOf<TicketClassOption?>(null)
    }

    var passengerData by remember {
        mutableStateOf<PassengerData?>(null)
    }

    var passengerList by remember {
        mutableStateOf<List<PassengerData>>(emptyList())
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

    var selectedETicketData by remember {
        mutableStateOf<ETicketData?>(null)
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
                        navController.navigate(Screens.scheduleDetail(scheduleId.toString()))
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
                val selectedPopularRoute = popularRoutes.find {
                    it.id == routeId
                }

                PopularRouteResultScreen(
                    popularRoute = selectedPopularRoute,
                    onScheduleClick = { scheduleId ->
                        navController.navigate(Screens.scheduleDetail(scheduleId.toString()))
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
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val scheduleId = backStackEntry.arguments?.getString("scheduleId").orEmpty()

                ScheduleDetailScreen(
                    scheduleId = scheduleId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onBookTicketClick = { selectedScheduleId ->
                        navController.navigate(Screens.selectTicket(selectedScheduleId))
                    }
                )
            }

            composable(
                route = Screens.SelectTicket,
                arguments = listOf(
                    navArgument("scheduleId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val scheduleId = backStackEntry.arguments?.getString("scheduleId").orEmpty()

                SelectTicketScreen(
                    scheduleId = scheduleId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onContinueClick = { selectedScheduleId, ticketClassId, ticketPrice, passengerCount ->
                        navController.navigate(
                            Screens.passengerForm(
                                scheduleId = selectedScheduleId,
                                ticketClassId = ticketClassId,
                                ticketPrice = ticketPrice,
                                passengerCount = passengerCount
                            )
                        )
                    }
                )
            }

            composable(
                route = Screens.PassengerForm,
                arguments = listOf(
                    navArgument("scheduleId") {
                        type = NavType.StringType
                    },
                    navArgument("ticketClassId") {
                        type = NavType.StringType
                    },
                    navArgument("ticketPrice") {
                        type = NavType.IntType
                    },
                    navArgument("passengerCount") {
                        type = NavType.IntType
                    },
                )
            ) { backStackEntry ->
                val scheduleId = backStackEntry.arguments?.getString("scheduleId").orEmpty()
                val ticketClassId = backStackEntry.arguments?.getString("ticketClassId").orEmpty()
                val ticketPrice = backStackEntry.arguments?.getInt("ticketPrice") ?: 0
                val passengerCount = backStackEntry.arguments?.getInt("passengerCount") ?: 1

                PassengerFormScreen(
                    scheduleId = scheduleId,
                    ticketClassId = ticketClassId,
                    ticketPrice = ticketPrice,
                    passengerCount = passengerCount,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onBookingCreated = { bookingId ->
                        navController.navigate(Screens.bookingSummary(bookingId))
                    }
                )
            }

            composable(
                route = Screens.BookingSummary,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                BookingSummaryScreen(
                    bookingId = bookingId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentClick = { selectedBookingId ->
                        navController.navigate(Screens.payment(selectedBookingId))
                    }
                )
            }

            composable(
                route = Screens.Payment,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                PaymentScreen(
                    bookingId = bookingId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentCreated = { paymentId ->
                        navController.navigate(Screens.paymentWaiting(paymentId))
                    }
                )
            }

            composable(
                route = Screens.PaymentWaiting,
                arguments = listOf(
                    navArgument("paymentId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val paymentId = backStackEntry.arguments?.getString("paymentId").orEmpty()

                PaymentWaitingScreen(
                    paymentId = paymentId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPaymentSuccess = { selectedPaymentId ->
                        navController.navigate(Screens.paymentSuccess(selectedPaymentId))
                    },
                    onPaymentFailed = { selectedPaymentId ->
                        navController.navigate(Screens.paymentFailed(selectedPaymentId))
                    }
                )
            }

            composable(
                route = Screens.PaymentSuccess,
                arguments = listOf(
                    navArgument("paymentId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val paymentId = backStackEntry.arguments?.getString("paymentId").orEmpty()

                PaymentSuccessScreen(
                    paymentId = paymentId,
                    onViewTicketClick = {
                        navController.navigate(Screens.eTicketByPayment(paymentId))
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

            composable(
                route = Screens.PaymentFailed,
                arguments = listOf(
                    navArgument("paymentId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val paymentId = backStackEntry.arguments?.getString("paymentId").orEmpty()

                PaymentFailedScreen(
                    paymentId = paymentId,
                    onRetryClick = {
                        navController.popBackStack()
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
                    onTicketClick = { bookingId ->
                        navController.navigate(Screens.eTicket(bookingId))
                    },
                    onPayNowClick = { bookingId ->
                        navController.navigate(Screens.payment(bookingId))
                    }
                )
            }

            composable(
                route = Screens.ETicket,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                ETicketScreen(
                    bookingId = bookingId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = Screens.ETicketByPayment,
                arguments = listOf(
                    navArgument("paymentId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val paymentId = backStackEntry.arguments?.getString("paymentId").orEmpty()

                ETicketScreen(
                    paymentId = paymentId,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.Notification) {
                NotificationScreen(
                    onNotificationClick = { notificationId ->
                        navController.navigate(Screens.notificationDetail(notificationId))
                    }
                )
            }

            composable(
                route = Screens.NotificationDetail,
                arguments = listOf(
                    navArgument("notificationId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val notificationId = backStackEntry.arguments?.getInt("notificationId")
                val notification = dummyNotifications.find {
                    it.id == notificationId
                }

                NotificationDetailScreen(
                    notification = notification,
                    onSeeTicketClick = {
                        navController.navigate(Screens.ETicket)
                    }
                )
            }

            composable(Screens.Profile) {
                val user = auth.currentUser

                ProfileScreen(
                    name = user?.displayName ?: "Vira Sare",
                    email = user?.email ?: "virasare@gmail.com",
                    onEditProfileClick = {
                        navController.navigate(Screens.ProfileEdit)
                    },
                    onPassengerDataClick = {
                        navController.navigate(Screens.ProfilePassengerData)
                    },
                    onSettingsClick = {
                        navController.navigate(Screens.ProfileSettings)
                    },
                    onHelpClick = {
                        navController.navigate(Screens.ProfileHelp)
                    },
                    onAboutClick = {
                        navController.navigate(Screens.ProfileAbout)
                    },
                    onLogoutClick = {
                        scope.launch {
                            val credentialManager = CredentialManager.create(context)

                            auth.signOut()
                            credentialManager.clearCredentialState(
                                ClearCredentialStateRequest()
                            )

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

            composable(Screens.ProfileEdit) {
                val user = auth.currentUser

                EditProfileScreen(
                    initialName = user?.displayName ?: "Vira Sare",
                    initialEmail = user?.email ?: "virasare@gmail.com",
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.ProfilePassengerData) {
                PassengerDataScreen(
                    onAddPassengerClick = {
                        navController.navigate(Screens.ProfilePassengerForm)
                    }
                )
            }

            composable(Screens.ProfilePassengerForm) {
                PassengerProfileFormScreen(
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.ProfileSettings) {
                SettingsScreen(
                    onLanguageClick = {
                        navController.navigate(Screens.ProfileLanguage)
                    },
                    onThemeClick = {
                        navController.navigate(Screens.ProfileTheme)
                    },
                    onAboutClick = {
                        navController.navigate(Screens.ProfileAbout)
                    },
                    onHelpClick = {
                        navController.navigate(Screens.ProfileHelp)
                    }
                )
            }

            composable(Screens.ProfileLanguage) {
                LanguageSettingScreen(
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.ProfileTheme) {
                ThemeSettingScreen(
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.ProfileHelp) {
                HelpScreen(
                    onHelpItemClick = { helpId ->
                        navController.navigate(Screens.profileHelpDetail(helpId))
                    }
                )
            }

            composable(
                route = Screens.ProfileHelpDetail,
                arguments = listOf(
                    navArgument("helpId") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val helpId = backStackEntry.arguments?.getInt("helpId") ?: 0

                HelpDetailScreen(
                    helpId = helpId
                )
            }

            composable(Screens.ProfileAbout) {
                AboutAppScreen()
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
        Screens.SelectTicket -> "Pilih Tiket"
        Screens.PassengerForm -> "Data Penumpang"
        Screens.BookingSummary -> "Ringkasan Pesanan"
        Screens.Payment -> "Pembayaran"
        Screens.PaymentWaiting -> "Menunggu Pembayaran"
        Screens.PaymentFailed -> "Status Pembayaran"
        Screens.PaymentSuccess -> "Status Pembayaran"
        Screens.MyTicket -> "Pesanan Saya"
        Screens.ETicket -> "E-Ticket"
        Screens.Notification -> "Notifikasi"
        Screens.NotificationDetail -> "Detail Notifikasi"
        Screens.Profile -> "Profil"
        Screens.PortSearchRoute -> "Pilih Pelabuhan"
        Screens.SearchResult -> "Hasil Pencarian"
        Screens.PopularRouteResult -> "Rute Populer"
        Screens.ProfileEdit -> "Edit Profil"
        Screens.ProfilePassengerData -> "Data Penumpang"
        Screens.ProfilePassengerForm -> "Tambah Penumpang"
        Screens.ProfileSettings -> "Pengaturan"
        Screens.ProfileLanguage -> "Bahasa"
        Screens.ProfileTheme -> "Tema"
        Screens.ProfileHelp -> "Bantuan"
        Screens.ProfileHelpDetail -> "Detail Bantuan"
        Screens.ProfileAbout -> "Tentang Aplikasi"
        else -> ""
    }
}