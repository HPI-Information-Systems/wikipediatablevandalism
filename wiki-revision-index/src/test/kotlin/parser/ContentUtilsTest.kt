package parser

import org.junit.Test

class ContentUtilsTest {
    @Test fun hashEqualsIfListsAreEqual() {
        val listOfStrings = listOf("John", "Doe")
        val otherListOfStrings = listOf("John", "Doe")

        assert(hash(listOfStrings) == hash(otherListOfStrings))
    }

    @Test fun hashDiffersIfListsAreEqual() {
        val listOfStrings = listOf("John", "Do")
        val otherListOfStrings = listOf("John", "Doe")

        assert(hash(listOfStrings) != hash(otherListOfStrings))
    }

    @Test fun hashesOfEmptyListsAreEqual() {
        val listOfStrings = listOf<String>()
        val otherListOfStrings = listOf<String>()

        assert(hash(listOfStrings) == hash(otherListOfStrings))
    }
}
