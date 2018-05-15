package db

/**
 * Connection pool size calculation suggested by PostgreSQL:
 * @see <a href="https://wiki.postgresql.org/wiki/Number_Of_Database_Connections">Number Of Database Connections</a>
 */
fun maxConnectionPoolSize() = 2 * Runtime.getRuntime().availableProcessors()
