package ru.itis.masterbi.config.datasource

import ru.itis.masterbi.model.build.DataBuilders.csv
import java.time.LocalDate
import java.time.DayOfWeek.FRIDAY
import java.time.DayOfWeek.SATURDAY
import java.time.DayOfWeek.SUNDAY
import java.time.Month.MARCH
import java.time.Month.NOVEMBER
import java.time.format.DateTimeFormatter
import kotlin.random.Random
import kotlin.random.nextInt

object YearApplies {

    private val lastDaysOfWeek = listOf(FRIDAY, SATURDAY, SUNDAY)
    private val peakMonth = listOf(MARCH, NOVEMBER)
    private val dateFormatter = DateTimeFormatter.ISO_DATE

    val applications = csv {
        literal(buildString {
            appendLine("day;amount")
            val today = LocalDate.now()
            val yearAgo = today.minusYears(1)
            val period = yearAgo.datesUntil(today.plusDays(1))
            period.forEach {
                var k = 1.0
                if (it.dayOfWeek in lastDaysOfWeek) {
                    k *= 1.5
                }
                if (it.month in peakMonth) {
                    k *= 2.0
                }
                val base = Random.nextInt(1 .. 3)
                val amount = (base * k).toInt()
                val dateIso = dateFormatter.format(it)
                appendLine("$dateIso;$amount")
            }
        })
    }
}