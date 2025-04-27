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
    override val collection: Collection,
    override val key: KeyDescription,
    override val value: KeyDescription,
    val visualizationProps: VisualizationProps
) : Query