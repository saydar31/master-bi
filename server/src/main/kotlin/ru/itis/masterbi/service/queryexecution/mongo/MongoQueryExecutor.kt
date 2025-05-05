package ru.itis.masterbi.service.queryexecution.mongo

import com.mongodb.client.FindIterable
import com.mongodb.client.MongoClients
import org.springframework.stereotype.Component
import ru.itis.masterbi.model.DatasourceType
import ru.itis.masterbi.model.FilteredQuery
import ru.itis.masterbi.model.MongoDatasource
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.exception.DatasourceTypeMismatch
import ru.itis.masterbi.service.queryexecution.QueryExecutor
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.QueryResultUnit

@Component
class MongoQueryExecutor : QueryExecutor {

    override val datasourceType: DatasourceType = DatasourceType.MONGO

    private val translator = MongoConditionTranslator()

    override fun execute(query: Query): QueryResult {
        val datasource = query.collection.datasource
        if (datasource !is MongoDatasource) {
            throw DatasourceTypeMismatch("Mongo datasource expected")
        }

        val client = MongoClients.create(datasource.connectionString)
        val database = client.getDatabase(datasource.database)
        val collection = database.getCollection(query.collection.name)

        // Получаем фильтр, если есть
        val mongoFilter = if (query is FilteredQuery) translator.translate(query.condition) else null

        // Выполняем запрос с фильтром
        val results = mutableListOf<QueryResultUnit>()

        try {
            var iterable: FindIterable<org.bson.Document> = collection.find()
            if (mongoFilter != null) {
                iterable = iterable.filter(mongoFilter)
            }

            iterable.forEach { doc ->
                val key = doc[query.key.name]?.toString() ?: ""
                val value = doc[query.value.name]?.toString()
                results.add(QueryResultUnit(key, value))
            }

            return QueryResult(results)
        } catch (e: Exception) {
            throw RuntimeException("MongoDB query failed", e)
        } finally {
            client.close()
        }
    }
}