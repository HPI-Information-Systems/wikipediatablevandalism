package indexer

import com.esotericsoftware.kryo.Kryo
import db.DatabaseProvider
import db.RevisionRepository
import kotlinx.coroutines.experimental.async
import parser.RevisionParser
import tracker.ProgressTracker
import java.io.File

class Indexer(private val databaseProvider: DatabaseProvider,
              private val tracker: ProgressTracker) {
    fun parse(files: List<File>) = files.map {
        async {
            val revisions = RevisionParser(Kryo()).parse(it)
            RevisionRepository(databaseProvider).insert(revisions)
            tracker.trackPageParsed()
        }
    }
}
