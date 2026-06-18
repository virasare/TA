package com.dicoding.tugas_akhir.data.recomendation

import com.dicoding.tugas_akhir.data.dummy.Port
import com.dicoding.tugas_akhir.data.dummy.ShipSchedule
import com.dicoding.tugas_akhir.ui.components.cards.ShipScheduleStatus
import kotlin.math.sqrt

enum class ScheduleInteractionType {
    VIEW_DETAIL,
    BOOKING,
    PAID
}

data class ScheduleInteraction(
    val userId: String,
    val scheduleId: Int,
    val type: ScheduleInteractionType
)

val dummyScheduleInteractions = listOf(
    ScheduleInteraction("USER_CURRENT", 1, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_CURRENT", 6, ScheduleInteractionType.BOOKING),
    ScheduleInteraction("USER_CURRENT", 11, ScheduleInteractionType.VIEW_DETAIL),

    ScheduleInteraction("USER_1", 1, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_1", 3, ScheduleInteractionType.BOOKING),
    ScheduleInteraction("USER_1", 6, ScheduleInteractionType.PAID),

    ScheduleInteraction("USER_2", 1, ScheduleInteractionType.BOOKING),
    ScheduleInteraction("USER_2", 2, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_2", 8, ScheduleInteractionType.BOOKING),

    ScheduleInteraction("USER_3", 6, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_3", 5, ScheduleInteractionType.BOOKING),
    ScheduleInteraction("USER_3", 12, ScheduleInteractionType.VIEW_DETAIL),

    ScheduleInteraction("USER_4", 11, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_4", 14, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_4", 10, ScheduleInteractionType.VIEW_DETAIL),

    ScheduleInteraction("USER_5", 2, ScheduleInteractionType.PAID),
    ScheduleInteraction("USER_5", 4, ScheduleInteractionType.BOOKING),
    ScheduleInteraction("USER_5", 8, ScheduleInteractionType.PAID),
)

fun findItemBasedScheduleRecommendations(
    schedules: List<ShipSchedule>,
    currentUserId: String,
    originPort: Port?,
    destinationPort: Port?,
    selectedDate: String
): List<ShipSchedule> {
    if (originPort == null || destinationPort == null || selectedDate.isBlank()) {
        return emptyList()
    }

    val currentUserInteractions = dummyScheduleInteractions
        .filter { it.userId == currentUserId }

    if (currentUserInteractions.isEmpty()) {
        return findRecommendedSchedules(
            schedules = schedules,
            originPort = originPort,
            destinationPort = destinationPort,
            selectedDate = selectedDate
        )
    }

    val interactedScheduleIds = currentUserInteractions
        .map { it.scheduleId }
        .toSet()

    return schedules
        .filter { schedule ->
            val route = schedule.toRouteParts()

            schedule.id !in interactedScheduleIds &&
                    schedule.status != ShipScheduleStatus.Unavailable &&
                    route.destination.equals(destinationPort.city, ignoreCase = true)
        }
        .map { candidate ->
            val predictionValue = currentUserInteractions.sumOf { interaction ->
                val similarity = calculateItemSimilarity(
                    firstScheduleId = candidate.id,
                    secondScheduleId = interaction.scheduleId
                )

                similarity * interaction.type.toImplicitWeight()
            }

            candidate to predictionValue
        }
        .filter { (_, predictionValue) -> predictionValue > 0.0 }
        .sortedByDescending { (_, predictionValue) -> predictionValue }
        .map { (schedule, _) -> schedule }
}

private fun calculateItemSimilarity(
    firstScheduleId: Int,
    secondScheduleId: Int
): Double {
    val firstVector = dummyScheduleInteractions
        .filter { it.scheduleId == firstScheduleId }
        .associate { it.userId to it.type.toImplicitWeight() }

    val secondVector = dummyScheduleInteractions
        .filter { it.scheduleId == secondScheduleId }
        .associate { it.userId to it.type.toImplicitWeight() }

    val commonUsers = firstVector.keys.intersect(secondVector.keys)

    if (commonUsers.isEmpty()) return 0.0

    val dotProduct = commonUsers.sumOf { userId ->
        firstVector[userId].orZero() * secondVector[userId].orZero()
    }

    val firstMagnitude = sqrt(firstVector.values.sumOf { it * it })
    val secondMagnitude = sqrt(secondVector.values.sumOf { it * it })

    if (firstMagnitude == 0.0 || secondMagnitude == 0.0) return 0.0

    return dotProduct / (firstMagnitude * secondMagnitude)
}

private fun ScheduleInteractionType.toImplicitWeight(): Double {
    return when (this) {
        ScheduleInteractionType.VIEW_DETAIL -> 1.0
        ScheduleInteractionType.BOOKING -> 2.0
        ScheduleInteractionType.PAID -> 3.0
    }
}

private fun ShipSchedule.toRouteParts(): RouteParts {
    val parts = route.split("→", "â†’")

    return RouteParts(
        origin = parts.getOrNull(0)?.trim().orEmpty(),
        destination = parts.getOrNull(1)?.trim().orEmpty()
    )
}

private data class RouteParts(
    val origin: String,
    val destination: String
)

private fun Double?.orZero(): Double = this ?: 0.0