package ru.itis.masterbi.service.queryexecution.transformer

import org.springframework.stereotype.Service
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.model.build.ElementBuilders.ActivityGraphBuilder.ActivityGraphQuery
import ru.itis.masterbi.service.queryexecution.QueryResult
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.ceil

@Service
class ActivityGraphTransformer : QueryResultTransformer {

    override fun isSuitable(query: Query): Boolean = query is ActivityGraphQuery

    override fun getTransformed(queryResult: QueryResult): QueryResult {
        if (queryResult.data.isEmpty()) return queryResult.copy(
            transformedData = defaultTransformation
        )

        val dateAndValues = queryResult.data.map {
            val dateStr = it.key.substring(0, DATE_PATTERN.length)
            val date = LocalDate.parse(dateStr, formatter)
            date to it.value
        }

        val maxDate = dateAndValues.asSequence()
            .map { it.first }
            .max()
        val minDate = dateAndValues.asSequence()
            .map { it.first }
            .min()

        val weeks = ceil((maxDate.toEpochDay() - minDate.toEpochDay() + 1) / 7.0).toInt()

        val dowToValues = dateAndValues.groupBy { it.first.dayOfWeek.value }
        val dowToWeekToValue = dowToValues
            .mapValues { (_, dvs) ->
                dvs.associate {
                    val weeksBetween = it.first.toEpochDay().minus(minDate.toEpochDay()) / 7
                    weeksBetween.toInt() to (it.second?.toInt() ?: 0)
                }
            }
        val resultArray = Array(7) { dow ->
            IntArray(weeks) { numberOfWeek ->
                dowToWeekToValue[dow + 1]?.get(numberOfWeek) ?: 0
            }
        }
        return queryResult.copy(
            transformedData = ActivityQueryResult(resultArray, weeks)
        )
    }

    class ActivityQueryResult(
        val numbers: Array<IntArray>,
        val weeksCount: Int
    )

    companion object {
        private const val DATE_PATTERN = "yyyy-MM-dd"
        private val formatter = DateTimeFormatter.ofPattern(DATE_PATTERN)

        private val defaultTransformation = ActivityQueryResult(
            Array(7) { IntArray(0) },
            0
        )
    }

}