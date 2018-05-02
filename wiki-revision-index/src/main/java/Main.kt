import com.esotericsoftware.kryo.Kryo
import db.RevisionRepository
import db.SqliteDatabase
import parser.RevisionParser
import java.io.File

fun main(args: Array<String>) {
    // TODO: Exchange with proper cmd tool
    assert(args.size == 1, { "Expecting path to local db as argument, e.g. /path/to/your/index.db" })
    val path = args[0]
    SqliteDatabase.open(path)

    val repository = RevisionRepository(SqliteDatabase.connection)
    val parser = RevisionParser(Kryo())
    val revisions = parser.parsePage(File("/Users/philipphager/Desktop/595484-King_Missile.parsed"))
    repository.insert(revisions)

    SqliteDatabase.close()
}
