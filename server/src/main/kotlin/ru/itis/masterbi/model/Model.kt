package ru.itis.masterbi.model

data class Dashboard(
    val name: String,
    val grid: List<List<Element>>
)

enum class ElementType{
    HISTOGRAM,
    LINE_CHART
}

interface Element{
    val type: ElementType
    val name: String
}

enum class ScaleType{
    DEFAULT, LOGARITHM
}

data class Scale(
    val name: String,
    val scaleType: ScaleType = ScaleType.DEFAULT
)

interface ScaledElement: Element{
    val abscissa: Scale
    val ordinate: Scale
}

abstract class AbstractScaledElement(
    override val type: ElementType
): ScaledElement{
    override lateinit var name: String
    override lateinit var abscissa: Scale
    override lateinit var ordinate: Scale
}

class Histogram: AbstractScaledElement(ElementType.HISTOGRAM)
class LineChart: AbstractScaledElement(ElementType.LINE_CHART)
