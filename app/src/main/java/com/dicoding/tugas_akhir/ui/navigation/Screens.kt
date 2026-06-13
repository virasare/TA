package com.dicoding.tugas_akhir.ui.navigation

object Screens {
//    splash and onboarding
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
    fun scheduleDetail(scheduleId: String): String {
        return "schedule_detail/$scheduleId"
    }

//    Booking
    const val SelectTicket = "select_ticket/{scheduleId}"
    const val BookingSummary = "booking_summary/{bookingId}"

    fun selectTicket(scheduleId: String): String {
        return "select_ticket/$scheduleId"
    }

    const val PassengerForm = "passenger_form/{scheduleId}/{ticketClassId}/{ticketPrice}/{passengerCount}"

    fun passengerForm(
        scheduleId: String,
        ticketClassId: String,
        ticketPrice: Int,
        passengerCount: Int
    ): String {
        return "passenger_form/$scheduleId/$ticketClassId/$ticketPrice/$passengerCount"
    }

    fun bookingSummary(bookingId: String): String {
        return "booking_summary/$bookingId"
    }

//   Payment
    const val Payment = "payment/{bookingId}"
    const val OrderDetail = "order_detail"
    const val PaymentWaiting = "payment_waiting/{paymentId}"
    const val PaymentSuccess = "payment_success/{paymentId}"
    const val PaymentFailed = "payment_failed/{paymentId}"

    fun payment(bookingId: String): String {
        return "payment/$bookingId"
    }

    fun paymentWaiting(paymentId: String): String {
        return "payment_waiting/$paymentId"
    }

    fun paymentSuccess(paymentId: String): String {
        return "payment_success/$paymentId"
    }

    fun paymentFailed(paymentId: String): String {
        return "payment_failed/$paymentId"
    }

//  MyTicket & E-Ticket
    const val MyTicket = "my_ticket"
    const val ETicket = "e_ticket/{bookingId}"
    const val ETicketByPayment = "e_ticket_payment/{paymentId}"

    fun eTicket(bookingId: String): String {
        return "e_ticket/$bookingId"
    }

    fun eTicketByPayment(paymentId: String): String {
        return "e_ticket_payment/$paymentId"
    }

//  Notification
    const val Notification = "notification"
    const val NotificationDetail = "notification_detail/{notificationId}"
    fun notificationDetail(notificationId: Int): String {
        return "notification_detail/$notificationId"
    }
//  Profile
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