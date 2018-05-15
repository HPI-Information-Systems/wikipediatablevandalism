import com.esotericsoftware.kryo.Kryo
import db.DatabaseProvider
import db.RevisionRepository
import kotlinx.coroutines.experimental.launch
import parser.RevisionParser
import tracker.ProgressTracker
import utils.wikiPages
import java.io.File

class Indexer(private val tracker: ProgressTracker) {
    fun parseRecursively(dir: File) = dir.wikiPages()
            .map {
                launch {
                    val revisions = RevisionParser(Kryo()).parse(it)
                    RevisionRepository(DatabaseProvider).insert(revisions)
                    tracker.trackPageParsed()
                }
            }
}
