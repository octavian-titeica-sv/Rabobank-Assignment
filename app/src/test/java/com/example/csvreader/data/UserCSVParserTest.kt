package com.example.csvreader.data

import com.example.csvreader.domain.model.UserModel
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.text.ParseException
import java.util.Random

class UserCSVParserTest {

    private val userParser = UserCSVParser()

    private val attributes = mutableListOf("First name", "Sur name", "Issue count", "Date of birth")
    private val entries = mutableListOf(listOf("Theo", "Jansen", "5", "1978-01-02T00:00:00"))
    private val expectedUserModel = UserModel("Theo", "Jansen", 5, "Jan 2, 1978")

    @Before
    fun setup() {
        userParser.attributes(attributes)
        userParser.entries(entries)
    }

    @Test
    fun `given the entries, when the parser is build, then the corresponding user is returned`() {
        // when
        val result = userParser.build()

        // then
        assert(result == listOf(expectedUserModel))
    }

    @Test
    fun `given the csv file header, when attributes are shuffled, then the parsing is successful`() {
        repeat(10) {
            // given
            val seed = System.nanoTime()
            attributes.shuffle(Random(seed))
            entries.shuffle(Random(seed))

            userParser.attributes(attributes)
            userParser.entries(entries)

            // when
            val result = userParser.build()
            val userResult = result[0]

            // then
            assert(result.size == 1)
            assert(userResult == expectedUserModel)
        }
    }

    @Test
    fun `given the shouldIgnoreInvalidFields flag is true, when the name of the user is empty, then the user without name is returned`() {
        // given
        userParser.shouldIgnoreInvalidFields(true)
        userParser.entries(mutableListOf(listOf("", "Jansen", "5", "1978-01-02T00:00:00")))

        // when
        val result = userParser.build()

        // then
        assert(result.size == 1)
        assert(result[0] == expectedUserModel.copy(firstName = ""))
    }

    @Test
    fun `given the shouldIgnoreInvalidFields flag is true, when the surname of the user is empty, then the user without surname is returned`() {
        // given
        userParser.shouldIgnoreInvalidFields(true)
        userParser.entries(mutableListOf(listOf("Theo", "", "5", "1978-01-02T00:00:00")))

        // when
        val result = userParser.build()

        // then
        assert(result.size == 1)
        assert(result[0] == expectedUserModel.copy(surName = ""))
    }

    @Test
    fun `given the shouldIgnoreInvalidFields flag is false, when the name of the user is empty, then the specific user isn't returned`() {
        // given
        userParser.shouldIgnoreInvalidFields(false)
        userParser.entries(mutableListOf(listOf("", "Jansen", "5", "1978-01-02T00:00:00")))

        // when
        val result = userParser.build()

        // then
        assert(result.isEmpty())
    }

    @Test
    fun `given the shouldIgnoreInvalidFields flag is false, when the surname of the user is empty, then the specific user isn't returned`() {
        // given
        userParser.shouldIgnoreInvalidFields(false)
        userParser.entries(mutableListOf(listOf("Theo", "", "5", "1978-01-02T00:00:00")))

        // when
        val result = userParser.build()

        // then
        assert(result.isEmpty())
    }

    @Test
    fun `given an entry row, when the date of birth field is empty, then the user date of birth is going to be empty`() {
        // given
        userParser.shouldIgnoreInvalidFields(false)
        userParser.entries(mutableListOf(listOf("Theo", "Jansen", "5", "")))

        // when
        var result = listOf<UserModel>()
        try {
            result = userParser.build()
        } catch (e: Exception) {
            assert(e is ParseException)
        }

        // then
        assert(result[0] == expectedUserModel.copy(dateOfBirth = ""))
    }
}
