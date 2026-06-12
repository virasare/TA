package com.dicoding.tugas_akhir.ui.navigation

object Screens {
    const val AuthRequired = "auth_required"
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val Register = "register"

    const val Home = "home"
    const val SearchResult = "search_result"
    const val PortSearchRoute = "port_search/{type}"
    const val PopularRouteResult = "popular_route_result/{routeId}"
    fun portSearch(type: String): String {
        return "port_search/$type"
    }
    fun popularRouteResult(routeId: Int): String {
        return "popular_route_result/$routeId"
    }

    const val Schedule = "schedule"
    const val ScheduleDetail = "schedule_detail/{scheduleId}"
    fun scheduleDetail(scheduleId: Int): String {
        return "schedule_detail/$scheduleId"
    }

    const val MyTicket = "my_ticket"
    const val SelectTicket = "select_ticket"
    const val PassengerForm = "passenger_form"
    const val BookingSummary = "booking_summary"
    const val Payment = "payment"
    const val PaymentWaiting = "payment_waiting"
    const val PaymentSuccess = "payment_success"
    const val PaymentFailed = "payment_failed"
    const val OrderDetail = "order_detail"
    const val ETicket = "e_ticket"


    const val Notification = "notification"
    const val NotificationDetail = "notification_detail/{notificationId}"
    fun notificationDetail(notificationId: Int): String {
        return "notification_detail/$notificationId"
    }

    const val Profile = "profile"
    const val ProfileEdit = "profile_edit"
    const val ProfilePassengerData = "profile_passenger_data"
    const val ProfilePassengerForm = "profile_passenger_form"
    const val ProfileSettings = "profile_settings"
    const val ProfileLanguage = "profile_language"
    const val ProfileTheme = "profile_theme"
    const val ProfileHelp = "profile_help"
    const val ProfileHelpDetail = "profile_help_detail/{helpId}"
    const val ProfileAbout = "profile_about"

    fun profileHelpDetail(helpId: Int): String {
        return "profile_help_detail/$helpId"
    }
}