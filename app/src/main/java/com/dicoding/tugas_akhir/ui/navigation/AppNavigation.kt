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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.dicoding.tugas_akhir.data.dummy.dummyPorts
import com.dicoding.tugas_akhir.data.dummy.popularRoutes
import com.dicoding.tugas_akhir.domain.model.Booking
import com.dicoding.tugas_akhir.domain.model.NotificationType
import com.dicoding.tugas_akhir.ui.components.dialog.navigation.AppBackTopBar
import com.dicoding.tugas_akhir.ui.components.dialog.navigation.AppBottomNavigationBar
import com.dicoding.tugas_akhir.ui.components.dialog.navigation.AppTopBar
import com.dicoding.tugas_akhir.ui.screens.auth.AuthRequiredScreen
import com.dicoding.tugas_akhir.ui.screens.auth.LoginScreen
import com.dicoding.tugas_akhir.ui.screens.auth.RegisterScreen
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
import com.dicoding.tugas_akhir.ui.screens.profile.PassengerDataScreen
import com.dicoding.tugas_akhir.ui.screens.profile.PassengerProfileFormScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileHelpDetailScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileHelpScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileScreen
import com.dicoding.tugas_akhir.ui.screens.profile.SettingsScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleDetailScreen
import com.dicoding.tugas_akhir.ui.screens.schedule.ScheduleScreen
import com.google.firebase.auth.FirebaseAuth
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileLanguageScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileSecurityScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileTextSizeScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileThemeScreen
import com.dicoding.tugas_akhir.ui.viewmodel.NotificationViewModel
import com.dicoding.tugas_akhir.ui.viewmodel.ViewModelFactory
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import com.dicoding.tugas_akhir.ui.notification.AppSystemNotification
import com.dicoding.tugas_akhir.ui.screens.ticket.RefundScreen
import com.dicoding.tugas_akhir.ui.screens.ticket.RescheduleScreen
import com.dicoding.tugas_akhir.ui.screens.ticket.ManageTicketSuccessScreen
import com.dicoding.tugas_akhir.ui.viewmodel.AuthViewModel
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.dicoding.tugas_akhir.R
import com.dicoding.tugas_akhir.ui.state.AuthUiState
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.dicoding.tugas_akhir.ui.viewmodel.BookingViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.MaterialTheme
import com.dicoding.tugas_akhir.ui.localization.AppStrings
import com.dicoding.tugas_akhir.ui.localization.LocalAppStrings
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {}
    )

    val context = LocalContext.current
    val strings = LocalAppStrings.current
    val factory = ViewModelFactory.getInstance()

    val webClientId = stringResource(
        id = R.string.default_web_client_id
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = factory
    )

    val bookingViewModel: BookingViewModel = viewModel(
        factory = factory
    )

    val credentialManager = remember {
        CredentialManager.create(context)
    }

    val authUiState by authViewModel.authUiState.collectAsStateWithLifecycle()
    val isLoggedIn = authUiState is AuthUiState.Authenticated

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS,
            ) == PackageManager.PERMISSION_GRANTED

            if (!isGranted) {
                notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }

    val scope = rememberCoroutineScope()

    val notificationViewModel: NotificationViewModel = viewModel(
        factory = ViewModelFactory.getInstance()
    )

    val unreadNotificationCount by notificationViewModel.unreadCount.collectAsStateWithLifecycle()

    val auth = remember {
        FirebaseAuth.getInstance()
    }

    val prefs = remember {
        context.getSharedPreferences(
            "app_preferences",
            Context.MODE_PRIVATE
        )
    }

    val localNotificationPrefs = remember {
        context.getSharedPreferences(
            "local_notification_markers",
            Context.MODE_PRIVATE
        )
    }

    val initialHasSeenOnboarding = remember {
        prefs.getBoolean("has_seen_onboarding", false)
    }

    var hasPassedSplash by remember {
        mutableStateOf(false)
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

    var eTicketDownloadRequest by remember { mutableStateOf(0) }

    var homeTicketOverview by remember {
        mutableStateOf<Booking?>(null)
    }

    fun resetSearchInput() {
        originPort = null
        destinationPort = null
        selectedDate = ""
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

    LaunchedEffect(currentRoute) {
        if (currentRoute == Screens.Splash) {
            hasPassedSplash = false
        } else if (!hasPassedSplash) {
            delay(300)
            hasPassedSplash = true
        }
    }

    val showBottomBar = hasPassedSplash &&
            currentRoute != null &&
            currentRoute in bottomBarRoutes

    val showTopBar = currentRoute != null &&
            currentRoute !in hideTopBarRoutes

    val showBackTopBar = currentRoute != null &&
            currentRoute !in bottomBarRoutes &&
            currentRoute !in hideTopBarRoutes

    fun navigateAfterAuthSuccess() {
        val targetRoute = pendingProtectedRoute
        pendingProtectedRoute = null
        resetSearchInput()

        navController.navigate(targetRoute ?: Screens.Home) {
            popUpTo(Screens.Login) {
                inclusive = true
            }

            launchSingleTop = true
        }
    }

    fun navigateToLoginFromGuest() {
        pendingProtectedRoute = null

        navController.navigate(Screens.Login) {
            popUpTo(Screens.Home) {
                inclusive = false
            }

            launchSingleTop = true
        }
    }

    suspend fun getGoogleIdToken(
        filterByAuthorizedAccounts: Boolean,
    ): String {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = credentialManager.getCredential(
            context = context,
            request = request,
        )

        val credential = result.credential

        val googleIdTokenCredential = GoogleIdTokenCredential
            .createFrom(credential.data)

        return googleIdTokenCredential.idToken
    }

    var selectedETicketData by remember {
        mutableStateOf<ETicketData?>(null)
    }

    fun pushNotification(
        title: String,
        message: String,
        type: NotificationType,
    ) {
        notificationViewModel.addNotification(
            title = title,
            message = message,
            type = type,
        )

        AppSystemNotification.show(
            context = context,
            title = title,
            message = message,
        )
    }

    fun markManageProcessStarted(
        bookingId: String,
        status: String,
    ) {
        val key = bookingId.toManageProcessStartKey(status)

        if (!localNotificationPrefs.contains(key)) {
            localNotificationPrefs.edit()
                .putLong(key, System.currentTimeMillis())
                .apply()
        }
    }

    fun pushNotificationOnce(
        markerKey: String,
        title: String,
        message: String,
        type: NotificationType,
    ) {
        if (localNotificationPrefs.getBoolean(markerKey, false)) return

        localNotificationPrefs.edit()
            .putBoolean(markerKey, true)
            .apply()

        pushNotification(
            title = title,
            message = message,
            type = type,
        )
    }

    suspend fun syncLocalTicketNotifications() {
        val bookings = try {
            bookingViewModel.getLocalBookingsSnapshot()
        } catch (exception: Exception) {
            emptyList()
        }

        val now = System.currentTimeMillis()

        bookings.forEach { booking ->
            if (booking.status.equals("Aktif", ignoreCase = true)) {
                val daysUntilDeparture = booking.daysUntilDeparture()

                if (daysUntilDeparture in listOf(3L, 1L, 0L)) {
                    val reminderLabel = when (daysUntilDeparture) {
                        3L -> "H-3"
                        1L -> "H-1"
                        else -> "Hari Ini"
                    }

                    pushNotificationOnce(
                        markerKey = "departure:${booking.id}:$daysUntilDeparture",
                        title = "Pengingat Keberangkatan $reminderLabel",
                        message = "${booking.shipName} berangkat ${booking.departureDate}, ${booking.departureTime} dari ${booking.origin} ke ${booking.destination}.",
                        type = NotificationType.SCHEDULE,
                    )
                }
            }

            val processStatus = when {
                booking.status.equals("Refund Diproses", ignoreCase = true) -> {
                    "Refund Berhasil"
                }

                booking.status.equals("Reschedule Diproses", ignoreCase = true) -> {
                    "Reschedule Berhasil"
                }

                else -> null
            }

            if (processStatus != null) {
                val startedKey = booking.id.toManageProcessStartKey(booking.status)
                val startedAt = localNotificationPrefs.getLong(startedKey, 0L)

                if (startedAt == 0L) {
                    localNotificationPrefs.edit()
                        .putLong(startedKey, now)
                        .apply()
                } else if (now - startedAt >= MANAGE_TICKET_PROCESS_DURATION_MILLIS) {
                    val updatedBooking = bookingViewModel.updateBookingStatusForSimulation(
                        bookingId = booking.id,
                        status = processStatus,
                    )

                    pushNotificationOnce(
                        markerKey = "manage-complete:${updatedBooking.id}:$processStatus",
                        title = processStatus,
                        message = "Status tiket ${updatedBooking.id} berubah menjadi $processStatus.",
                        type = if (processStatus.contains("Refund", ignoreCase = true)) {
                            NotificationType.REFUND
                        } else {
                            NotificationType.RESCHEDULE
                        },
                    )
                }
            }
        }
    }

    LaunchedEffect(isLoggedIn, currentRoute) {
        if (isLoggedIn) {
            syncLocalTicketNotifications()
            homeTicketOverview = try {
                bookingViewModel.getLocalBookingsSnapshot().nearestActiveBooking()
            } catch (exception: Exception) {
                null
            }
        } else {
            homeTicketOverview = null
        }
    }

    val startDestination = remember {
        if (initialHasSeenOnboarding) {
            Screens.Home
        } else {
            Screens.Onboarding
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (showTopBar) {
                if (showBackTopBar) {
                    AppBackTopBar(
                        title = getTopBarTitle(currentRoute.orEmpty(), strings),
                        onBackClick = {
                            navController.popBackStack()
                        },
                        actionIcon = if (
                            currentRoute == Screens.ETicket ||
                            currentRoute == Screens.ETicketByPayment
                        ) {
                            Icons.Outlined.FileDownload
                        } else {
                            null
                        },
                        actionDescription = "Download E-Ticket",
                        onActionClick = {
                            eTicketDownloadRequest++
                        },
                    )
                } else {
                    AppTopBar(
                        title = getTopBarTitle(currentRoute.orEmpty(), strings)
                    )
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomNavigationBar(
                    currentRoute = currentRoute.orEmpty(),
                    unreadNotificationCount = unreadNotificationCount,
                    onItemClick = { item ->
                        if (item.route in bottomBarRoutes && item.route != currentRoute) {
                            resetSearchInput()
                        }

                        if (!isLoggedIn && item.route in protectedRoutes) {
                            pendingProtectedRoute = null

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
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
//            composable(Screens.Splash) {
//                SplashScreen(
//                    onSplashFinished = {
//                        val nextRoute = if (hasSeenOnboarding) {
//                            Screens.Home
//                        } else {
//                            Screens.Onboarding
//                        }
//
//                        navController.navigate(nextRoute) {
//                            popUpTo(Screens.Splash) {
//                                inclusive = true
//                            }
//                        }
//                    }
//                )
//            }

            composable(Screens.Onboarding) {
                OnboardingScreen(
                    onFinishClick = {
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
                val registerSuccessMessage = navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("register_success_message")

                LaunchedEffect(registerSuccessMessage) {
                    if (!registerSuccessMessage.isNullOrBlank()) {
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.remove<String>("register_success_message")
                    }
                }

                LoginScreen(
                    registerSuccessMessage = registerSuccessMessage,
                    onLoginClick = { email, password, showError ->
                        authViewModel.loginWithEmail(
                            email = email,
                            password = password,
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Berhasil masuk. Selamat datang!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navigateAfterAuthSuccess()
                            },
                            onError = { message: String ->
                                showError(message)
                            }
                        )
                    },
                    onGoogleLoginClick = { showError ->
                        scope.launch {
                            try {
                                val idToken = try {
                                    getGoogleIdToken(
                                        filterByAuthorizedAccounts = true,
                                    )
                                } catch (exception: NoCredentialException) {
                                    getGoogleIdToken(
                                        filterByAuthorizedAccounts = false,
                                    )
                                }

                                authViewModel.loginWithGoogle(
                                    idToken = idToken,
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Berhasil masuk dengan Google.",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        navigateAfterAuthSuccess()
                                    },
                                    onError = { message ->
                                        showError(message)
                                    },
                                )
                            } catch (exception: GetCredentialException) {
                                showError("Login Google dibatalkan atau gagal. Silakan coba lagi.")
                            } catch (exception: Exception) {
                                showError(
                                    exception.message ?: "Login Google gagal. Silakan coba lagi."
                                )
                            }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(Screens.Register)
                    },
                    onContinueAsGuestClick = {
                        navController.navigate(Screens.Home)
                    }
                )
            }

            composable(Screens.Register) {
                RegisterScreen(
                    onRegisterClick = { name, email, password, showError ->
                        authViewModel.registerWithEmail(
                            name = name,
                            email = email,
                            password = password,
                            onSuccess = {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        "register_success_message",
                                        "Akun berhasil terdaftar. Silakan login untuk masuk."
                                    )

                                navController.popBackStack()
                            },
                            onError = { message ->
                                showError(message)
                            }
                        )
                    },
                    onLoginClick = {
                        navController.popBackStack()
                    }
                )
            }

            composable(Screens.AuthRequired) {
                AuthRequiredScreen(
                    onLoginClick = {
                        navigateToLoginFromGuest()
                    },
                    onBackClick = {
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
                    },
                    ticketOverview = homeTicketOverview,
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

                AuthGate(
                    onLoginClick = {
                        pendingProtectedRoute = Screens.selectTicket(scheduleId)
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
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
                                    passengerCount = passengerCount,
                                )
                            )
                        }
                    )
                }
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    PassengerFormScreen(
                        scheduleId = scheduleId,
                        ticketClassId = ticketClassId,
                        ticketPrice = ticketPrice,
                        passengerCount = passengerCount,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onBookingCreated = { bookingId ->
                            pushNotification(
                                title = "Booking Berhasil Dibuat",
                                message = "Pesanan tiket berhasil dibuat. Silakan periksa ringkasan pesanan sebelum melanjutkan pembayaran.",
                                type = NotificationType.INFO,
                            )

                            navController.navigate(Screens.bookingSummary(bookingId))
                        }
                    )
                }
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    PaymentScreen(
                        bookingId = bookingId,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onPaymentCreated = { paymentId ->
                            pushNotification(
                                title = "Menunggu Pembayaran",
                                message = "Pesanan kamu berhasil dibuat. Selesaikan pembayaran sebelum batas waktu berakhir.",
                                type = NotificationType.PAYMENT,
                            )

                            navController.navigate(Screens.paymentWaiting(paymentId))
                        }
                    )
                }
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    PaymentWaitingScreen(
                        paymentId = paymentId,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onPaymentSuccess = { selectedPaymentId ->
                            pushNotification(
                                title = "Pembayaran Berhasil",
                                message = "Pembayaran tiket berhasil dikonfirmasi.",
                                type = NotificationType.PAYMENT,
                            )

                            pushNotification(
                                title = "E-Ticket Tersedia",
                                message = "E-ticket kamu sudah tersedia dan dapat dilihat pada halaman Tiket Saya.",
                                type = NotificationType.TICKET,
                            )

                            navController.navigate(Screens.paymentSuccess(selectedPaymentId))
                        },
                        onPaymentFailed = { selectedPaymentId ->
                            pushNotification(
                                title = "Pembayaran Gagal",
                                message = "Pembayaran belum berhasil. Silakan coba kembali atau gunakan metode pembayaran lain.",
                                type = NotificationType.PAYMENT,
                            )

                            navController.navigate(Screens.paymentFailed(selectedPaymentId))
                        }
                    )
                }
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.navigate(Screens.Home)
                    }
                ) {
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.navigate(Screens.Home)
                    }
                ) {
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
            }

            composable(Screens.MyTicket) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    }
                ) {
                    MyTicketScreen(
                        onTicketClick = { bookingId ->
                            navController.navigate(Screens.eTicket(bookingId))
                        },
                        onPayNowClick = { bookingId ->
                            navController.navigate(Screens.payment(bookingId))
                        }
                    )
                }
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ETicketScreen(
                        bookingId = bookingId,
                        downloadRequest = eTicketDownloadRequest,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onRefundClick = { selectedBookingId ->
                            navController.navigate(Screens.refund(selectedBookingId))
                        },
                        onRescheduleClick = { selectedBookingId ->
                            navController.navigate(Screens.reschedule(selectedBookingId))
                        }
                    )
                }
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

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ETicketScreen(
                        paymentId = paymentId,
                        downloadRequest = eTicketDownloadRequest,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onRefundClick = { selectedBookingId ->
                            navController.navigate(Screens.refund(selectedBookingId))
                        },
                        onRescheduleClick = { selectedBookingId ->
                            navController.navigate(Screens.reschedule(selectedBookingId))
                        }
                    )
                }
            }

            composable(
                route = Screens.Refund,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    RefundScreen(
                        bookingId = bookingId,
                        onSubmitClick = { selectedBookingId ->
                            bookingViewModel.submitRefund(
                                bookingId = selectedBookingId,
                                onSuccess = {
                                    markManageProcessStarted(
                                        bookingId = selectedBookingId,
                                        status = "Refund Diproses",
                                    )

                                    pushNotification(
                                        title = "Refund Diproses",
                                        message = "Pengajuan refund tiket berhasil dikirim dan sedang diproses.",
                                        type = NotificationType.REFUND,
                                    )

                                    navController.navigate(
                                        Screens.refundSuccess(selectedBookingId)
                                    )
                                },
                                onError = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                },
                            )
                        }
                    )
                }
            }

            composable(
                route = Screens.Reschedule,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    RescheduleScreen(
                        bookingId = bookingId,
                        onSubmitClick = { selectedBookingId ->
                            bookingViewModel.submitReschedule(
                                bookingId = selectedBookingId,
                                onSuccess = {
                                    markManageProcessStarted(
                                        bookingId = selectedBookingId,
                                        status = "Reschedule Diproses",
                                    )

                                    pushNotification(
                                        title = "Reschedule Diproses",
                                        message = "Pengajuan reschedule tiket berhasil dikirim dan sedang diproses.",
                                        type = NotificationType.RESCHEDULE,
                                    )

                                    navController.navigate(
                                        Screens.rescheduleSuccess(selectedBookingId)
                                    )
                                },
                                onError = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                },
                            )
                        }
                    )
                }
            }

            composable(
                route = Screens.RefundSuccess,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.navigate(Screens.MyTicket)
                    }
                ) {
                    ManageTicketSuccessScreen(
                        title = "Refund Diproses",
                        description = "Pengajuan refund berhasil dikirim. Status refund dapat dilihat pada halaman Tiket Saya.",
                        onContinueClick = {
                            navController.navigate(Screens.MyTicket)
                        }
                    )
                }
            }

            composable(
                route = Screens.RescheduleSuccess,
                arguments = listOf(
                    navArgument("bookingId") {
                        type = NavType.StringType
                    }
                )
            ) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.navigate(Screens.MyTicket)
                    }
                ) {
                    ManageTicketSuccessScreen(
                        title = "Reschedule Berhasil",
                        description = "Pengajuan reschedule berhasil dikirim. Tiket baru akan tersedia setelah proses dikonfirmasi.",
                        onContinueClick = {
                            navController.navigate(Screens.MyTicket)
                        }
                    )
                }
            }

            composable(Screens.Notification) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    NotificationScreen(
                        onNotificationClick = { notificationId ->
                            navController.navigate(
                                Screens.notificationDetail(notificationId)
                            )
                        },
                    )
                }
            }

            composable(
                route = Screens.NotificationDetail,
                arguments = listOf(
                    navArgument("notificationId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val notificationId = backStackEntry.arguments
                    ?.getString("notificationId")
                    .orEmpty()

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    NotificationDetailScreen(
                        notificationId = notificationId,
                    )
                }
            }

            composable(Screens.Profile) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    }
                ) {
                    ProfileScreen(
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
                        onSecurityClick = {
                            navController.navigate(Screens.ProfileSecurity)
                        },
                        onLogoutSuccess = {
                            Toast.makeText(
                                context,
                                "Akun berhasil logout.",
                                Toast.LENGTH_SHORT
                            ).show()

                            navController.navigate(Screens.Login) {
                                popUpTo(Screens.Home) {
                                    inclusive = true
                                }

                                launchSingleTop = true
                            }
                        },
                    )
                }
            }

            composable(Screens.ProfileEdit) {
                val user = (authUiState as? AuthUiState.Authenticated)?.user

                EditProfileScreen(
                    initialName = user?.name.orEmpty(),
                    initialEmail = user?.email.orEmpty(),
                    initialPhotoUrl = user?.photoUrl.orEmpty(),
                    onSaveClick = {
                        navController.popBackStack()
                    }
                )
            }



            composable(Screens.ProfilePassengerData) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    PassengerDataScreen(
                        onAddPassengerClick = {
                            navController.navigate(Screens.profilePassengerForm())
                        },
                        onEditPassengerClick = { passengerId ->
                            navController.navigate(Screens.profilePassengerForm(passengerId))
                        },
                    )
                }
            }

            composable(
                route = Screens.ProfilePassengerForm,
                arguments = listOf(
                    navArgument("passengerId") {
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val passengerId = backStackEntry.arguments?.getString("passengerId")

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    PassengerProfileFormScreen(
                        passengerId = passengerId,
                        onSaveClick = {
                            navController.popBackStack()
                        },
                    )
                }
            }

            composable(Screens.ProfileSettings) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    SettingsScreen(
                        onLanguageClick = {
                            navController.navigate(Screens.ProfileLanguage)
                        },
                        onThemeClick = {
                            navController.navigate(Screens.ProfileTheme)
                        },
                        onTextSizeClick = {
                            navController.navigate(Screens.ProfileTextSize)
                        },
                        onAboutClick = {
                            navController.navigate(Screens.ProfileAbout)
                        },
                        onHelpClick = {
                            navController.navigate(Screens.ProfileHelp)
                        },
                        onSecurityClick = {
                            navController.navigate(Screens.ProfileSecurity)
                        },
                    )
                }
            }

            composable(Screens.ProfileLanguage) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ProfileLanguageScreen()
                }
            }

            composable(Screens.ProfileTheme) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ProfileThemeScreen()
                }
            }

            composable(Screens.ProfileTextSize) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ProfileTextSizeScreen()
                }
            }

            composable(Screens.ProfileHelp) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ProfileHelpScreen(
                        onHelpDetailClick = { type ->
                            navController.navigate(Screens.profileHelpDetail(type))
                        },
                    )
                }
            }

            composable(
                route = Screens.ProfileHelpDetail,
                arguments = listOf(
                    navArgument("type") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val type = backStackEntry.arguments?.getString("type").orEmpty()

                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ProfileHelpDetailScreen(
                        type = type,
                    )
                }
            }

            composable(Screens.ProfileAbout) {
                AboutAppScreen()
            }

            composable(Screens.ProfileSecurity) {
                AuthGate(
                    onLoginClick = {
                        navController.navigate(Screens.Login)
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                ) {
                    ProfileSecurityScreen()
                }
            }
        }
    }
}

