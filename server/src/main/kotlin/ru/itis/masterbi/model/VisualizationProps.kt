package ru.itis.masterbi.model

import com.fasterxml.jackson.annotation.JsonValue

class VisualizationProps {
    var color: String? = null
    val backgroundColor: String? = null
    var shape: MarkerShape? = null
    var lineType: LineType? = null
    var font: FontProps? = null

    var keyProps: MutableMap<String, VisualizationProps> = mutableMapOf()

    enum class MarkerShape {
        CIRCLE, TRIANGLE, RECT, CROSS;

        @JsonValue
        fun lowerCaseName() = this.name.lowercase()
    }

    enum class LineType {
        SOLID, DOTTED, DASHED
    }

    data class FontProps(
        var family: String? = null, // e.g., "Arial"
        var size: Int? = null, // e.g., 14
        var weight: String? = null // e.g., "bold"
    )

}