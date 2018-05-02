package db

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

const val DB_NAME = "index.db"

object SqliteDatabase {
    lateinit var connection: Connection

    fun open(dir: File) {
        val url = "jdbc:sqlite:${dir.absolutePath.plus(DB_NAME)}"
        connection = DriverManager.getConnection(url)
        ensureTables()
    }

    fun close() = connection.close()

    private fun ensureTables() {
        connection.prepareStatement(CREATE_TABLE).execute()
    }
}