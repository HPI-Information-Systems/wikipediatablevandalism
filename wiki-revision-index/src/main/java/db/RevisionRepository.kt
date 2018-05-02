package db

import model.Revision
import java.math.BigDecimal
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

const val CREATE_TABLE = """
    CREATE TABLE IF NOT EXISTS Revision (
        id INTEGER,
        page_id INTEGER,
        created_at TEXT,
        has_tables INTEGER,
        table_hash TEXT,
        PRIMARY KEY (id, page_id)
    );"""
const val INSERT = "INSERT INTO Revision VALUES (?, ?, ?, ?, ?)"

class RevisionRepository(private val connection: Connection) {
    private var insertStatement: PreparedStatement = connection.prepareStatement(INSERT)

    fun insert(revision: Revision) {
        insertStatement.setBigDecimal(1, BigDecimal(revision.id))
        insertStatement.setBigDecimal(2, BigDecimal(revision.pageId))
        insertStatement.setString(3, revision.createdAt.toString())
        insertStatement.setBoolean(4, revision.hasTables)
        insertStatement.setString(5, revision.tableHash)
        insertStatement.execute()
    }

    fun insert(revisions: List<Revision>) {
        try {
            connection.autoCommit = false

            for (revision in revisions) {
                insert(revision)
            }
            connection.commit()
        } catch (e: SQLException) {
            connection.rollback()
            throw e
        } finally {
            connection.autoCommit = true
        }
    }
}
