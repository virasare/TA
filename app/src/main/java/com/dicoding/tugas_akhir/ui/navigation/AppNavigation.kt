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
import com.dicoding.tugas_akhir.ui.screens.splash.SplashScreen
import com.dicoding.tugas_akhir.ui.theme.Background
import com.google.firebase.auth.FirebaseAuth
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileLanguageScreen
import com.dicoding.tugas_akhir.ui.screens.profile.ProfileSecurityScreen
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
import kotlinx.coroutines.launch

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
    val factory = ViewModelFactory.getInstance()

    val webClientId = stringResource(
        id = R.string.default_web_client_id
    )

    val authViewModel: AuthViewModel = viewModel(
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

    var hasSeenOnboarding by remember {
        mutableStateOf(prefs.getBoolean("has_seen_onboarding", false))
    }

    var isSplashFinished by remember {
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

    val showBottomBar = isSplashFinished &&
            currentRoute != null &&
            currentRoute in bottomBarRoutes

    val showTopBar = currentRoute != null &&
            currentRoute !in hideTopBarRoutes

    val showBackTopBar = currentRoute != null &&
            currentRoute !in bottomBarRoutes &&
            currentRoute !in hideTopBarRoutes

    fun navigateAfterAuthSuccess() {
        pendingProtectedRoute = null

        navController.navigate(Screens.Home) {
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
                    unreadNotificationCount = unreadNotificationCount,
                    onItemClick = { item ->
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

                        isSplashFinished = true

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

                AuthGate(
                    onLoginClick = {
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
                            pushNotification(
                                title = "Refund Diproses",
                                message = "Pengajuan refund tiket berhasil dikirim dan sedang diproses.",
                                type = NotificationType.INFO,
                            )

                            navController.navigate(
                                Screens.refundSuccess(selectedBookingId)
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
                            pushNotification(
                                title = "Reschedule Berhasil Diajukan",
                                message = "Pengajuan reschedule tiket berhasil dikirim dan sedang diproses.",
                                type = NotificationType.INFO,
                            )

                            navController.navigate(
                                Screens.rescheduleSuccess(selectedBookingId)
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
                        description = "Pengajuan refund berhasil dikirim. Status refund dapat dilihat pada halaman Pesanan Saya.",
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
        Screens.Refund -> "Ajukan Refund"
        Screens.Reschedule -> "Reschedule Tiket"
        Screens.RefundSuccess -> "Refund Diproses"
        Screens.RescheduleSuccess -> "Reschedule Berhasil"
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