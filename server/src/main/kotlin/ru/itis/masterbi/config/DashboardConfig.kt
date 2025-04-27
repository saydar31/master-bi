package ru.itis.masterbi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.Dashboard
import ru.itis.masterbi.model.Scale
import ru.itis.masterbi.model.ValueType.NUMBER
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.csv
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.query
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