package com.dicoding.tugas_akhir.data.dummy

import java.util.Calendar

fun List<ShipSchedule>.filterUpcomingSchedules(
    nowMillis: Long = System.currentTimeMillis(),
): List<ShipSchedule> {
    return filter { schedule ->
        schedule.isUpcomingSchedule(nowMillis)
    }
}

fun ShipSchedule.isUpcomingSchedule(
    nowMillis: Long = System.currentTimeMillis(),
): Boolean {
    val departureMillis = toDepartureMillis() ?: return true
    return departureMillis >= nowMillis
}

private fun ShipSchedule.toDepartureMillis(): Long? {
    val dateParts = departureDate.toDateParts() ?: return null
    val timeParts = departureTime.toTimeParts()

    return Calendar.getInstance().apply {
        set(Calendar.YEAR, dateParts.year)
        set(Calendar.MONTH, dateParts.monthIndex)
        set(Calendar.DAY_OF_MONTH, dateParts.day)
        set(Calendar.HOUR_OF_DAY, timeParts.hour)
        set(Calendar.MINUTE, timeParts.minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private data class DateParts(
    val day: Int,
    val monthIndex: Int,
    val year: Int,
)

private data class TimeParts(
    val hour: Int,
    val minute: Int,
)

private fun String.toDateParts(): DateParts? {
    val trimmed = trim()
    val isoParts = trimmed.split("-")
    if (isoParts.size == 3) {
        val year = isoParts[0].toIntOrNull()
        val month = isoParts[1].toIntOrNull()
        val day = isoParts[2].toIntOrNull()

        if (year != null && month != null && day != null) {
            return DateParts(
                day = day,
                monthIndex = month - 1,
                year = year,
            )
        }
    }

    val parts = trimmed.split(" ")
    if (parts.size != 3) return null

    val monthMap = mapOf(
        "Jan" to Calendar.JANUARY,
        "Feb" to Calendar.FEBRUARY,
        "Mar" to Calendar.MARCH,
        "Apr" to Calendar.APRIL,
        "Mei" to Calendar.MAY,
        "May" to Calendar.MAY,
        "Jun" to Calendar.JUNE,
        "Jul" to Calendar.JULY,
        "Agu" to Calendar.AUGUST,
        "Aug" to Calendar.AUGUST,
        "Sep" to Calendar.SEPTEMBER,
        "Okt" to Calendar.OCTOBER,
        "Oct" to Calendar.OCTOBER,
        "Nov" to Calendar.NOVEMBER,
        "Des" to Calendar.DECEMBER,
        "Dec" to Calendar.DECEMBER,
    )

    val day = parts[0].toIntOrNull() ?: return null
    val monthIndex = monthMap[parts[1]] ?: return null
    val year = parts[2].toIntOrNull() ?: return null

    return DateParts(
        day = day,
        monthIndex = monthIndex,
        year = year,
    )
}

private fun String.toTimeParts(): TimeParts {
    val timeText = trim()
        .substringBefore(" ")
        .replace(".", ":")

    val parts = timeText.split(":")
    val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
    val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

    return TimeParts(
        hour = hour,
        minute = minute,
    )
}
