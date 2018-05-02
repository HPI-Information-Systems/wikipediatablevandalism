import db.RevisionRepository
import db.SqliteDatabase
import model.Revision

fun main(args: Array<String>) {
    // TODO: Exchange with proper cmd tool
    assert(args.size == 1, { "Expecting path to local db as argument, e.g. /path/to/your/index.db" })
    val path = args[0]
    SqliteDatabase.open(path)

    val repository = RevisionRepository(SqliteDatabase.connection)
    repository.insert(Revision(100))

    SqliteDatabase.close()
}
