package ru.itis.masterbi.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.itis.masterbi.model.Dashboard

@RestController
@RequestMapping("/api/v1/dashboards")
@CrossOrigin(origins = ["http://localhost:3000"])
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