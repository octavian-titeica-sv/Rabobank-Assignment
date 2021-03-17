package com.example.csvreader.data

import android.content.Context
import com.example.csvreader.R
import com.example.csvreader.domain.exceptions.CSVParsingException
import com.example.csvreader.domain.util.COMMA_CHAR
import com.example.csvreader.domain.util.EMPTY_STRING
import com.example.csvreader.domain.util.ESCAPED_QUOTE_STRING
import com.example.csvreader.domain.util.ListOfList
import com.example.csvreader.domain.util.MutableListOfList
import com.example.csvreader.domain.util.NUMBER_OF_EXPECTED_ATTRIBUTES
import com.example.csvreader.domain.util.QUOTE_CHAR
import com.example.csvreader.domain.model.UserModel
import java.io.BufferedReader
import java.io.InputStreamReader

private const val FILE_MIN_VALID_LINES = 2
private const val FILE_HEADER_INDEX = 0
private const val FILE_ENTRIES_STARTING_INDEX = 1

private const val INPUT_FILE_PATH = R.raw.issues

/**
 * Class meant to read all the lines from a given file and return a list of users
 */
class CSVFileReader constructor(private val context: Context) {

    private val separator = COMMA_CHAR
    private val delimiter = QUOTE_CHAR

    /**
     * This is the only exposed method.
     * It's role is to read all the lines (attributes and entries lines) from a file and validate them.
     */
    fun readUsers(): List<UserModel> {
        val lines = readFile().readLines()
        // Each file should have at least 2 lines, one header and one entry
        if (lines.size < FILE_MIN_VALID_LINES) {
            throw CSVParsingException("The provided file doesn't contain any data")
        }

        return UserCSVParser()
            .shouldIgnoreInvalidFields(false)
            .attributes(splitLine(lines[FILE_HEADER_INDEX]))
            .entries(splitLines(lines.subList(FILE_ENTRIES_STARTING_INDEX, lines.size)))
            .build()
    }

    /**
     * Method used to open a file and return a buffered reader
     */
    private fun readFile(): BufferedReader {
        val inputStream = context.resources.openRawResource(INPUT_FILE_PATH)
        return BufferedReader(InputStreamReader(inputStream))
    }

    /**
     * This method receives all the lines in the file and adds all the results returned by
     * splitLine() method into a list of lists.
     */
    private fun splitLines(lines: List<String>): ListOfList<String> {
        val result : MutableListOfList<String> = mutableListOf()
        lines.forEach { line ->
            result.add(splitLine(line))
        }
        return result
    }

    /**
     * Method used to split all the attributes present on a line and insert them in a list.
     * This method also assures that the line contains the number of expected attributes.
     */
    private fun splitLine(line: String, isHeader: Boolean = false): List<String> {
        val result = mutableListOf<String>()
        var token = EMPTY_STRING
        val separatedLine = line + separator
        var quoteOpened = false

        val charArray = separatedLine.toCharArray()
        for (char in charArray) {
            when(char) {
                delimiter -> {
                    quoteOpened = quoteOpened.not()
                }
                separator -> {
                    if (!quoteOpened) {
                        result.add(token)
                        token = EMPTY_STRING
                    } else {
                        token += char
                    }
                }
                else -> token+=char
            }
        }

        return validateLine(result.map {
            it.replace(ESCAPED_QUOTE_STRING, EMPTY_STRING)
            it.trim()
        }, isHeader)
    }

    /**
     * Method used to validate the attributes from a read line.
     * A line is considered valid if the number of elements separated by a comma is equal to the
     * number of expected attributes (NUMBER_OF_EXPECTED_ATTRIBUTES).
     */
    @Throws(Exception::class)
    private fun validateLine(entriesList: List<String>, isHeader: Boolean) : List<String> {
        if (entriesList.size == NUMBER_OF_EXPECTED_ATTRIBUTES) {
            return entriesList
        } else {
            val errorMessage = "Expected $NUMBER_OF_EXPECTED_ATTRIBUTES attributes, but only ${entriesList.size} found."
            if (isHeader) {
                throw CSVParsingException("Invalid header, $errorMessage")
            } else {
                throw CSVParsingException("Invalid entry, $errorMessage")
            }
        }
    }
}
