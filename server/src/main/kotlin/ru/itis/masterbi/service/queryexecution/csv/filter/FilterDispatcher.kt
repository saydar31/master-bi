package ru.itis.masterbi.service.queryexecution.csv.filter

import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import ru.itis.masterbi.model.ValueType
import ru.itis.masterbi.model.condition.AtomicCondition
import ru.itis.masterbi.model.condition.AtomicCondition.ConditionFunction

@Service
class FilterDispatcher(
    filters: List<AtomicConditionFilter>
) {
    private val filterMap: Map<ValueType, Map<ConditionFunction, AtomicConditionFilter>> =
        filters.groupBy { it.valueType }
            .mapValues {
                it.value.flatMap { filter ->
                    filter.functions.map { f -> f to filter }
                }.associate { it }
            }

    fun getFilter(condition: AtomicCondition): (CSVRecord) -> Boolean {
        val filter = filterMap[condition.key.type]?.get(condition.function)
        return filter?.let { f ->
            return {
                val value = it[condition.key.name]
                f.filter(value, condition)
            }
        } ?: { true }
    }
}