package ru.itis.masterbi.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.ValueType
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.mongo
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.histogram
import ru.itis.masterbi.model.linear

@Configuration
@ConditionalOnProperty("dashboard.mongo")
class MongoDashboardConfig {

    companion object {
        val mongo = mongo {
            host = "localhost"
            database = "smart-city"
            username = "admin"
            password = "mysecretpassword"
        }


        val dow = key {
            name = "dow"
            type = ValueType.STRING
        }
        val count = key {
            name = "count"
            type = ValueType.NUMBER
        }

    }

    @Bean
    fun mongoDashboard() = dashboard {
        name = "mongo"
        row {
            +histogram {
                name = "Accidents per day"
                abscissa = linear("dow")
                ordinate = linear("count")
                +query {
                    key = dow
                    value = count
                    collection = collection {
                        datasource = mongo
                        name = "accidents"
                    }
                }
            }
        }
    }
}