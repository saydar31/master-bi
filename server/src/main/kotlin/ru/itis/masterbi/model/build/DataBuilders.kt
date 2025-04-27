package ru.itis.masterbi.model.build

import ru.itis.masterbi.model.*
import ru.itis.masterbi.model.Collection as MBICollection

object DataBuilders{
    // Datasource Builders
    fun csv(init: CsvDatasourceBuilder.() -> Unit): CsvDatasource {
        val builder = CsvDatasourceBuilder()
        builder.init()
        return builder.build()
    }

    class CsvDatasourceBuilder {
        lateinit var value: String
        lateinit var valueType: CsvDatasource.DataLocationType
        var separator: Char = ';'
        var nullPlaceholder: String = "<NULL>"

        fun build(): CsvDatasource {
            validate()
            return CsvDatasource(value, valueType, separator, nullPlaceholder)
        }

        private fun validate() {
            require(::value.isInitialized) { "Value must be set" }
            require(::valueType.isInitialized) { "ValueType must be set" }
        }
    }

    // Collection Builder
    fun collection(init: CollectionBuilder.() -> Unit): MBICollection {
        val builder = CollectionBuilder()
        builder.init()
        return builder.build()
    }

    class CollectionBuilder {
        lateinit var name: String
        lateinit var datasource: Datasource

        fun build(): MBICollection {
            validate()
            return SimpleCollection(name, datasource)
        }

        private fun validate() {
            require(::name.isInitialized) { "Collection name must be set" }
            require(::datasource.isInitialized) { "Datasource must be set" }
        }
    }

    // KeyDescription Builder
    fun key(init: KeyDescriptionBuilder.() -> Unit): KeyDescription {
        val builder = KeyDescriptionBuilder()
        builder.init()
        return builder.build()
    }

    class KeyDescriptionBuilder {
        lateinit var name: String
        lateinit var type: ValueType

        fun build(): KeyDescription {
            validate()
            return SimpleKeyDescription(name, type)
        }

        private fun validate() {
            require(::name.isInitialized) { "Key name must be set" }
            require(::type.isInitialized) { "Key type must be set" }
        }
    }

    // Query Builder
    fun query(init: QueryBuilder.() -> Unit): Query {
        val builder = QueryBuilder()
        builder.init()
        return builder.build()
    }

    class QueryBuilder {
        lateinit var collection: MBICollection
        lateinit var key: KeyDescription
        lateinit var value: KeyDescription

        fun build(): Query {
            validate()
            return SimpleQuery(collection, key, value)
        }

        private fun validate() {
            require(::collection.isInitialized) { "Collection must be set" }
            require(::key.isInitialized) { "Key must be set" }
            require(::value.isInitialized) { "Value must be set" }
        }
    }
}
