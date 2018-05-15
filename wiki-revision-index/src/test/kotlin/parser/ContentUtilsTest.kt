package parser

import org.junit.Test

class ContentUtilsTest {
    @Test
    fun emptyListsHaveNoTableChange() {
        val previousTables = listOf<String>()
        val tables = listOf<String>()

        assert(changedTables(previousTables, tables) == 0)
    }

    @Test
    fun countSingleAddedTable() {
        val previousTables = listOf<String>()
        val tables = listOf("Table")

        assert(changedTables(previousTables, tables) == 1)
    }

    @Test
    fun countSingleDeletedTable() {
        val previousTables = listOf("Table")
        val tables = listOf<String>()

        assert(changedTables(previousTables, tables) == 1)
    }

    @Test
    fun countModifiedTableAsDeletionAndInsertion() {
        val previousTables = listOf("Table")
        val tables = listOf("Different Table")

        assert(changedTables(previousTables, tables) == 2)
    }

    @Test
    fun countMultipleTableChanges() {
        val previousTables = listOf("Table", "Deleted Table")
        val tables = listOf("New Table", "Table")

        assert(changedTables(previousTables, tables) == 2)
    }
}
