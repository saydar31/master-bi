package ru.itis.masterbi.service.queryexecution.csv

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.springframework.stereotype.Component
import ru.itis.masterbi.model.CsvDatasource
import ru.itis.masterbi.model.DatasourceType
import ru.itis.masterbi.model.FilteredQuery
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.exception.DatasourceTypeMismatch
import ru.itis.masterbi.service.queryexecution.QueryExecutor
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.QueryResultUnit
import ru.itis.masterbi.service.queryexecution.csv.filter.CsvFilterQueryService
import java.io.InputStreamReader

@Component
class CsvFileQueryExecutor(
    val filterService: CsvFilterQueryService
) : QueryExecutor {

    override val datasourceType = DatasourceType.CSV

    override fun execute(query: Query): QueryResult {
        val datasource = query.collection.datasource
        if (datasource !is CsvDatasource) {
            throw DatasourceTypeMismatch("Datasource type should be ${CsvDatasource::class.simpleName}")
        }
        val format = CSVFormat.DEFAULT
            .builder()
            .setDelimiter(datasource.separator)
            .setNullString(datasource.nullPlacement)
            .setHeader()
            .setSkipHeaderRecord(true)
            .get()
        val results = mutableListOf<QueryResultUnit>()

        val resolver = CsvLocationResolverFactory.getResolver(datasource)
        val input = resolver.getInputStream(datasource)

        val filter = when (query) {
            is FilteredQuery -> filterService.buildFilter(query.condition)
            else -> {
                { true }
            }
        }

        input.use { inputStream ->
            InputStreamReader(inputStream).use { reader ->
                CSVParser.parse(reader, format).use { parser ->
                    parser.forEach { record ->
                        val value = record.get(query.value.name)
                        if (filter(record)) {
                            results.add(
                                QueryResultUnit(
                                    key = record.get(query.key.name),
                                    value = value
                                )
                            )
                        }
                    }
                }
            }
        }

        return QueryResult(results)
    }
}