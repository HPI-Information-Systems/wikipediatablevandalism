package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection


object DatabaseProvider {
    private const val HOST = "localhost"
    private const val PORT = 5432
    private const val SCHEMA = "postgres"

    private val dataSource: HikariDataSource by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://$HOST:$PORT/$SCHEMA"
        }
        HikariDataSource(config).also {
            it.connection.prepareStatement(CREATE_TABLE).execute()
        }
    }

    fun connection(): Connection = dataSource.connection
}
