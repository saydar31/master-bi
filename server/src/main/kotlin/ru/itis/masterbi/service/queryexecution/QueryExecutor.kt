package ru.itis.masterbi.service.queryexecution

import ru.itis.masterbi.model.Query

interface QueryExecutor {
    fun execute(query: Query): QueryResult
}