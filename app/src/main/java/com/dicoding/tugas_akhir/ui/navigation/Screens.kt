package com.dicoding.tugas_akhir.ui.navigation

object Screens {
    const val Splash = "splash"
    const val Onboarding = "onboarding"
    const val Login = "login"
    const val Register = "register"
    const val AuthRequired = "auth_required"

    const val Home = "home"
    const val Schedule = "schedule"
    const val MyTicket = "my_ticket"
    const val Notification = "notification"
    const val Profile = "profile"

    const val PortSearchRoute = "port_search/{type}"
    fun portSearch(type: String): String {
        return "port_search/$type"
    }

    const val SearchResult = "search_result"

    const val PopularRouteResult = "popular_route_result/{routeId}"
    fun popularRouteResult(routeId: Int): String {
        return "popular_route_result/$routeId"
    }

    const val ScheduleDetail = "schedule_detail/{scheduleId}"
    fun scheduleDetail(scheduleId: String): String {
        return "schedule_detail/$scheduleId"
    }

    const val SelectTicket = "select_ticket/{scheduleId}"
    fun selectTicket(scheduleId: String): String {
        return "select_ticket/$scheduleId"
    }

    const val PassengerForm =
        "passenger_form/{scheduleId}/{ticketClassId}/{ticketPrice}/{passengerCount}"

    fun passengerForm(
        scheduleId: String,
        ticketClassId: String,
        ticketPrice: Int,
        passengerCount: Int,
    ): String {
        return "passenger_form/$scheduleId/$ticketClassId/$ticketPrice/$passengerCount"
    }

    const val BookingSummary = "booking_summary/{bookingId}"
    fun bookingSummary(bookingId: String): String {
        return "booking_summary/$bookingId"
    }

    const val Payment = "payment/{bookingId}"
    fun payment(bookingId: String): String {
        return "payment/$bookingId"
    }

    const val PaymentWaiting = "payment_waiting/{paymentId}"
    fun paymentWaiting(paymentId: String): String {
        return "payment_waiting/$paymentId"
    }

    const val PaymentSuccess = "payment_success/{paymentId}"
    fun paymentSuccess(paymentId: String): String {
        return "payment_success/$paymentId"
    }

    const val PaymentFailed = "payment_failed/{paymentId}"
    fun paymentFailed(paymentId: String): String {
        return "payment_failed/$paymentId"
    }

    const val ETicket = "e_ticket/{bookingId}"
    fun eTicket(bookingId: String): String {
        return "e_ticket/$bookingId"
    }

    const val ETicketByPayment = "e_ticket_by_payment/{paymentId}"
    fun eTicketByPayment(paymentId: String): String {
        return "e_ticket_by_payment/$paymentId"
    }

    const val Refund = "refund/{bookingId}"
    const val Reschedule = "reschedule/{bookingId}"
    const val RefundSuccess = "refund_success/{bookingId}"
    const val RescheduleSuccess = "reschedule_success/{bookingId}"

    fun refund(bookingId: String): String {
        return "refund/$bookingId"
    }

    fun reschedule(bookingId: String): String {
        return "reschedule/$bookingId"
    }

    fun refundSuccess(bookingId: String): String {
        return "refund_success/$bookingId"
    }

    fun rescheduleSuccess(bookingId: String): String {
        return "reschedule_success/$bookingId"
    }

    const val NotificationDetail = "notification_detail/{notificationId}"
    fun notificationDetail(notificationId: String): String {
        return "notification_detail/$notificationId"
    }

    const val ProfileEdit = "profile_edit"
    const val ProfilePassengerData = "profile_passenger_data"

    const val ProfilePassengerForm = "profile_passenger_form?passengerId={passengerId}"
    fun profilePassengerForm(passengerId: String? = null): String {
        return if (passengerId.isNullOrBlank()) {
            "profile_passenger_form"
        } else {
            "profile_passenger_form?passengerId=$passengerId"
        }
    }

    const val ProfileSettings = "profile_settings"
    const val ProfileLanguage = "profile_language"
    const val ProfileTheme = "profile_theme"
    const val ProfileTextSize = "profile_text_size"
    const val ProfileHelp = "profile_help"

    const val ProfileHelpDetail = "profile_help_detail/{type}"
    fun profileHelpDetail(type: String): String {
        return "profile_help_detail/$type"
    }

    const val ProfileAbout = "profile_about"
    const val ProfileSecurity = "profile_security"
}
