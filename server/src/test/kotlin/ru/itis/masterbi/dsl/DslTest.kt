package ru.itis.masterbi.dsl

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.print.print
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.itis.masterbi.builders.ElementBuilders.histogram
import ru.itis.masterbi.builders.ElementBuilders.lineChart
import ru.itis.masterbi.model.Histogram
import ru.itis.masterbi.model.LineChart
import ru.itis.masterbi.model.Scale

class DslTest {

    @Test
    fun `test dashboard dsl`() {
        val dashboardName = "Foo"
        val x = Scale("x")
        val y = Scale("y")


        val dashboard = dashboard {
            name = dashboardName
            row {
                histogram {
                    name = "foo"
                    abscissa = x
                    ordinate = y
                }
                lineChart {
                    name = "bar"
                    abscissa = x
                    ordinate = y
                }

            }
        }

        dashboard.name shouldBe dashboardName
        dashboard.grid shouldHaveSize 1
        val row = dashboard.grid.first()
        row shouldHaveSize 2
        val histogram = row.first() as Histogram
        with(histogram) {
            name shouldBe "foo"
            abscissa shouldBe x
            ordinate shouldBe y
        }
        val lineChart = row[1] as LineChart
        with(lineChart) {
            name shouldBe "bar"
            abscissa shouldBe x
            ordinate shouldBe y
        }
        val objectMapper = ObjectMapper()
        val dashboardJson = objectMapper.writeValueAsString(dashboard)
        dashboardJson.print()
    }
}