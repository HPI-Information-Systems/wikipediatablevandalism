package db

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


object SqliteDatabase {
    lateinit var connection: Connection

    fun open(path: String) {
        val url = "jdbc:sqlite:$path"
        connection = DriverManager.getConnection(url)
        ensureTables()
    }

    fun close() = connection.close()

    private fun ensureTables() {
        connection.prepareStatement(CREATE_TABLE).execute()
    }
}