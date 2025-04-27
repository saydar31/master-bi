package ru.itis.masterbi.model

// AbstractScaledElement now uses a primary constructor
abstract class AbstractScaledElement(
    override val type: ElementType,
    override val name: String,
    override val abscissa: Scale,
    override val ordinate: Scale,
    override val queries: MutableList<Query> = mutableListOf()
) : ScaledElement

class Histogram(
    name: String,
    abscissa: Scale,
    ordinate: Scale,
    queries: MutableList<Query> = mutableListOf()
) : AbstractScaledElement(ElementType.HISTOGRAM, name, abscissa, ordinate, queries)

class LineChart(
    name: String,
    abscissa: Scale,
    ordinate: Scale,
    queries: MutableList<Query> = mutableListOf()
) : AbstractScaledElement(ElementType.LINE_CHART, name, abscissa, ordinate, queries)

class PieChart(
    override val name: String,
    override val queries: List<Query> = emptyList()
) : Element {
    override val type: ElementType = ElementType.PIE_CHART
}

class DonutChart(
    override val name: String,
    override val queries: List<Query> = emptyList()
) : Element {
    override val type: ElementType = ElementType.DONUT_CHART
}

class TagCloud(
    override val name: String,
    override val queries: List<Query> = emptyList(),
    val maxFontSize: Int = 42,
    val minFontSize: Int = 16,
    val colorPalette: List<String> = defaultPalette
) : Element {
    override val type: ElementType = ElementType.TAG_CLOUD
}

val defaultPalette = listOf("#FF5733", "#33FF57", "#3357FF")