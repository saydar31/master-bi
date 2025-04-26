package ru.itis.masterbi.dsl

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.print.print
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.itis.masterbi.model.*
import ru.itis.masterbi.model.CsvDatasource.DataLocationType.LITERAL
import ru.itis.masterbi.model.ValueType.NUMBER

class DslTest {

    @Test
    fun `test dashboard dsl`() {
        val dashboardName = "Foo"
        val x = Scale("x")
        val y = Scale("y")
        val csvDatasource = CsvDatasource().apply {
            value = """
                a,b
                1,100
                2,200
            """.trimIndent()
            valueType = LITERAL
            separator = ','
        }
        val collection = SimpleCollection().apply {
            datasource = csvDatasource
            name = "foo"
        }

        val dashboard = dashboard {
            name = dashboardName
            row {
                append<Histogram> {
                    name = "foo"
                    abscissa = x
                    ordinate = y
                    queries.add(SimpleQuery().apply {
                        this.collection = collection
                        key = SimpleKeyDescription().apply {
                            name = "a"
                            type = NUMBER
                        }
                        value = SimpleKeyDescription().apply {
                            name = "b"
                            type = NUMBER
                        }
                    })
                }
                append<LineChart> {
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