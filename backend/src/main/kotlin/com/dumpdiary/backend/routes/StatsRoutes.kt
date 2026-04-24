package com.dumpdiary.backend.routes

import com.dumpdiary.backend.model.MonthlySummary
import com.dumpdiary.backend.model.StreakSummary
import com.dumpdiary.backend.model.YearlyTrendPoint
import com.dumpdiary.backend.repository.InMemoryStore
import com.dumpdiary.backend.security.AuthenticatedUser
import io.ktor.server.application.call
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

fun Route.statsRoutes(store: InMemoryStore) {
    get("/stats/monthly") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val month = call.request.queryParameters["month"] ?: LocalDate.now().toString().take(7)
        val logs = store.visibleLogs(userId).filter { !it.isDeleted && it.dateKey.startsWith(month) }
        call.respond(
            MonthlySummary(
                month = month,
                totalCount = logs.size,
                activeDays = logs.map { it.dateKey }.distinct().size,
            ),
        )
    }

    get("/stats/streak") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val dates = store.visibleLogs(userId)
            .filter { !it.isDeleted }
            .map { LocalDate.parse(it.dateKey, dateFormatter) }
            .distinct()
            .sorted()
        if (dates.isEmpty()) {
            call.respond(StreakSummary(0, 0))
            return@get
        }
        var maxStreak = 1
        var currentStreak = 1
        for (index in 1 until dates.size) {
            currentStreak = if (dates[index - 1].plusDays(1) == dates[index]) currentStreak + 1 else 1
            maxStreak = maxOf(maxStreak, currentStreak)
        }
        var endingStreak = 1
        for (index in dates.lastIndex downTo 1) {
            if (dates[index - 1].plusDays(1) == dates[index]) {
                endingStreak += 1
            } else {
                break
            }
        }
        val today = LocalDate.now()
        val current = if (dates.last() == today || dates.last() == today.minusDays(1)) endingStreak else 0
        call.respond(StreakSummary(currentStreakDays = current, maxStreakDays = maxStreak))
    }

    get("/stats/yearly") {
        val userId = call.principal<AuthenticatedUser>()!!.userId
        val year = call.request.queryParameters["year"]?.toIntOrNull() ?: LocalDate.now().year
        val counts = (1..12).associateWith { 0 }.toMutableMap()
        store.visibleLogs(userId)
            .filter { !it.isDeleted && it.dateKey.startsWith("$year-") }
            .forEach { log ->
                val month = LocalDate.parse(log.dateKey, dateFormatter).monthValue
                counts[month] = (counts[month] ?: 0) + 1
            }
        call.respond((1..12).map { month -> YearlyTrendPoint(month, counts[month] ?: 0) })
    }
}
