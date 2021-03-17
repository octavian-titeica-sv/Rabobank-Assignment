package com.example.csvreader.data

import android.content.Context
import com.example.csvreader.R
import com.example.csvreader.base.exceptions.CSVParsingException
import com.example.csvreader.base.util.COMMA_CHAR
import com.example.csvreader.base.util.EMPTY_STRING
import com.example.csvreader.base.util.ESCAPED_QUOTE_STRING
import com.example.csvreader.base.util.ListOfList
import com.example.csvreader.base.util.MutableListOfList
import com.example.csvreader.base.util.NUMBER_OF_EXPECTED_ATTRIBUTES
import com.example.csvreader.base.util.QUOTE_CHAR
import com.example.csvreader.domain.model.UserModel
import java.io.BufferedReader
import java.io.InputStreamReader

private const val FILE_MIN_VALID_LINES = 2
private const val FILE_HEADER_INDEX = 0
private const val FILE_ENTRIES_STARTING_INDEX = 1

private const val INPUT_FILE_PATH = R.raw.issues

class CSVFileReader constructor(private val context: Context) {

    private val separator = COMMA_CHAR
    private val delimiter = QUOTE_CHAR

    fun readUsers(): List<UserModel> {
        val lines = readFile().readLines()
        // Each file should have at least 2 lines, one header and one entry
        if (lines.size < FILE_MIN_VALID_LINES) {
            throw CSVParsingException("The provided file doesn't contain any data")
        }

        return UserBuilder()
            .shouldIgnoreInvalidFields(false)
            .attributes(splitLine(lines[FILE_HEADER_INDEX]))
            .entries(splitLines(lines.subList(FILE_ENTRIES_STARTING_INDEX, lines.size)))
            .build()
    }

    private fun readFile(): BufferedReader {
        val inputStream = context.resources.openRawResource(INPUT_FILE_PATH)
        return BufferedReader(InputStreamReader(inputStream))
    }

    private fun splitLines(lines: List<String>): ListOfList<String> {
        val result : MutableListOfList<String> = mutableListOf()
        lines.forEach { line ->
            result.add(splitLine(line))
        }
        return result
    }

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
