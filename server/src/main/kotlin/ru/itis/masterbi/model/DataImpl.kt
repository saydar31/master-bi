package ru.itis.masterbi.model

class CsvDatasource : Datasource {
    override val type: DatasourceType = DatasourceType.CSV

    lateinit var fileName: String
    var separator: String = ";"
    var nullPlacement: String = "<NULL>"
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