package ru.itis.masterbi.model.build

import ru.itis.masterbi.model.*
import ru.itis.masterbi.model.CsvDatasource.DataLocationType.LITERAL
import java.time.LocalDateTime
import java.util.UUID
import ru.itis.masterbi.model.Collection as MBICollection

object DataBuilders {
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

        fun literal(literal: String) {
            value = literal.trimIndent()
            valueType = LITERAL
        }

        fun file(path: String) {
            value = path
            valueType = CsvDatasource.DataLocationType.FILENAME
        }

        fun url(url: String) {
            value = url
            valueType = CsvDatasource.DataLocationType.URL
        }

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
        var label: String? = null
        lateinit var collection: MBICollection
        lateinit var key: KeyDescription
        lateinit var value: KeyDescription
        var visualizationProps: VisualizationProps = VisualizationProps()

        fun visualizationProps(init: VisualizationProps.() -> Unit) {
            init(visualizationProps)
        }

        fun VisualizationProps.forKey(key: String, init: VisualizationProps.() -> Unit) {
            val props = VisualizationProps()
            init(props)
            this.keyProps[key] = props
        }

        fun VisualizationProps.forKey(key: Int, init: VisualizationProps.() -> Unit) {
            val props = VisualizationProps()
            init(props)
            this.keyProps[key.toString()] = props
        }

        fun VisualizationProps.forKey(key: LocalDateTime, init: VisualizationProps.() -> Unit) {
            val props = VisualizationProps()
            init(props)
            this.keyProps[key.toString()] = props
        }

        fun build(): Query {
            validate()
            val id = UUID.randomUUID().toString()
            return SimpleQuery(id, label, collection, key, value, visualizationProps)
        }

        private fun validate() {
            require(::collection.isInitialized) { "Collection must be set" }
            require(::key.isInitialized) { "Key must be set" }
            require(::value.isInitialized) { "Value must be set" }
        }
    }
}
