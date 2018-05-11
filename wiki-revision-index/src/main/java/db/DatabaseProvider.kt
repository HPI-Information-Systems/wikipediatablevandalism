package db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection


object DatabaseProvider {
    private val dataSource: HikariDataSource by lazy {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/postgres"
        }
        HikariDataSource(config).also {
            it.connection.prepareStatement(CREATE_TABLE).execute()
        }
    }

    fun connection(): Connection = dataSource.connection
}
