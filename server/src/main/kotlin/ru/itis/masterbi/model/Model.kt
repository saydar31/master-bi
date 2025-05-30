package ru.itis.masterbi.model

import ru.itis.masterbi.model.condition.Condition

data class Dashboard(
    val name: String,
    val grid: List<List<Element>>
)

enum class ElementType {
    HISTOGRAM,
    LINE_CHART,
    PIE_CHART,
    DONUT_CHART,
    TAG_CLOUD,
    ACTIVITY_GRAPH
}

interface Element {
    val type: ElementType
    val name: String
    val queries: List<Query>
        get() = listOf()
}

enum class DatasourceType {
    JDBC,
    MONGO,
    CSV
}

interface Datasource {
    val type: DatasourceType
}

interface Collection {
    val name: String
    val datasource: Datasource
}

enum class ValueType {
    STRING, TIMESTAMP, NUMBER
}

interface KeyDescription {
    val name: String
    val type: ValueType
}

interface Query {
    val id: String
    val label: String?
    val collection: Collection
    val key: KeyDescription
    val value: KeyDescription
}

interface FilteredQuery: Query{
    val condition: Condition
}

enum class ScaleType {
    LINEAR, LOGARITHM
}

data class Scale(
    val name: String,
    val displayName: String? = null,
    val scaleType: ScaleType = ScaleType.LINEAR
)

fun linear(name: String, displayName: String? = null) = Scale(name, displayName = displayName)
fun logarithm(name: String, displayName: String? = null) =
    Scale(name, scaleType = ScaleType.LOGARITHM, displayName = displayName)

interface ScaledElement : Element {
    val abscissa: Scale
    val ordinate: Scale
}


