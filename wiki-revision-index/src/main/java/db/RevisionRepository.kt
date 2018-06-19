package db

import model.Revision
import java.math.BigDecimal

const val CREATE_TABLE = """
    CREATE TABLE IF NOT EXISTS Revision (
        id INTEGER,
        page_id INTEGER,
        created_at TEXT,
        table_count INTEGER,
        changed_tables INTEGER,
        PRIMARY KEY (id, page_id)
    );"""
const val DROP_TABLE = "DROP TABLE IF EXISTS Revision"
const val INSERT = "INSERT INTO Revision VALUES (?, ?, ?, ?, ?)"

class RevisionRepository(private val databaseProvider: DatabaseProvider) {
    fun insert(revisions: List<Revision>) {
        databaseProvider.connection().use {
            it.prepareStatement(INSERT).use {
                for (revision in revisions) {
                    it.setBigDecimal(1, BigDecimal(revision.id))
                    it.setBigDecimal(2, BigDecimal(revision.pageId))
                    it.setString(3, revision.createdAt.toString())
                    it.setInt(4, revision.tableCount)
                    it.setInt(5, revision.changedTables)
                    it.addBatch()
                }
                it.executeBatch()
            }
        }
    }
}
