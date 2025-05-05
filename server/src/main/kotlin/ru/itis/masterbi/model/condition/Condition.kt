package ru.itis.masterbi.model.condition

import ru.itis.masterbi.model.KeyDescription

interface Condition {
    val inverted: Boolean
    operator fun not(): Condition
}

data class ComplexCondition(
    val conditions: List<Condition>,
    val operator: Operator,
    override val inverted: Boolean = false
) : Condition {
    enum class Operator {
        OR, AND
    }

    override fun not(): Condition = copy(inverted = !this.inverted)

}

data class AtomicCondition(
    val key: KeyDescription,
    val function: ConditionFunction,
    val argument: Any,
    override val inverted: Boolean = false
) : Condition {

    override fun not(): Condition = copy(inverted = !this.inverted)

    enum class ConditionFunction {
        EQ,
        LT,
        LE,
        GT,
        GE,
        IN
    }
}

