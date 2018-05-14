package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection


object DatabaseProvider {
    private val HOST = System.getenv("HOST") ?: "localhost"
    private val PORT = System.getenv("PORT") ?: "5432"
    private val SCHEMA = System.getenv("SCHEMA") ?: "postgres"

    private val dataSource: HikariDataSource by lazy {
        val url = "jdbc:postgresql://$HOST:$PORT/$SCHEMA"
        println("Initializing datasource with: $url")

        val config = HikariConfig().apply { jdbcUrl = url }
        HikariDataSource(config).also { it.connection.prepareStatement(CREATE_TABLE).execute() }
    }

    fun connection(): Connection = dataSource.connection
}
