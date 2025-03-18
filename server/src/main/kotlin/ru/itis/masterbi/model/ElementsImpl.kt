package ru.itis.masterbi.model

abstract class AbstractScaledElement(
    override val type: ElementType
) : ScaledElement {
    override lateinit var name: String
    override lateinit var abscissa: Scale
    override lateinit var ordinate: Scale
}

class Histogram : AbstractScaledElement(ElementType.HISTOGRAM)
class LineChart : AbstractScaledElement(ElementType.LINE_CHART)

class PieChart : Element {
    override lateinit var name: String
    override val type: ElementType = ElementType.PIE_CHART
}

class DonutChart : Element {
    override lateinit var name: String
    override val type: ElementType = ElementType.DONUT_CHART
}