package ru.itis.masterbi.dto

import ru.itis.masterbi.service.queryexecution.QueryResult

data class QueryResponse(
    val idToResult: Map<String, QueryResult>
)
