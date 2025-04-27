package ru.itis.masterbi.service

import org.springframework.stereotype.Service
import ru.itis.masterbi.dto.QueryRequest
import ru.itis.masterbi.dto.QueryResponse
import ru.itis.masterbi.model.Dashboard

@Service
class QueryGateway(
    dashboards: List<Dashboard>,
    val queryDispatcher: QueryDispatcher
) {

    val idToQuery = dashboards.flatMap { it.grid }
        .flatten()
        .flatMap { it.queries }
        .associateBy { it.id }

    fun executeAll(queryRequest: QueryRequest): QueryResponse {
        val idToResult = queryRequest.queryIds.mapNotNull {
            idToQuery[it]
        }.associate {
            val result = queryDispatcher.execute(it)
            it.id to result
        }
        return QueryResponse(idToResult)
    }

}