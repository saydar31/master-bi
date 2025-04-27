package ru.itis.masterbi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.builders.ElementBuilders.histogram
import ru.itis.masterbi.builders.ElementBuilders.lineChart
import ru.itis.masterbi.builders.ElementBuilders.pieChart
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.Dashboard
import ru.itis.masterbi.model.Scale

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
                histogram {
                    name = "bars"
                    abscissa = x
                    ordinate = y
                }
            }
        }
    }
}