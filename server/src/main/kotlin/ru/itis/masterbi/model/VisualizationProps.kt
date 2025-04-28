package ru.itis.masterbi.model

import com.fasterxml.jackson.annotation.JsonValue

class VisualizationProps {
    var color: String? = null
    var shape: MarkerShape? = null
    var lineType: LineType? = null
    var keyProps: MutableMap<String, VisualizationProps> = mutableMapOf()

    enum class MarkerShape {
        CIRCLE, TRIANGLE, RECT, CROSS;

        @JsonValue
        fun lowerCaseName() = this.name.lowercase()
    }

    enum class LineType {
        DEFAULT, DOTTED
    }

}