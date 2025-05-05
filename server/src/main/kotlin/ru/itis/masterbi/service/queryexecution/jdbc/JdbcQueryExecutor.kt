package ru.itis.masterbi.service.queryexecution.jdbc

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.stereotype.Component
import ru.itis.masterbi.model.DatasourceType
import ru.itis.masterbi.model.FilteredQuery
import ru.itis.masterbi.model.JdbcDatasource
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.exception.DatasourceTypeMismatch
import ru.itis.masterbi.service.queryexecution.QueryExecutor
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.QueryResultUnit
import javax.sql.DataSource

@Component
class JdbcQueryExecutor : QueryExecutor {
    override val datasourceType: DatasourceType
        get() = DatasourceType.JDBC

    override fun execute(query: Query): QueryResult {
        val datasource = query.collection.datasource
        if (datasource !is JdbcDatasource) {
            throw DatasourceTypeMismatch("Datasource type should be ${JdbcDatasource::class.simpleName}")
        }

        val dataSource = createDataSource(datasource)
        val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)

        // Base query
        var sql = "SELECT ${query.key.name} as key, ${query.value.name} as value FROM ${query.collection.name}"

        // Apply filter if present
        val translator = JdbcConditionTranslator()
        val (whereClause, params) = if (query is FilteredQuery) {
            translator.translate(query.condition).let {
                " WHERE ${it.sql}" to it.params
            }
        } else {
            "" to emptyMap()
        }

        sql += whereClause

        // Execute query
        val results = mutableListOf<QueryResultUnit>()
        jdbcTemplate.query(sql, params) { rs, _ ->
            results.add(
                QueryResultUnit(
                    key = rs.getString("key"),
                    value = rs.getString("value")
                )
            )
        }

        return QueryResult(results)
    }

    private fun createDataSource(datasource: JdbcDatasource): DataSource {
        return DriverManagerDataSource().apply {
            setDriverClassName(datasource.dataBase.driverClassName)
            url = datasource.jdbcUrl
            username = datasource.username
            password = datasource.password
        }
    }
}