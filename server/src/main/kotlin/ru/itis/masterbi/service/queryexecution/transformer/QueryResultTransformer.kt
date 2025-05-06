package ru.itis.masterbi.service.queryexecution.transformer

import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.queryexecution.QueryResult

interface QueryResultTransformer {

    fun isSuitable(query: Query): Boolean
    fun getTransformed(queryResult: QueryResult): QueryResult

}