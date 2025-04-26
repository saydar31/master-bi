package ru.itis.masterbi.service.queryexecution.csv

import ru.itis.masterbi.model.CsvDatasource
import java.io.File
import java.io.InputStream
import java.net.URL

// Common interface
interface CsvLocationResolver {
    fun getInputStream(datasource: CsvDatasource): InputStream
}

// File implementation
class FileCsvLocationResolver : CsvLocationResolver {
    override fun getInputStream(datasource: CsvDatasource): InputStream {
        require(datasource.valueType == CsvDatasource.DataLocationType.FILENAME) {
            "This resolver only handles FILENAME type"
        }
        return File(datasource.value).inputStream()
    }
}

// URL implementation
class UrlCsvLocationResolver : CsvLocationResolver {
    override fun getInputStream(datasource: CsvDatasource): InputStream {
        require(datasource.valueType == CsvDatasource.DataLocationType.URL) {
            "This resolver only handles URL type"
        }
        return URL(datasource.value).openStream()
    }
}

// Literal implementation
class LiteralCsvLocationResolver : CsvLocationResolver {
    override fun getInputStream(datasource: CsvDatasource): InputStream {
        require(datasource.valueType == CsvDatasource.DataLocationType.LITERAL) {
            "This resolver only handles LITERAL type"
        }
        val sourceString = datasource.value.trimIndent()
        return sourceString.byteInputStream(Charsets.UTF_8)
    }
}

// Factory to get the appropriate resolver
object CsvLocationResolverFactory {
    fun getResolver(datasource: CsvDatasource): CsvLocationResolver {
        return when (datasource.valueType) {
            CsvDatasource.DataLocationType.FILENAME -> FileCsvLocationResolver()
            CsvDatasource.DataLocationType.URL -> UrlCsvLocationResolver()
            CsvDatasource.DataLocationType.LITERAL -> LiteralCsvLocationResolver()
        }
    }
}
