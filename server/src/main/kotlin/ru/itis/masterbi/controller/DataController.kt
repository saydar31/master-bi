package ru.itis.masterbi.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itis.masterbi.dto.QueryRequest
import ru.itis.masterbi.dto.QueryResponse
import ru.itis.masterbi.service.QueryGateway

@RestController
@RequestMapping("/api/v1/data")
class DataController(
    val queryGateway: QueryGateway
) {

    @PostMapping
    fun getData(@RequestBody queryRequest: QueryRequest): QueryResponse {
        return queryGateway.executeAll(queryRequest)
    }
}