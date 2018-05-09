import com.esotericsoftware.kryo.Kryo
import db.RevisionRepository
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.schedulers.Schedulers
import model.Revision
import parser.RevisionParser
import java.io.File

class Indexer(private val revisionRepository: RevisionRepository) {
    fun parseRecursively(dir: File): Observable<List<Revision>> {
        return dir.walkTopDown()
                .toObservable()
                .flatMap {
                    Observable.just(it)
                            .subscribeOn(Schedulers.io())
                            .filter { it.isFile && it.name.endsWith(".parsed") }
                            .map { RevisionParser(Kryo()).parse(it) }
                }
                .doOnNext {
                    revisionRepository.insert(it)
                    println("Parsed ${it.size} revisions")
                }
    }
}
