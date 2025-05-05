package ru.itis.masterbi.service.queryexecution.mongo

import org.bson.conversions.Bson
import com.mongodb.client.model.Filters.*
import ru.itis.masterbi.model.condition.*
import ru.itis.masterbi.model.ValueType

class MongoConditionTranslator {

    fun translate(condition: Condition): Bson {
        val filter = when (condition) {
            is AtomicCondition -> buildAtomicFilter(condition)
            is ComplexCondition -> buildComplexFilter(condition)
            else -> throw IllegalArgumentException("Unsupported condition type")
        }

        return if (condition.inverted) not(filter) else filter
    }

    private fun buildAtomicFilter(condition: AtomicCondition): Bson {
        val key = condition.key.name

        return when (condition.function) {
            AtomicCondition.ConditionFunction.EQ ->
                eq(key, condition.argument)

            AtomicCondition.ConditionFunction.LT ->
                lt(key, condition.argument)

            AtomicCondition.ConditionFunction.LE ->
                lte(key, condition.argument)

            AtomicCondition.ConditionFunction.GT ->
                gt(key, condition.argument)

            AtomicCondition.ConditionFunction.GE ->
                gte(key, condition.argument)

            AtomicCondition.ConditionFunction.IN -> {
                when (condition.argument) {
                    is ClosedRange<*> -> {
                        val range = condition.argument as ClosedRange<*>
                        and(
                            gte(key, range.start),
                            lte(key, range.endInclusive)
                        )
                    }

                    is Iterable<*> -> {
                        val values = condition.argument.toList()
                        `in`(key, values)
                    }

                    else -> throw IllegalArgumentException("Unsupported IN argument type")
                }
            }

            else -> throw IllegalArgumentException("Unsupported function: ${condition.function}")
        }
    }

    private fun buildComplexFilter(condition: ComplexCondition): Bson {
        val filters = condition.conditions.map { translate(it) }

        return when (condition.operator) {
            ComplexCondition.Operator.AND -> and(filters)
            ComplexCondition.Operator.OR -> or(filters)
        }
    }
}