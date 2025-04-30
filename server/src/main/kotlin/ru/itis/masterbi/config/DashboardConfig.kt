package ru.itis.masterbi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.Dashboard
import ru.itis.masterbi.model.JdbcDatasource.DataBase.POSTGRESQL
import ru.itis.masterbi.model.Scale
import ru.itis.masterbi.model.ValueType.NUMBER
import ru.itis.masterbi.model.ValueType.STRING
import ru.itis.masterbi.model.VisualizationProps.LineType.DOTTED
import ru.itis.masterbi.model.VisualizationProps.MarkerShape.CIRCLE
import ru.itis.masterbi.model.VisualizationProps.MarkerShape.TRIANGLE
import ru.itis.masterbi.model.build.DataBuilders
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.csv
import ru.itis.masterbi.model.build.DataBuilders.jdbc
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.donutChart
import ru.itis.masterbi.model.build.ElementBuilders.histogram
import ru.itis.masterbi.model.build.ElementBuilders.lineChart
import ru.itis.masterbi.model.build.ElementBuilders.pieChart
import ru.itis.masterbi.model.build.ElementBuilders.tagCloud

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

    val salesDb = jdbc {
        jdbcUrl = "jdbc:postgresql://localhost:5432/myapp"
        username = "postgres"
        password = "mysecretpassword"
        database = POSTGRESQL
    }

    @Bean
    fun salesDashboard() = dashboard {
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
                        datasource = salesDb
                    }
                    STRING key "day_of_week"
                    NUMBER value "sales_count"
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
                name = "Successful Offers Percentage"
                abscissa = Scale("hour")
                ordinate = Scale("success_percent")

                +query {
                    collection = collection {
                        name = "call_metrics"
                        label = "Успешных звонков, %"
                        datasource = salesDb
                    }
                    STRING key "hour"
                    NUMBER value "success_percent"
                    visualizationProps {
                        color = "#2196F3"
                        shape = TRIANGLE
                    }
                }
                +query {
                    collection = collection {
                        name = "email_metrics"
                        label = "Успешных email"
                        datasource = csv {
                            file("email_metrics.csv")
                        }
                    }
                    STRING key "hour"
                    NUMBER value "success_percent"
                    visualizationProps {
                        color = "#D6AE01"
                        shape = CIRCLE
                    }
                }
            }
        }

        // Second row with donut chart and tag cloud
        row {
            +donutChart {
                name = "B2B vs B2C Distribution"

                +query {
                    collection = collection {
                        name = "customer_types"
                        datasource = salesDb
                    }
                    key = key {
                        name = "customer_type"
                        type = STRING
                    }
                    value = key {
                        name = "percentage"
                        type = NUMBER
                    }
                }
            }

            +tagCloud {
                name = "Feedback"
                +query {
                    STRING key "option"
                    NUMBER value "count"
                    collection = collection {
                        name = "feedback"
                        datasource = salesDb
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