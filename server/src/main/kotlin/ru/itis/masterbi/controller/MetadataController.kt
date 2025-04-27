package ru.itis.masterbi.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.itis.masterbi.model.Dashboard

@RestController
@RequestMapping("/api/v1/dashboards")
class MetadataController(
    dashboards: List<Dashboard>
) {
    val nameToDashboard = dashboards.associateBy { it.name }

    @GetMapping("/{name}")
    fun getDashboard(@PathVariable name: String): ResponseEntity<Dashboard> {
        return nameToDashboard[name]
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }
}