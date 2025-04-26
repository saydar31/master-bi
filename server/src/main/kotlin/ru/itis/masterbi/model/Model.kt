package ru.itis.masterbi.model

data class Dashboard(
    val name: String,
    val grid: List<List<Element>>
)

enum class ElementType {
    HISTOGRAM,
    LINE_CHART,
    PIE_CHART,
    DONUT_CHART,
    TAG_CLOUD
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
    val collection: Collection
    val key: KeyDescription
    val value: KeyDescription
}

enum class ScaleType {
    LINEAR, LOGARITHM
}

data class Scale(
    val name: String,
    val scaleType: ScaleType = ScaleType.LINEAR
)

interface ScaledElement : Element {
    val abscissa: Scale
    val ordinate: Scale
}


