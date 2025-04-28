package ru.itis.masterbi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.*
import ru.itis.masterbi.model.ScaleType.LINEAR
import ru.itis.masterbi.model.ValueType.NUMBER
import ru.itis.masterbi.model.VisualizationProps.LineType.DOTTED
import ru.itis.masterbi.model.VisualizationProps.MarkerShape.CIRCLE
import ru.itis.masterbi.model.VisualizationProps.MarkerShape.TRIANGLE
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.csv
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.donutChart
import ru.itis.masterbi.model.build.ElementBuilders.histogram
import ru.itis.masterbi.model.build.ElementBuilders.lineChart
import ru.itis.masterbi.model.build.ElementBuilders.pieChart

@Configuration
class DashboardConfig {

    companion object {
        val x = Scale("x")
        val y = Scale("y")
    }

    @Bean
    fun fooDashboard(): Dashboard {
        val dashboardName = "Foo"

        return dashboard {
            name = dashboardName
            row {
                +histogram {
                    name = "foo"
                    abscissa = x
                    ordinate = y
                }
                +lineChart {
                    name = "bar"
                    abscissa = x
                    ordinate = y
                }

            }
            row {
                +pieChart {
                    name = "pie"
                }
            }
        }
    }

    @Bean
    fun salesDashboard(): Dashboard {
        return dashboard {
            name = "Sales"

            // First row with histogram and line chart
            row {
                +histogram {
                    name = "Sales by Day of Week"
                    abscissa = Scale("day_of_week")
                    ordinate = Scale("sales_count")

                    +query {
                        collection = collection {
                            name = "sales_by_day"
                            label = "Продано в день"
                            datasource = csv {
                                literal(
                                    """
                                        day_of_week,sales_count
                                        Monday,42
                                        Tuesday,56
                                        Wednesday,38
                                        Thursday,61
                                        Friday,89
                                        Saturday,24
                                        Sunday,15
                                    """.trimIndent()
                                )
                                separator = ','
                            }
                        }
                        key = key {
                            name = "day_of_week"
                            type = ValueType.STRING
                        }
                        value = key {
                            name = "sales_count"
                            type = NUMBER
                        }
                        visualizationProps {
                            color {
                                red = 76
                                green = 175
                                blue = 80
                            }
                        }
                    }
                }

                +lineChart {
                    name = "Successful Calls Percentage"
                    abscissa = Scale("hour")
                    ordinate = Scale("success_percent", LINEAR)

                    +query {
                        collection = collection {
                            name = "call_metrics"
                            label = "Успешных звонков, %"
                            datasource = csv {
                                literal(
                                    """
                                    hour,success_percent
                                    9,62.5
                                    10,68.3
                                    11,71.2
                                    12,65.7
                                    13,70.1
                                    14,73.8
                                    15,75.4
                                    16,72.9
                                    17,68.7
                                """.trimIndent()
                                )
                                separator = ','
                            }
                        }
                        key = key {
                            name = "hour"
                            type = ValueType.STRING
                        }
                        value = key {
                            name = "success_percent"
                            type = NUMBER
                        }
                        visualizationProps {
                            color = "#2196F3"
                            shape = TRIANGLE
                            lineType = DOTTED
                        }
                    }
                }
            }

            // Second row with donut chart
            row {
                +donutChart {
                    name = "B2B vs B2C Distribution"

                    +query {
                        collection = collection {
                            name = "customer_types"
                            datasource = csv {
                                literal(
                                    """
                                        customer_type,percentage
                                        B2B,65.2
                                        B2C,34.8
                                    """.trimIndent()
                                )
                                separator = ','
                            }
                        }
                        key = key {
                            name = "customer_type"
                            type = ValueType.STRING
                        }
                        value = key {
                            name = "percentage"
                            type = NUMBER
                        }
                    }
                }
            }
        }
    }

    @Bean
    fun barDashboard(): Dashboard {
        return dashboard {
            name = "Bar"
            row {
                +histogram {
                    name = "bars"
                    abscissa = x
                    ordinate = y
                    +query {
                        collection = collection {
                            name = "Default"
                            datasource = csv {
                                separator = ','
                                val content = generateSequence(0) { it + 1 }
                                    .take(20)
                                    .map { "$it,${it * 2}" }
                                    .joinToString(separator = "\n", prefix = "x,y\n")
                                literal(content)
                            }
                        }
                        key = key {
                            name = "x"
                            type = NUMBER
                        }
                        value = key {
                            name = "y"
                            type = NUMBER
                        }
                    }
                }
            }
        }
    }
}