package ru.itis.masterbi.model

class VisualizationProps {
    var color: String? = null
    var shape: MarkerShape? = null
    var lineType: LineType? = null
    var keyProps: MutableMap<String, VisualizationProps> = mutableMapOf()

    enum class MarkerShape {
        CIRCLE, TRIANGLE, SQUARE
    }

    enum class LineType {
        DEFAULT, DOTTED
    }

}