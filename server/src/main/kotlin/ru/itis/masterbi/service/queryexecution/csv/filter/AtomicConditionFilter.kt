package ru.itis.masterbi.service.queryexecution.csv.filter

import org.springframework.stereotype.Service
import ru.itis.masterbi.model.ValueType
import ru.itis.masterbi.model.condition.AtomicCondition
import ru.itis.masterbi.model.condition.AtomicCondition.ConditionFunction.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface AtomicConditionFilter {
    val valueType: ValueType
    val functions: List<AtomicCondition.ConditionFunction>

    fun filter(value: String, condition: AtomicCondition): Boolean
}

@Service
class NumberCompareConditionFilter : AtomicConditionFilter {
    override val valueType = ValueType.NUMBER
    override val functions = listOf(EQ, LE, LT, GE, GT)


    override fun filter(value: String, condition: AtomicCondition): Boolean {
        val number = value.toDouble()
        val arg = (condition.argument as Number).toDouble()
        val function = condition.function

        return when (function) {
            EQ -> number == arg
            LE -> number <= arg
            LT -> number < arg
            GE -> number >= arg
            GT -> number > arg
            else -> throw RuntimeException("")
        }
    }

}

@Service
class NumberInConditionFilter : AtomicConditionFilter {
    override val valueType = ValueType.NUMBER
    override val functions = listOf(IN)

    override fun filter(value: String, condition: AtomicCondition): Boolean {
        val args = (condition.argument as Iterable<*>)
            .asSequence()
            .map { (it as Number).toDouble() }
            .toSet()
        return value.toDouble() in args
    }
}

@Service
class StringCompareConditionFilter : AtomicConditionFilter {
    override val valueType = ValueType.STRING
    override val functions = listOf(EQ, LE, LT, GE, GT)


    override fun filter(value: String, condition: AtomicCondition): Boolean {
        val arg = condition.argument.toString()
        val function = condition.function

        return when (function) {
            EQ -> value == arg
            LE -> value <= arg
            LT -> value < arg
            GE -> value >= arg
            GT -> value > arg
            else -> throw RuntimeException("")
        }
    }
}

@Service
class StringInConditionFilter : AtomicConditionFilter {
    override val valueType = ValueType.STRING
    override val functions = listOf(IN)

    override fun filter(value: String, condition: AtomicCondition): Boolean {
        val args = (condition.argument as Iterable<*>)
            .toSet()
        return value in args
    }
}

private val formatter = DateTimeFormatter.ISO_DATE_TIME

@Service
class TimestampCompareConditionFilter : AtomicConditionFilter {
    override val valueType = ValueType.TIMESTAMP
    override val functions = listOf(EQ, LE, LT, GE, GT)

    override fun filter(value: String, condition: AtomicCondition): Boolean {
        val arg = when (condition.argument) {
            is LocalDateTime -> condition.argument
            is LocalDate -> condition.argument.atStartOfDay()
            else -> throw RuntimeException("")
        }
        val function = condition.function
        val dateTime = LocalDateTime.parse(value, formatter)

        return when (function) {
            EQ -> dateTime == arg
            LE -> dateTime <= arg
            LT -> dateTime < arg
            GE -> dateTime >= arg
            GT -> dateTime > arg
            else -> throw RuntimeException("")
        }
    }
}


@Service
class DateInConditionFilter : AtomicConditionFilter {
    override val valueType = ValueType.TIMESTAMP
    override val functions = listOf(IN)

    override fun filter(value: String, condition: AtomicCondition): Boolean {
        val dateTime = LocalDateTime.parse(value, formatter)
        if (condition.argument is Iterable<*>) {
            val timestamps = condition.argument as Iterable<LocalDateTime>
            return dateTime in timestamps
        }
        if (condition.argument is ClosedRange<*>) {
            val range = condition.argument as ClosedRange<LocalDateTime>
            return dateTime in range
        }
        if (condition.argument is OpenEndRange<*>) {
            val range = condition.argument as OpenEndRange<LocalDateTime>
            return dateTime in range
        }
        throw RuntimeException()
    }
}


