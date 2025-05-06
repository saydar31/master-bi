package ru.itis.masterbi.service.queryexecution

import org.springframework.stereotype.Service
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.queryexecution.transformer.QueryResultTransformer

@Service
class ResultTransformerDispatcher(
    private val transformers: List<QueryResultTransformer>
) {
    fun getTransformed(query: Query, queryResult: QueryResult): QueryResult {
        return transformers.find { it.isSuitable(query) }
            ?.getTransformed(queryResult)
            ?: queryResult
    }
}