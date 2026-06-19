package com.dicoding.tugas_akhir.data.recomendation

import com.dicoding.tugas_akhir.data.dummy.PopularRoute
import kotlin.math.sqrt

enum class PopularRouteInteractionType {
    SEARCH,
    VIEW_DETAIL,
    BOOKING,
}

data class PopularRouteInteraction(
    val userId: String,
    val routeId: Int,
    val type: PopularRouteInteractionType,
)

private val dummyPopularRouteInteractions = listOf(
    PopularRouteInteraction("USER_CURRENT", 1, PopularRouteInteractionType.BOOKING),

    PopularRouteInteraction("USER_1", 1, PopularRouteInteractionType.BOOKING),
    PopularRouteInteraction("USER_1", 2, PopularRouteInteractionType.SEARCH),

    PopularRouteInteraction("USER_2", 1, PopularRouteInteractionType.VIEW_DETAIL),
    PopularRouteInteraction("USER_2", 3, PopularRouteInteractionType.BOOKING),

    PopularRouteInteraction("USER_3", 2, PopularRouteInteractionType.BOOKING),
    PopularRouteInteraction("USER_3", 3, PopularRouteInteractionType.SEARCH),
)

fun findItemBasedPopularRoutes(
    routes: List<PopularRoute>,
    currentUserId: String,
): List<PopularRoute> {
    val currentUserInteractions = dummyPopularRouteInteractions
        .filter { it.userId == currentUserId }

    if (currentUserInteractions.isEmpty()) {
        return routes.sortedByDescending { it.popularityScore }
    }

    val interactedRouteIds = currentUserInteractions.map { it.routeId }.toSet()

    val scoredRoutes = routes
        .filter { it.id !in interactedRouteIds }
        .map { route ->
            val score = currentUserInteractions.sumOf { interaction ->
                calculateRouteSimilarity(route.id, interaction.routeId) *
                        interaction.type.toImplicitWeight()
            }

            route to score
        }
        .filter { (_, score) -> score > 0.0 }
        .sortedByDescending { (_, score) -> score }

    val scoredRouteIds = scoredRoutes.map { it.first.id }.toSet()

    val fallbackRoutes = routes
        .filter { it.id !in interactedRouteIds && it.id !in scoredRouteIds }
        .sortedByDescending { it.popularityScore }

    return (scoredRoutes.map { it.first } + fallbackRoutes)
        .ifEmpty { routes.sortedByDescending { it.popularityScore } }
}

private fun calculateRouteSimilarity(
    firstRouteId: Int,
    secondRouteId: Int,
): Double {
    val firstVector = routeVector(firstRouteId)
    val secondVector = routeVector(secondRouteId)
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

private fun routeVector(routeId: Int): Map<String, Double> {
    return dummyPopularRouteInteractions
        .filter { it.routeId == routeId }
        .groupBy { it.userId }
        .mapValues { (_, interactions) ->
            interactions.maxOf { it.type.toImplicitWeight() }
        }
}

private fun PopularRouteInteractionType.toImplicitWeight(): Double {
    return when (this) {
        PopularRouteInteractionType.SEARCH -> 1.0
        PopularRouteInteractionType.VIEW_DETAIL -> 2.0
        PopularRouteInteractionType.BOOKING -> 3.0
    }
}

private fun Double?.orZero(): Double = this ?: 0.0