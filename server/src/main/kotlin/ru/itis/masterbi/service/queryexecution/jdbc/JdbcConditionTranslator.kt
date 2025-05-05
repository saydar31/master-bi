package ru.itis.masterbi.service.queryexecution.jdbc

import ru.itis.masterbi.model.condition.*
import kotlin.random.Random


class JdbcConditionTranslator {
    data class SqlCondition(
        val sql: String,
        val params: Map<String, Any>
    )

    fun translate(condition: Condition): SqlCondition {
        val result = when (condition) {
            is AtomicCondition -> buildAtomicCondition(condition)
            is ComplexCondition -> buildComplexCondition(condition)
            else -> throw IllegalArgumentException("Unsupported condition type")
        }

        return if (condition.inverted) {
            result.copy(sql = "NOT (${result.sql})")
        } else {
            result
        }
    }

    private fun buildAtomicCondition(condition: AtomicCondition): SqlCondition {
        val rand = Random.nextInt(50_000)
        val paramName = "p$rand"
        val operator = when(condition.function) {
            AtomicCondition.ConditionFunction.EQ -> "="
            AtomicCondition.ConditionFunction.LT -> "<"
            AtomicCondition.ConditionFunction.LE -> "<="
            AtomicCondition.ConditionFunction.GT -> ">"
            AtomicCondition.ConditionFunction.GE -> ">="
            AtomicCondition.ConditionFunction.IN -> "IN"
            else -> throw IllegalArgumentException("Unsupported function")
        }

        val value = when(condition.argument) {
            is ClosedRange<*> -> {
                val range = condition.argument as ClosedRange<*>
                listOf(range.start, range.endInclusive)
            }
            is OpenEndRange<*> -> {
                val range = condition.argument as OpenEndRange<*>
                listOf(range.start, range.endExclusive)
            }
            is Iterable<*> -> condition.argument.toList()
            else -> condition.argument
        }

        val sql = when(condition.function) {
            AtomicCondition.ConditionFunction.IN -> {
                val placeholders = (value as List<*>).indices.joinToString(prefix = "(", postfix = ")") {
                    ":$paramName$it"
                }
                "${condition.key.name} $operator $placeholders"
            }

            else -> {
                "${condition.key.name} $operator :$paramName"
            }
        }

        val params = when(value) {
            is List<*> -> value.filterNotNull().mapIndexed { i, v -> "${paramName}$i" to v }.toMap()
            else -> mapOf(paramName to value)
        }

        return SqlCondition(sql, params)
    }

    private fun buildComplexCondition(condition: ComplexCondition): SqlCondition {
        val conditions = condition.conditions.map { translate(it) }
        val operator = when(condition.operator) {
            ComplexCondition.Operator.AND -> "AND"
            ComplexCondition.Operator.OR -> "OR"
        }

        val sql = conditions
            .joinToString(" $operator ", prefix = "(", postfix = ")") { it.sql }

        val params = conditions.flatMap { it.params.entries }.associate { it.toPair() }
        return SqlCondition(sql, params)
    }
}