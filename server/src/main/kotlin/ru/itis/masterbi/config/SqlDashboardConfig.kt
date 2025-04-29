package ru.itis.masterbi.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.itis.masterbi.dsl.dashboard
import ru.itis.masterbi.model.Dashboard
import ru.itis.masterbi.model.JdbcDatasource.DataBase.POSTGRESQL
import ru.itis.masterbi.model.Scale
import ru.itis.masterbi.model.ValueType
import ru.itis.masterbi.model.build.DataBuilders.collection
import ru.itis.masterbi.model.build.DataBuilders.jdbc
import ru.itis.masterbi.model.build.DataBuilders.key
import ru.itis.masterbi.model.build.DataBuilders.query
import ru.itis.masterbi.model.build.ElementBuilders.histogram

@Configuration
@ConditionalOnProperty("dashboard.sql")
class SqlDashboardConfig {
    @Bean
    fun sqlDashboard(): Dashboard {
        return dashboard {
            name = "SQL_Dashboard"

            row {
                +histogram {
                    name = "User Registrations by Month"
                    abscissa = Scale("month")
                    ordinate = Scale("count")

                    +query {
                        collection = collection {
                            name = "user_registrations"
                            datasource = jdbc {
                                jdbcUrl = "jdbc:postgresql://localhost:5432/myapp"
                                username = "postgres"
                                password = "mysecretpassword"
                                database = POSTGRESQL
                            }
                        }
                        key = key {
                            name = "month"
                            type = ValueType.STRING
                        }
                        value = key {
                            name = "count"
                            type = ValueType.NUMBER
                        }
                    }
                }
            }
        }
    }
}