package ru.itis.masterbi.dsl

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.assertions.print.print
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import ru.itis.masterbi.model.Histogram
import ru.itis.masterbi.model.LineChart
import ru.itis.masterbi.model.Scale
import ru.itis.masterbi.model.SimpleQuery
import ru.itis.masterbi.model.ValueType.NUMBER
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.csv
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.histogram
import ru.itis.masterbi.model.build.ElementBuilders.lineChart

class DslTest {

    @Test
    fun `test dashboard dsl`() {
        val dashboardName = "Foo"
        val x = Scale("x")
        val y = Scale("y")

        val keyX = key {
            name = "x"
            type = NUMBER
        }

        val keyY = key {
            name = "y"
            type = NUMBER
        }

        val csvDatasource = csv {
            literal(
                """
                x;y
                1;2
                3;4
            """
            )
        }
        val defaultCollection = collection {
            name = "DEFAULT"
            datasource = csvDatasource
        }

        val dashboard = dashboard {
            name = dashboardName
            row {
                +histogram {
                    name = "foo"
                    abscissa = x
                    ordinate = y
                    +query {
                        key = keyX
                        value = keyY
                        collection = defaultCollection
                    }
                }
                +lineChart {
                    name = "bar"
                    abscissa = x
                    ordinate = y
                    +query {
                        key = keyX
                        value = keyY
                        collection = defaultCollection
                        visualizationProps {
                            color = "FF00AA"
                            forKey(1) {
                                color = "AABBCC"
                            }
                        }
                    }
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

            val query = queries.first() as SimpleQuery
            query.key shouldBe keyX
            query.value shouldBe keyY

            with(query.visualizationProps) {
                color shouldBe "FF00AA"
                keyProps["1"].shouldNotBeNull {
                    color shouldBe "AABBCC"
                }
            }
        }
        val objectMapper = ObjectMapper()
        val dashboardJson = objectMapper.writeValueAsString(dashboard)
        dashboardJson.print()
    }
}