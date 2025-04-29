package ru.itis.masterbi.model

import com.fasterxml.jackson.annotation.JsonIgnore
import ru.itis.masterbi.model.DatasourceType.CSV

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

    enum class DataBase(val driverClassName: String){
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