package ru.itis.masterbi.model.build

import ru.itis.masterbi.model.*

object ElementBuilders {

    // Builder for Histogram
    fun histogram(init: HistogramBuilder.() -> Unit): Histogram {
        val builder = HistogramBuilder()
        builder.init()
        return builder.build()
    }

    class HistogramBuilder {
        lateinit var name: String
        lateinit var abscissa: Scale
        lateinit var ordinate: Scale
        private val queries: MutableList<Query> = mutableListOf()

        operator fun Query.unaryPlus() {
            queries.add(this)
        }

        fun build(): Histogram {
            validate()
            return Histogram(name, abscissa, ordinate, queries)
        }

        private fun validate() {
            require(name.isNotBlank()) { "Name must not be blank" }
            require(::abscissa.isInitialized) { "Abscissa must be initialized" }
            require(::ordinate.isInitialized) { "Ordinate must be initialized" }
        }
    }

    // Builder for LineChart
    fun lineChart(init: LineChartBuilder.() -> Unit): LineChart {
        val builder = LineChartBuilder()
        builder.init()
        return builder.build()
    }

    class LineChartBuilder {
        lateinit var name: String
        lateinit var abscissa: Scale
        lateinit var ordinate: Scale
        private val queries: MutableList<Query> = mutableListOf()

        operator fun Query.unaryPlus() {
            queries.add(this)
        }

        fun build(): LineChart {
            validate()
            return LineChart(name, abscissa, ordinate, queries)
        }

        private fun validate() {
            require(name.isNotBlank()) { "Name must not be blank" }
            require(::abscissa.isInitialized) { "Abscissa must be initialized" }
            require(::ordinate.isInitialized) { "Ordinate must be initialized" }
        }
    }

    // Builder for PieChart
    fun pieChart(init: PieChartBuilder.() -> Unit): PieChart {
        val builder = PieChartBuilder()
        builder.init()
        return builder.build()
    }

    class PieChartBuilder {
        lateinit var name: String
        private val queries: MutableList<Query> = mutableListOf()

        operator fun Query.unaryPlus() {
            queries.add(this)
        }

        fun build(): PieChart {
            validate()
            return PieChart(name, queries)
        }

        private fun validate() {
            require(name.isNotBlank()) { "Name must not be blank" }
        }
    }

    // Builder for DonutChart
    fun donutChart(init: DonutChartBuilder.() -> Unit): DonutChart {
        val builder = DonutChartBuilder()
        builder.init()
        return builder.build()
    }

    class DonutChartBuilder {
        lateinit var name: String
        private val queries: MutableList<Query> = mutableListOf()

        operator fun Query.unaryPlus() {
            queries.add(this)
        }

        fun build(): DonutChart {
            validate()
            return DonutChart(name, queries)
        }

        private fun validate() {
            require(name.isNotBlank()) { "Name must not be blank" }
        }
    }

    // Builder for TagCloud
    fun tagCloud(init: TagCloudBuilder.() -> Unit): TagCloud {
        val builder = TagCloudBuilder()
        builder.init()
        return builder.build()
    }

    class TagCloudBuilder {
        lateinit var name: String
        var queries: MutableList<Query> = mutableListOf()
        var maxFontSize: Int = 42
        var minFontSize: Int = 16
        var colorPalette: List<String> = defaultPalette

        operator fun Query.unaryPlus() {
            queries.add(this)
        }

        fun build(): TagCloud {
            validate()
            return TagCloud(name, queries, maxFontSize, minFontSize, colorPalette)
        }

        private fun validate() {
            require(name.isNotBlank()) { "Name must not be blank" }
            require(maxFontSize > minFontSize) { "maxFontSize must be greater than minFontSize" }
            require(colorPalette.isNotEmpty()) { "Color palette must not be empty" }
        }
    }

    fun activityGraph(init: ActivityGraphBuilder.() -> Unit): ActivityGraph {
        val builder = ActivityGraphBuilder()
        builder.init()
        return builder.build()
    }

    class ActivityGraphBuilder {
        lateinit var name: String
        private val queries: MutableList<Query> = mutableListOf()

        operator fun Query.unaryPlus() {
            queries.add(this)
        }

        fun build(): ActivityGraph {
            validate()
            return ActivityGraph(name, queries)
        }

        private fun validate() {
            require(queries.all { it.key.type == ValueType.TIMESTAMP })
            require(name.isNotBlank()) { "Name must not be blank" }
        }
    }
}