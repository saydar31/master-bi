package ru.itis.masterbi.service.queryexecution.transformer

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.QueryResultUnit
import java.time.DayOfWeek
import java.time.DayOfWeek.MONDAY
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import kotlin.math.max

class ActivityGraphTransformerTest {

    private lateinit var transformer: ActivityGraphTransformer

    @BeforeEach
    fun setUp() {
        transformer = ActivityGraphTransformer()
    }


    @Test
    @DisplayName("Transform with empty data returns empty result")
    fun getTransformed_EmptyData_ReturnsEmptyResult() {
        val queryResult = QueryResult(emptyList())
        val result = transformer.getTransformed(queryResult)
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        assertEquals(0, activityResult.weeksCount)
        assertEquals(7, activityResult.numbers.size) // 7 days of week
        assertTrue(activityResult.numbers.all { it.all { value -> value == 0 } })
    }

    @Test
    @DisplayName("Transform with single day data")
    fun getTransformed_SingleDayData_CorrectResult() {
        val data = listOf(
            QueryResultUnit("2024-01-01", "5") // Monday
        )
        val queryResult = QueryResult(data)
        val result = transformer.getTransformed(queryResult) as QueryResult
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        assertEquals(1, activityResult.weeksCount)
        assertEquals(5, activityResult.numbers[1][0]) // Monday is day 1 (ISO)
        assertEquals(0, activityResult.numbers[0][0]) // Sunday should be 0
    }

    @Test
    @DisplayName("Transform with multiple weeks data")
    fun getTransformed_MultipleWeeksData_CorrectResult() {
        val data = listOf(
            QueryResultUnit("2024-01-01", "1"), // Monday, week 0
            QueryResultUnit("2024-01-02", "2"), // Tuesday, week 0
            QueryResultUnit("2024-01-08", "3"), // Monday, week 1
            QueryResultUnit("2024-01-09", "4")  // Tuesday, week 1
        )
        val queryResult = QueryResult(data)
        val result = transformer.getTransformed(queryResult) as QueryResult
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        assertEquals(2, activityResult.weeksCount)
        assertEquals(1, activityResult.numbers[1][0]) // Monday week 0
        assertEquals(2, activityResult.numbers[2][0]) // Tuesday week 0
        assertEquals(3, activityResult.numbers[1][1]) // Monday week 1
        assertEquals(4, activityResult.numbers[2][1]) // Tuesday week 1
    }

    @Test
    @DisplayName("Transform with null values converts to 0")
    fun getTransformed_NullValues_ConvertedToZero() {
        val data = listOf(
            QueryResultUnit("2024-01-01", null) // Monday
        )
        val queryResult = QueryResult(data)
        val result = transformer.getTransformed(queryResult) as QueryResult
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        assertEquals(0, activityResult.numbers[1][0])
    }

    @Test
    @DisplayName("Transform with dates in different formats")
    fun getTransformed_DifferentDateFormats_CorrectParsing() {
        val data = listOf(
            QueryResultUnit("2024-01-01 12:00:00", "1"), // With time
            QueryResultUnit("2024-01-02T00:00:00Z", "2"), // ISO format
            QueryResultUnit("2024-01-03", "3") // Simple format
        )
        val queryResult = QueryResult(data)
        val result = transformer.getTransformed(queryResult) as QueryResult
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        assertEquals(1, activityResult.numbers[1][0]) // Monday
        assertEquals(2, activityResult.numbers[2][0]) // Tuesday
        assertEquals(3, activityResult.numbers[3][0]) // Wednesday
    }

    @Test
    @DisplayName("Week calculation spans correct number of weeks")
    fun getTransformed_WeekCalculation_CorrectSpan() {
        val data = listOf(
            QueryResultUnit("2024-01-01", "1"), // Week 0
            QueryResultUnit("2024-01-15", "2")   // Week 2 (2 weeks later)
        )
        val queryResult = QueryResult(data)
        val result = transformer.getTransformed(queryResult) as QueryResult
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        assertEquals(3, activityResult.weeksCount) // Weeks 0, 1, 2
    }

    @Test
    fun testSundayActivity(){
        val start = LocalDate.of(2025, Month.JANUARY, 1)
        val dates = (0..60).map {
            start.plusDays(it.toLong())
        }
        val mondayCount = dates.count { it.dayOfWeek == MONDAY }

        val data = dates.map {
            val dateStr = it.format(DateTimeFormatter.ISO_DATE)
            val value = if (it.dayOfWeek == MONDAY) {"1"} else {"0"}
            QueryResultUnit(dateStr, value)
        }
        val testResult = QueryResult(data)
        val result = transformer.getTransformed(testResult) as QueryResult
        val activityResult = result.transformedData as ActivityGraphTransformer.ActivityQueryResult

        activityResult.numbers[MONDAY.value-1].sum() shouldBe mondayCount
    }
}