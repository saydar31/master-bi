package ru.itis.masterbi.controller

import org.springframework.web.bind.annotation.*
import ru.itis.masterbi.dto.QueryRequest
import ru.itis.masterbi.dto.QueryResponse
import ru.itis.masterbi.service.QueryGateway

@RestController
@RequestMapping("/api/v1/data")
@CrossOrigin(origins = ["http://localhost:3000"])
class DataController(
    val queryGateway: QueryGateway
) {

    @PostMapping
    fun getData(@RequestBody queryRequest: QueryRequest): QueryResponse {
        return queryGateway.executeAll(queryRequest)
    }
}