private const val MANAGE_TICKET_PROCESS_DURATION_MILLIS = 24L * 60L * 60L * 1000L

private fun String.toManageProcessStartKey(
    status: String,
): String {
    return "manage-start:${this}:${status.lowercase().replace(" ", "-")}"
}

private fun Booking.daysUntilDeparture(): Long? {
    val departureMillis = departureDate.toStartOfDayMillis() ?: return null
    val todayMillis = Calendar.getInstance().startOfDayMillis()

    return TimeUnit.MILLISECONDS.toDays(departureMillis - todayMillis)
}

private fun List<Booking>.nearestActiveBooking(): Booking? {
    val activeBookings = filter { booking ->
        booking.status.equals("Aktif", ignoreCase = true)
    }

    return activeBookings
        .map { booking ->
            booking to (booking.daysUntilDeparture() ?: Long.MAX_VALUE)
        }
        .filter { (_, daysUntilDeparture) ->
            daysUntilDeparture >= 0L
        }
        .minByOrNull { (_, daysUntilDeparture) ->
            daysUntilDeparture
        }
        ?.first
        ?: activeBookings.firstOrNull()
}

private fun String.toStartOfDayMillis(): Long? {
    val formats = listOf(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
        SimpleDateFormat("dd MMM yyyy", Locale.forLanguageTag("id-ID")),
        SimpleDateFormat("dd MMMM yyyy", Locale.forLanguageTag("id-ID")),
    )

    formats.forEach { formatter ->
        try {
            formatter.isLenient = false
            val date = formatter.parse(this)

            if (date != null) {
                return Calendar.getInstance().apply {
                    time = date
                }.startOfDayMillis()
            }
        } catch (exception: Exception) {
            // Try the next supported date format.
        }
    }

    return null
}

