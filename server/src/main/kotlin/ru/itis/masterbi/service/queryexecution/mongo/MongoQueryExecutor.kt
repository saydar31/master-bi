package ru.itis.masterbi.service.queryexecution.mongo

import com.mongodb.client.MongoClients
import org.springframework.stereotype.Component
import ru.itis.masterbi.model.DatasourceType
import ru.itis.masterbi.model.MongoDatasource
import ru.itis.masterbi.model.Query
import ru.itis.masterbi.service.exception.DatasourceTypeMismatch
import ru.itis.masterbi.service.queryexecution.QueryExecutor
import ru.itis.masterbi.service.queryexecution.QueryResult
import ru.itis.masterbi.service.queryexecution.QueryResultUnit

@Component
class MongoQueryExecutor : QueryExecutor {

    override val datasourceType: DatasourceType = DatasourceType.MONGO

    override fun execute(query: Query): QueryResult {
        val datasource = query.collection.datasource
        if (datasource !is MongoDatasource){
            throw DatasourceTypeMismatch("Mongo datasource expected")
        }

        return try {
            // Ленивая инициализация клиента
            val mongoClient = MongoClients.create(datasource.connectionString)
            val database = mongoClient.getDatabase(datasource.database)
            val collection = database.getCollection(query.collection.name)

            val results = mutableListOf<QueryResultUnit>()
            collection.find().forEach { doc ->
                results.add(
                    QueryResultUnit(
                        key = doc[query.key.name]?.toString() ?: "",
                        value = doc[query.value.name]?.toString()
                    )
                )
            }

            QueryResult(results)
        } catch (e: Exception) {
            throw RuntimeException("MongoDB query failed", e)
        }
    }
}