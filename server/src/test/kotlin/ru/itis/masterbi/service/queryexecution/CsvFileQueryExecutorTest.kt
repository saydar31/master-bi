package ru.itis.masterbi.service.queryexecution

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.io.TempDir
import ru.itis.masterbi.model.*
import ru.itis.masterbi.model.CsvDatasource.DataLocationType.LITERAL
import ru.itis.masterbi.service.queryexecution.csv.CsvFileQueryExecutor
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.Test
import kotlin.text.Charsets.UTF_8
import ru.itis.masterbi.model.Collection as BICollection

class CsvFileQueryExecutorTest {

    private val testDataSource = CsvDatasource().apply {
        separator = ","
        nullPlacement = "NULL"
        valueType = LITERAL
    }

    private val testCollection = object : BICollection {
        override val name = "test-collection"
        override val datasource: Datasource = testDataSource
    }

    private val validQuery = object : Query {
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
        testDataSource.value = """
            id,value,description
            1,100,Test 1
            2,200,Test 2
        """

        val result = executor.execute(validQuery)

        result.data shouldHaveSize 2
        result.data[0] shouldBe QueryResultUnit("1", "100")
    }

    @Test
    fun `throws when key column missing`() {
        testDataSource.value = """
            wrong,value
            1,100
        """

        shouldThrow<IllegalArgumentException> {
            executor.execute(validQuery)
        }.message shouldBe "Mapping for id not found, expected one of [wrong, value]"
    }

    @Test
    fun `handles quoted values`() {
        testDataSource.value = """
            id,value
            "1","100"
            "2","200,50"
        """
        val result = executor.execute(validQuery)

        result.data shouldContainExactly listOf(
            QueryResultUnit("1", "100"),
            QueryResultUnit("2", "200,50")
        )
    }

    @Test
    fun `handles null values`() {
        testDataSource.apply {
            value = """
                id,value
                1,NULL
                2,200
            """
            nullPlacement = "NULL"
        }

        val result = executor.execute(validQuery)
        result.data[0].value.shouldBeNull()
    }

}