private fun Calendar.startOfDayMillis(): Long {
    return apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun getTopBarTitle(
    route: String,
    strings: AppStrings,
): String {
    return when (route) {
        Screens.AuthRequired -> strings.titleLoginRequired
        Screens.Home -> strings.navHome
        Screens.Schedule -> strings.navSchedule
        Screens.ScheduleDetail -> strings.titleScheduleDetail
        Screens.SelectTicket -> strings.titleSelectTicket
        Screens.PassengerForm -> strings.titlePassengerForm
        Screens.BookingSummary -> strings.titleBookingSummary
        Screens.Payment -> strings.titlePayment
        Screens.PaymentWaiting -> strings.titlePaymentWaiting
        Screens.PaymentFailed -> strings.titlePaymentStatus
        Screens.PaymentSuccess -> strings.titlePaymentStatus
        Screens.MyTicket -> strings.navMyTicket
        Screens.ETicket -> strings.titleETicket
        Screens.ETicketByPayment -> strings.titleETicket
        Screens.Refund -> strings.titleRefund
        Screens.Reschedule -> strings.titleReschedule
        Screens.RefundSuccess -> strings.titleRefundProcess
        Screens.RescheduleSuccess -> strings.titleRescheduleSuccess
        Screens.Notification -> strings.navNotification
        Screens.NotificationDetail -> strings.titleNotificationDetail
        Screens.Profile -> strings.navProfile
        Screens.PortSearchRoute -> strings.titleChoosePort
        Screens.SearchResult -> strings.titleSearchResult
        Screens.PopularRouteResult -> strings.titlePopularRoute
        Screens.ProfileEdit -> strings.editProfile
        Screens.ProfilePassengerData -> strings.passengerData
        Screens.ProfilePassengerForm -> strings.titleAddPassenger
        Screens.ProfileSettings -> strings.settings
        Screens.ProfileLanguage -> strings.language
        Screens.ProfileTheme -> strings.theme
        Screens.ProfileTextSize -> strings.textSize
        Screens.ProfileHelp -> strings.help
        Screens.ProfileHelpDetail -> strings.helpTitle
        Screens.ProfileAbout -> strings.aboutApp
        Screens.ProfileSecurity -> strings.security
        else -> ""
    }
}
