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
    private val row: MutableList<Element> = mutableListOf()

    fun build() = row

    infix operator fun RowBuilder.plus(element: Element): RowBuilder {
        row.add(element)
        return this
    }

    operator fun Element.unaryPlus() {
        row.add(this)
    }
}