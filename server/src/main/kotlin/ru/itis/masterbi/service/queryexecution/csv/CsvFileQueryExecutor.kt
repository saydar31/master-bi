package ru.itis.masterbi.service.queryexecution.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import ru.itis.masterbi.model.CsvDatasource
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.exception.DatasourceTypeMismatch
import ru.itis.masterbi.service.queryexecution.QueryExecutor
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.QueryResultUnit
import java.io.InputStreamReader
import java.net.URL

class CsvFileQueryExecutor : QueryExecutor {

    override fun execute(query: Query): QueryResult {
        val datasource = query.collection.datasource
        if (datasource !is CsvDatasource) {
            throw DatasourceTypeMismatch("Datasource type should be ${CsvDatasource::class.simpleName}")
        }
        val format = CSVFormat.DEFAULT
            .builder()
            .setDelimiter(datasource.separator.first())
            .setNullString(datasource.nullPlacement)
            .setHeader()
            .setSkipHeaderRecord(true)
            .get()
        val results = mutableListOf<QueryResultUnit>()

        val resolver = CsvLocationResolverFactory.getResolver(datasource)
        val input = resolver.getInputStream(datasource)

        input.use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                CSVParser.parse(reader, format).use { parser ->
                    parser.forEach { record ->
                        results.add(
                            QueryResultUnit(
                                key = record.get(query.key.name),
                                value = record.get(query.value.name)
                            )
                        )
                    }
                }
            }
        }

        return QueryResult(results)
    }
}