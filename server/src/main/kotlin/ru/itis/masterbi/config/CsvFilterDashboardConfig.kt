package ru.itis.masterbi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.SimpleKeyDescription
import ru.itis.masterbi.model.ValueType
import ru.itis.masterbi.model.ValueType.NUMBER
import ru.itis.masterbi.model.ValueType.STRING
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.csv
import ru.itis.masterbi.model.build.DataBuilders.filter
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.histogram
import ru.itis.masterbi.model.condition.builder.eq
import ru.itis.masterbi.model.condition.builder.or
import ru.itis.masterbi.model.linear
import java.time.Month
import kotlin.random.Random

@Configuration
class CsvFilterDashboardConfig {

    @Bean
    fun filteredCsvDashboard() = dashboard {
        name = "csv-filter"
        row {
            +histogram {
                name = "months"
                abscissa = linear("month")
                ordinate = linear("count")

                val monthKey = SimpleKeyDescription("month", STRING)

                +query {
                    key = monthKey
                    NUMBER value "count"

                    collection = collection {
                        name = "default"
                        datasource = csv {
                            literal(buildString {
                                appendLine("month;count")
                                Month.entries.forEach {
                                    val count = Random.nextInt(100, 200)
                                    appendLine("$it;$count")
                                }
                            })
                        }
                    }
                }.filter {
                    or {
                        +(monthKey eq Month.SEPTEMBER.name)
                        +(monthKey eq Month.MARCH)
                    }
                }
            }
        }
    }
}