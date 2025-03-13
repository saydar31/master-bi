package ru.itis.masterbi.dsl

import ru.itis.masterbi.model.Dashboard
import ru.itis.masterbi.model.Element

fun dashboard(init: DashboardBuilder.() -> Unit): Dashboard {
    val builder = DashboardBuilder()
    builder.init()
    return builder.build()
}

class DashboardBuilder {
    var name: String? = null
    private var grid: MutableList<MutableList<Element>> = mutableListOf()

    fun row(init: RowBuilder.() -> Unit) {
        val builder = RowBuilder()
        builder.init()
        grid.add(builder.build())
    }

    fun build() = Dashboard(
        name = name ?: throw RuntimeException(), grid = grid
    )
}

class RowBuilder {
    val row: MutableList<Element> = mutableListOf()

    inline fun <reified T : Element> append(init: T.() -> Unit) {
        val element = T::class.constructors
            .find { it.parameters.isEmpty() }
            ?.call() ?: throw RuntimeException("No empty constructor")

        element.init()
        row.add(element)
    }

    fun build() = row
}