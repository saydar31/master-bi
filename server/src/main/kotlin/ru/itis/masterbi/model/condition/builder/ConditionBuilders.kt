package ru.itis.masterbi.model.condition.builder

import ru.itis.masterbi.model.KeyDescription
import ru.itis.masterbi.model.condition.AtomicCondition
import ru.itis.masterbi.model.condition.ComplexCondition
import ru.itis.masterbi.model.condition.Condition

class ComplexConditionBuilder(
    private val operator: ComplexCondition.Operator
) {
    private val conditions = mutableListOf<Condition>()


    operator fun Condition.unaryPlus() {
        conditions.add(this)
    }

    fun build(): Condition {
        require(conditions.size >= 2) { "Complex condition should have at least 2 conditions" }
        return ComplexCondition(conditions, operator)
    }
}

private fun complexCondition(
    operator: ComplexCondition.Operator,
    init: ComplexConditionBuilder.() -> Unit
): Condition {
    val condition = ComplexConditionBuilder(operator)
    init(condition)
    return condition.build()
}

fun or(init: ComplexConditionBuilder.() -> Unit) = complexCondition(ComplexCondition.Operator.OR, init)


fun and(init: ComplexConditionBuilder.() -> Unit) = complexCondition(ComplexCondition.Operator.AND, init)
infix fun KeyDescription.eq(arg: Any) = AtomicCondition(this, AtomicCondition.ConditionFunction.EQ, arg)
infix fun KeyDescription.le(arg: Any) = AtomicCondition(this, AtomicCondition.ConditionFunction.LE, arg)
infix fun KeyDescription.lt(arg: Any) = AtomicCondition(this, AtomicCondition.ConditionFunction.LT, arg)
infix fun KeyDescription.gt(arg: Any) = AtomicCondition(this, AtomicCondition.ConditionFunction.GT, arg)
infix fun KeyDescription.ge(arg: Any) = AtomicCondition(this, AtomicCondition.ConditionFunction.GE, arg)
infix fun KeyDescription.isIn(arg: Any) = if (arg is Iterable<*> || arg is ClosedRange<*>) {
    AtomicCondition(this, AtomicCondition.ConditionFunction.IN, arg)
} else {
    throw RuntimeException()
}

