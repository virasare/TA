package com.dicoding.tugas_akhir.data.recomendation

import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.abs

fun findExactSchedules(
    schedules: List<ShipSchedule>,
    originPort: Port?,
    destinationPort: Port?,
    selectedDate: String
): List<ShipSchedule> {
    if (originPort == null || destinationPort == null || selectedDate.isEmpty()) {
        return emptyList()
    }

    return schedules
        .filter { schedule ->
            val route = schedule.toRouteDirection()

            route.origin.equals(originPort.city, ignoreCase = true) &&
                    route.destination.equals(destinationPort.city, ignoreCase = true) &&
                    schedule.departureDate == selectedDate
        }
        .sortedWith(
            scheduleRecommendationComparator(
                originPort = originPort,
                destinationPort = destinationPort,
                selectedDate = selectedDate
            )
        )
}

fun findRecommendedSchedules(
    schedules: List<ShipSchedule>,
    originPort: Port?,
    destinationPort: Port?,
    selectedDate: String
): List<ShipSchedule> {
    if (originPort == null || destinationPort == null || selectedDate.isEmpty()) {
        return emptyList()
    }

    return schedules
        .filter { schedule ->
            val route = schedule.toRouteDirection()

            // Ini penting:
            // hanya ambil jadwal yang TUJUANNYA sama dengan tujuan yang dicari user.
            route.destination.equals(destinationPort.city, ignoreCase = true)
        }
        .sortedWith(
            scheduleRecommendationComparator(
                originPort = originPort,
                destinationPort = destinationPort,
                selectedDate = selectedDate
            )
        )
}

private fun scheduleRecommendationComparator(
    originPort: Port,
    destinationPort: Port,
    selectedDate: String
): Comparator<ShipSchedule> {
    return compareBy<ShipSchedule> { schedule ->
        // Prioritas 1:
        // Tersedia → Terbatas → Habis
        schedule.status.toAvailabilityRank()
    }.thenBy { schedule ->
        // Prioritas 2:
        // Asal + tujuan sama dulu.
        // Setelah itu baru asal lain dengan tujuan yang sama.
        val route = schedule.toRouteDirection()

        val isExactRoute =
            route.origin.equals(originPort.city, ignoreCase = true) &&
                    route.destination.equals(destinationPort.city, ignoreCase = true)

        if (isExactRoute) 0 else 1
    }.thenBy { schedule ->
        // Prioritas 3:
        // Tanggal paling dekat dari tanggal yang dipilih user.
        schedule.departureDate.dateDistanceFrom(selectedDate)
    }.thenBy { schedule ->
        // Prioritas 4:
        // Kalau tanggalnya sama-sama dekat, urutkan jam berangkat.
        schedule.departureTime.toTimeRank()
    }
}

private data class RouteDirection(
    val origin: String,
    val destination: String
)

private fun ShipSchedule.toRouteDirection(): RouteDirection {
    val parts = route.split("→")

    val origin = parts.getOrNull(0)?.trim().orEmpty()
    val destination = parts.getOrNull(1)?.trim().orEmpty()

    return RouteDirection(
        origin = origin,
        destination = destination
    )
}

private fun ShipScheduleStatus.toAvailabilityRank(): Int {
    return when (this) {
        ShipScheduleStatus.Available -> 0
        ShipScheduleStatus.Limited -> 1
        ShipScheduleStatus.Unavailable -> 2
    }
}

private fun String.dateDistanceFrom(selectedDate: String): Long {
    val scheduleDateMillis = this.toDateMillis()
    val selectedDateMillis = selectedDate.toDateMillis()

    if (scheduleDateMillis == null || selectedDateMillis == null) {
        return Long.MAX_VALUE
    }

    return abs(scheduleDateMillis - selectedDateMillis)
}

private fun String.toDateMillis(): Long? {
    return try {
        val formatter = SimpleDateFormat(
            "dd MMM yyyy",
            Locale.forLanguageTag("id-ID")
        )

        formatter.parse(this)?.time
    } catch (e: Exception) {
        null
    }
}

private fun String.toTimeRank(): Int {
    return try {
        val cleanTime = this
            .replace("WITA", "")
            .replace("WIB", "")
            .replace("WIT", "")
            .trim()

        val parts = cleanTime.split(".")

        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        hour * 60 + minute
    } catch (e: Exception) {
        Int.MAX_VALUE
    }
}