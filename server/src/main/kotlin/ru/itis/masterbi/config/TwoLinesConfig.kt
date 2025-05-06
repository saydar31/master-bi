package ru.itis.masterbi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.config.TwoLinesConfig.Datasources.DEFAULT_COLLECTION
import ru.itis.masterbi.config.TwoLinesConfig.Datasources.paraboloid
import ru.itis.masterbi.config.datasource.YearApplies
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.ValueType
import ru.itis.masterbi.model.ValueType.*
import ru.itis.masterbi.model.build.DataBuilders
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.csv
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.activityGraph
import ru.itis.masterbi.model.build.ElementBuilders.lineChart
import ru.itis.masterbi.model.linear

@Configuration
class TwoLinesConfig {

    object Datasources {
        val squareRoot = csv {
            separator = ','
            literal(buildString {
                append("x,y\n")
                generateSequence(0) { it + 1 }
                    .take(20)
                    .forEach {
                        append(it, ',', it * 2, '\n')
                    }
            })
        }

        val paraboloid = csv {
            separator = ','
            literal(buildString {
                append("x,y\n")
                generateSequence(0) { it + 1 }
                    .take(20)
                    .forEach {
                        append(it, ',', (it * 2.5).toInt(), '\n')
                    }
            })
        }

        const val DEFAULT_COLLECTION = "DEFAULT"
    }

    object keys {
        val x = key {
            name = "x"
            type = NUMBER
        }

        val y = key {
            name = "y"
            type = NUMBER
        }
    }

    @Bean
    fun twoLinesDashboard() = dashboard {
        name = "two-lines"
        row {
            +lineChart {
                name = "Square root"
                abscissa = linear("x", displayName = "foo")
                ordinate = linear("y", displayName = "bar")
                +query {
                    label = "sqrt"
                    collection = collection {
                        name = DEFAULT_COLLECTION
                        datasource = Datasources.squareRoot
                        key = keys.x
                        value = keys.y
                    }
                    visualizationProps {
                        color {
                            red = 164
                            green = 180
                            blue = 101
                        }

                        backgroundColor {
                            red = 98
                            green = 111
                            blue = 71
                        }
                    }
                }
                +query {
                    label = "paraboloid"
                    collection = collection {
                        name = DEFAULT_COLLECTION
                        datasource = paraboloid
                        key = keys.x
                        value = keys.y
                    }
                }
            }
        }
        row {
            +activityGraph {
                name = "foo"
                +query {
                    collection = collection {
                        name = "default"
                        key = key {
                            name = "day"
                            type = TIMESTAMP
                        }
                        value = key {
                            name = "amount"
                            type = NUMBER
                        }
                        datasource = YearApplies.applications
                    }
                }
            }
        }
    }

}