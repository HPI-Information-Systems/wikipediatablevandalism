package db

import model.Revision
import java.sql.Connection
import java.sql.PreparedStatement

const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Revision (id INTEGER);"
const val INSERT = "INSERT INTO Revision VALUES (?)"

class RevisionRepository(connection: Connection) {
    private var insertStatement: PreparedStatement = connection.prepareStatement(INSERT)

    fun insert(revision: Revision) {
        // TODO: Add more fields
        insertStatement.setInt(1, revision.id)
        insertStatement.execute()
    }
}
