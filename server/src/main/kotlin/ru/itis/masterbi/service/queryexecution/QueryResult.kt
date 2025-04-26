package ru.itis.masterbi.service.queryexecution

data class QueryResultUnit(
    val key: String,
    val value: String?
)

data class QueryResult(
    val data: List<QueryResultUnit>
)
