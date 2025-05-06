package ru.itis.masterbi.service

import org.springframework.stereotype.Service
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.exception.DatasourceTypeNotSupportedException
import ru.itis.masterbi.service.queryexecution.QueryExecutor
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.ResultTransformerDispatcher

@Service
class QueryDispatcher(
    executors: List<QueryExecutor>,
    val resultTransformerDispatcher: ResultTransformerDispatcher
) {
    val typeToExecutor = executors.associateBy { it.datasourceType }

    fun execute(query: Query): QueryResult {
        val type = query.collection.datasource.type
        val queryExecutor = typeToExecutor[type] ?: throw DatasourceTypeNotSupportedException()
        return queryExecutor.execute(query).let {
            resultTransformerDispatcher.getTransformed(query, it)
        }
    }
}