package ru.itis.masterbi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.itis.masterbi.model.DatasourceType.CSV
import ru.itis.masterbi.model.DatasourceType.MONGO
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

data class CsvDatasource(
    val value: String,
    val valueType: DataLocationType,
    val separator: Char = ';',
    val nullPlacement: String = "<NULL>"
) : Datasource {

    enum class DataLocationType {
        FILENAME, URL, LITERAL
    }

    override val type: DatasourceType
        get() = CSV
}

data class JdbcDatasource(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val dataBase: DataBase
) : Datasource {
    override val type: DatasourceType
        get() = DatasourceType.JDBC

    enum class DataBase(val driverClassName: String) {
        POSTGRESQL("org.postgresql.Driver"),
        MYSQL("com.mysql.cj.jdbc.Driver"),
        MARIADB("org.mariadb.jdbc.Driver"),
        SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
        H2("org.h2.Driver"),
        SQLITE("org.sqlite.JDBC"),
        ORACLE("oracle.jdbc.OracleDriver"),
        CLICKHOUSE("com.clickhouse.jdbc.ClickHouseDriver")
    }

}

data class MongoDatasource(
    val host: String,
    val port: Int = 27017,
    val database: String,
    val username: String? = null,
    val password: String? = null,
    val authSource: String? = "admin",
    val authMechanism: String? = null,
    val directConnection: Boolean = true
) : Datasource {

    override val type = MONGO

    val connectionString = buildString {
        append("mongodb://")

        // Добавляем аутентификацию если есть username
        if (!username.isNullOrBlank()) {
            append(username)
            if (!password.isNullOrBlank()) {
                append(":").append(password.urlEncode())
            }
            append("@")
        }

        append(host)
        if (port != 27017) append(":").append(port)

        // Параметры подключения
        val params = mutableListOf<String>().apply {
            if (directConnection) add("directConnection=true")
            if (!authSource.isNullOrBlank()) add("authSource=$authSource")
            if (!authMechanism.isNullOrBlank()) add("authMechanism=$authMechanism")
        }

        if (params.isNotEmpty()) {
            append("/?").append(params.joinToString("&"))
        }
    }

    private fun String.urlEncode(): String =
        URLEncoder.encode(this, StandardCharsets.UTF_8)
}

class SimpleCollection(
    override val name: String,
    @get:JsonIgnore
    override val datasource: Datasource
) : Collection

class SimpleKeyDescription(
    override val name: String,
    override val type: ValueType
) : KeyDescription

class SimpleQuery(
    override val id: String,
    override val label: String?,
    override val collection: Collection,
    override val key: KeyDescription,
    override val value: KeyDescription,
    val visualizationProps: VisualizationProps
) : Query