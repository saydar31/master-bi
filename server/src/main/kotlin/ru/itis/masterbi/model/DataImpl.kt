package ru.itis.masterbi.model

class CsvDatasource : Datasource {
    override val type: DatasourceType = DatasourceType.CSV

    lateinit var value: String
    var valueType: DataLocationType = DataLocationType.FILENAME
    var separator: String = ";"
    var nullPlacement: String = "<NULL>"

    enum class DataLocationType{
        FILENAME, URL, LITERAL
    }
}

class SimpleCollection : Collection {
    override lateinit var datasource: Datasource
    override lateinit var name: String
}

class SimpleKeyDescription : KeyDescription {
    override lateinit var name: String
    override lateinit var type: ValueType
}

class SimpleQuery : Query {
    override lateinit var collection: Collection
    override lateinit var key: KeyDescription
    override lateinit var value: KeyDescription
}