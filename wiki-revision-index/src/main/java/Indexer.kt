import db.RevisionRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import model.Revision
import parser.RevisionParser
import java.io.File

class Indexer(private val revisionParser: RevisionParser,
              private val revisionRepository: RevisionRepository) {
    fun parseRecursively(dir: File): Observable<List<Revision>> {
        println("Parsing wiki files on ${Runtime.getRuntime().availableProcessors()} threads in parallel")
        return dir.walkTopDown()
                .toObservable()
                .subscribeOn(Schedulers.computation())
                .filter { it.isFile && it.name.endsWith(".parsed") }
                .map { revisionParser.parse(it) }
                .doOnNext {
                    revisionRepository.insert(it)
                    println("Parsed ${it.size} revisions")
                }
    }
}
