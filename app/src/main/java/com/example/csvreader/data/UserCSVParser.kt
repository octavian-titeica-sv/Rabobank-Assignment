package com.example.csvreader.data

import com.example.csvreader.domain.util.EMPTY_STRING
import com.example.csvreader.domain.util.ListOfList
import com.example.csvreader.domain.util.convertStringToDate
import com.example.csvreader.domain.model.UserModel

/**
 * Class meant to parse the entries provided from a CSV into a list of users.
 */
class UserCSVParser {

    private var attributes = mutableListOf<UserAttribute>()
    private var entries: ListOfList<String> = mutableListOf()
    private var shouldIgnoreInvalidFields = true

    /**
     * Enum class used to reverse-lookup for an attribute based on its key
     */
    enum class UserAttribute(val key: String) {
        FIRST_NAME("First name"),
        SUR_NAME("Sur name"),
        ISSUE_COUNT("Issue count"),
        DATE_OF_BIRTH("Date of birth")
    }

    // ----------------------------BUILDER Methods ---------------------------------------
    fun attributes(attributes: List<String>) = apply {
        attributes.forEach { attribute ->
            this.attributes.add(UserAttribute.values().first { it.key == attribute })
        }
    }
    fun entries(entries: ListOfList<String>) = apply { this.entries = entries }
    fun shouldIgnoreInvalidFields(shouldIgnore: Boolean) = apply { this.shouldIgnoreInvalidFields = shouldIgnore }

    fun build() : List<UserModel> {
        val result = mutableListOf<UserModel>()

        entries.forEach { userLine ->
            val userEntry = buildUserEntry(userLine)
            result.add(buildUser(userEntry))
        }

        return removeInvalidUsers(result)
    }
    // ----------------------------END BUILDER Methods -----------------------------------

    /**
     * Method used to build an user, its attributes being stored using a map
     * @param userLine -> represents a line of data from the csv file
     * @return an user entry represented by a map. Each attribute defined in the csv file
     * is represented by a key and its corresponding value is assigned based on the order given
     * by the csv file.
     */
    private fun buildUserEntry(userLine: List<String>): Map<UserAttribute, String> {
        val userEntry = mutableMapOf<UserAttribute, String>()
        userLine.forEachIndexed { index, entry ->
            userEntry[attributes[index]] = entry
        }
        return userEntry.toMap()
    }

    /**
     * Method used to build an UserModel based on an userEntry
     * @param userEntry -> map used to build the actual user model
     * @return the corresponding UserModel
     */
    private fun buildUser(userEntry: Map<UserAttribute, String>) = UserModel(
        firstName = userEntry[UserAttribute.FIRST_NAME],
        surName = userEntry[UserAttribute.SUR_NAME],
        issueCount = userEntry[UserAttribute.ISSUE_COUNT]?.toIntOrNull(),
        dateOfBirth = userEntry[UserAttribute.DATE_OF_BIRTH]?.let { convertStringToDate(it) } ?: EMPTY_STRING
    )

    /**
     * Method used to remove the invalid users from a list of users, based on [shouldIgnoreInvalidFields].
     * An user is considered as being invalid if its firstName and surName are null or empty.
     *
     * @param users -> a list containing all the users
     * @return -> the provided list of users if the flag [shouldIgnoreInvalidFields] is true, or the users
     * without the invalid entries otherwise
     */
    private fun removeInvalidUsers(users: List<UserModel>) = if (!shouldIgnoreInvalidFields)
        users.filter { userModel ->
            userModel.firstName?.isNotEmpty() == true && userModel.surName?.isNotEmpty() == true
        } else users
}
