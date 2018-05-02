import com.esotericsoftware.kryo.Kryo
import db.RevisionRepository
import db.SqliteDatabase
import parser.RevisionParser
import java.io.File

fun main(args: Array<String>) {
    // TODO: Exchange with proper cmd tool
    assert(args.size == 2, { "Expecting path to local db and folder to parse, e.g. /path/to/index.db /path/to/dataSample" })
    val indexPath = args[0]
    val dataPath = args[1]

    SqliteDatabase.open(indexPath)

    val repository = RevisionRepository(SqliteDatabase.connection)
    val parser = RevisionParser(Kryo())
    Indexer(parser, repository)
            .parseRecursively(File(dataPath))
            .blockingSubscribe()

    SqliteDatabase.close()
}
