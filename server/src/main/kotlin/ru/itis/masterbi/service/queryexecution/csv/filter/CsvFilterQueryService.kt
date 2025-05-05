package ru.itis.masterbi.service.queryexecution.csv.filter

import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import ru.itis.masterbi.model.condition.AtomicCondition
import ru.itis.masterbi.model.condition.ComplexCondition
import ru.itis.masterbi.model.condition.ComplexCondition.Operator.AND
import ru.itis.masterbi.model.condition.ComplexCondition.Operator.OR
import ru.itis.masterbi.model.condition.Condition

@Service
class CsvFilterQueryService(
    val filterDispatcher: FilterDispatcher
) {

    fun buildComplex(condition: ComplexCondition): (CSVRecord) -> Boolean {
        // Pre-build the individual filter functions from the conditions.
        val filters = condition.conditions.map { buildFilter(it) }

        return { value: CSVRecord ->
            when (condition.operator) {
                AND -> {
                    // For AND, if any filter returns false, the overall result is false.
                    filters.all { it(value) }
                }

                OR -> {
                    // For OR, if any filter returns true, the overall result is true.
                    filters.any { it(value) }
                }
            }
        }.let {
            if (condition.inverted) {
                return it.invert()
            } else {
                it
            }
        }
    }

    fun buildAtomic(condition: AtomicCondition) = filterDispatcher.getFilter(condition).let {
        if (condition.inverted) {
            it.invert()
        } else {
            it
        }
    }

    fun ((CSVRecord) -> Boolean).invert(): (CSVRecord) -> Boolean = { s -> !this(s) }


    fun buildFilter(rootCondition: Condition): (CSVRecord) -> Boolean {
        return when (rootCondition) {
            is ComplexCondition -> buildComplex(rootCondition)
            is AtomicCondition -> buildAtomic(rootCondition)
            else -> {
                { true }
            }
        }
    }
}