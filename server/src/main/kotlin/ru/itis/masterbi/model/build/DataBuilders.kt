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

    // Добавляем рядом с csv()
    fun jdbc(init: JdbcDatasourceBuilder.() -> Unit): JdbcDatasource {
        val builder = JdbcDatasourceBuilder()
        builder.init()
        return builder.build()
    }

    class JdbcDatasourceBuilder {
        lateinit var jdbcUrl: String
        lateinit var username: String
        lateinit var password: String
        lateinit var database: JdbcDatasource.DataBase // значение по умолчанию

        fun build(): JdbcDatasource {
            validate()
            return JdbcDatasource(jdbcUrl, username, password, database)
        }

        private fun validate() {
            require(::database.isInitialized) { "Database must be set" }
            require(::jdbcUrl.isInitialized) { "JDBC URL must be set" }
            require(::username.isInitialized) { "Username must be set" }
            require(::password.isInitialized) { "Password must be set" }
        }
    }

    // mongo
    fun mongo(init: MongoDatasourceBuilder.() -> Unit): MongoDatasource {
        val builder = MongoDatasourceBuilder()
        builder.init()
        return builder.build()
    }

    class MongoDatasourceBuilder {
        var host: String = "localhost"
        var port: Int = 27017
        var database: String = ""
        var username: String? = null
        var password: String? = null
        var authSource: String? = "admin"
        var authMechanism: String? = null
        var directConnection: Boolean = true

        fun host(host: String) = apply { this.host = host }
        fun port(port: Int) = apply { this.port = port }
        fun database(database: String) = apply { this.database = database }
        fun credentials(username: String, password: String) = apply {
            this.username = username
            this.password = password
        }

        fun authSource(source: String) = apply { this.authSource = source }
        fun authMechanism(mechanism: String) = apply { this.authMechanism = mechanism }
        fun directConnection(enable: Boolean) = apply { this.directConnection = enable }

        fun build(): MongoDatasource {
            validate()
            return MongoDatasource(
                host = host,
                port = port,
                database = database,
                username = username,
                password = password,
                authSource = authSource,
                authMechanism = authMechanism,
                directConnection = directConnection
            )
        }

        private fun validate() {
            require(database.isNotBlank()) { "Database name must be specified" }
            require(port in 1..65535) { "Port must be between 1 and 65535" }
            if (authMechanism != null) {
                require(username != null) { "Username must be specified when authMechanism is set" }
            }
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

        data class Color(
            var red: Int = 75,
            var green: Int = 192,
            var blue: Int = 192,
            var alpha: Int = 1
        ) {
            fun toChartColor() = "rgba($red, $green, $blue, $alpha)"
        }

        fun VisualizationProps.color(init: Color.() -> Unit) {
            val color = Color()
            init(color)
            this.color = color.toChartColor()
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
