package com.dicoding.tugas_akhir.ui.navigation

object Screens {
    const val Home = "home"
    const val Schedule = "schedule"
    const val ScheduleDetail = "schedule_detail"
    const val PassengerForm = "passenger_form"
    const val BookingSummary = "booking_summary"
    const val Payment = "payment"
    const val PaymentSuccess = "payment_success"
    const val MyTicket = "my_ticket"
    const val ETicket = "e_ticket"
    const val Notification = "notification"
    const val Profile = "profile"

    const val SearchResult = "search_result"

    const val PortSearchRoute = "port_search/{type}"

    fun portSearch(type: String): String {
        return "port_search/$type"
    }

    const val PopularRouteResult = "popular_route_result/{routeId}"

    fun popularRouteResult(routeId: Int): String {
        return "popular_route_result/$routeId"
    }
}