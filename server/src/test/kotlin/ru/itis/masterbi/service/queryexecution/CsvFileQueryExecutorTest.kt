package ru.itis.masterbi.service.queryexecution

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import ru.itis.masterbi.model.*
import ru.itis.masterbi.model.CsvDatasource.DataLocationType.LITERAL
import ru.itis.masterbi.service.queryexecution.csv.CsvFileQueryExecutor
import kotlin.test.Test
import ru.itis.masterbi.model.Collection as BICollection

class CsvFileQueryExecutorTest {

    private val testDataSource = CsvDatasource(
        "foo", LITERAL, ','
    )

    private val testCollection = object : BICollection {
        override val name = "test-collection"
        override var datasource: Datasource = testDataSource
    }

    private val validQuery = object : Query {
        override val id: String
            get() = "id"
        override val label: String? = null
        override val collection = testCollection
        override val key = object : KeyDescription {
            override val name = "id"
            override val type = ValueType.STRING
        }
        override val value = object : KeyDescription {
            override val name = "value"
            override val type = ValueType.NUMBER
        }
    }

    private val executor = CsvFileQueryExecutor()

    @Test
    fun `reads CSV with headers`() {
        val ds = testDataSource.copy(
            value = """
            id,value,description
            1,100,Test 1
            2,200,Test 2
        """.trimIndent()
        )

        validQuery.collection.datasource = ds
        val result = executor.execute(validQuery)

        result.data shouldHaveSize 2
        result.data[0] shouldBe QueryResultUnit("1", "100")
    }

    @Test
    fun `throws when key column missing`() {
        val ds = testDataSource.copy(
            value = """
            wrong,value
            1,100
            """.trimIndent()
        )

        validQuery.collection.datasource = ds

        shouldThrow<IllegalArgumentException> {
            executor.execute(validQuery)
        }.message shouldBe "Mapping for id not found, expected one of [wrong, value]"
    }

    @Test
    fun `handles quoted values`() {
        val ds = testDataSource.copy(
            value = """
            id,value
            "1","100"
            "2","200,50"
            """.trimIndent()
        )

        validQuery.collection.datasource = ds

        val result = executor.execute(validQuery)

        result.data shouldContainExactly listOf(
            QueryResultUnit("1", "100"),
            QueryResultUnit("2", "200,50")
        )
    }

    @Test
    fun `handles null values`() {
        val ds = testDataSource.copy(
            value = """
                id,value
                1,NULL
                2,200
            """.trimIndent(),
            nullPlacement = "NULL"
        )

        validQuery.collection.datasource = ds

        val result = executor.execute(validQuery)
        result.data[0].value.shouldBeNull()
    }

}