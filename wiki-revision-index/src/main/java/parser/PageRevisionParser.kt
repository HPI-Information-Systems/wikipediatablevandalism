package parser

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import model.AnnotatedRevision
import model.PageRevision
import org.xerial.snappy.SnappyInputStream
import wikixmlsplit.datastructures.MyPageType
import wikixmlsplit.datastructures.MyRevisionType
import java.io.File
import java.io.FileInputStream
import kotlin.math.min

class PageRevisionParser(private val kryo: Kryo, private val maxUpcomingRevisions: Int = 3) {
    fun annotate(pageRevision: PageRevision, file: File): AnnotatedRevision {
        val page = getPage(file)
        val revisions = page.revisions

        for (i in 0 until revisions.size) {
            val revision = revisions[i]

            if (revision.id == pageRevision.revisionId) {
                val max = min(i + maxUpcomingRevisions, revisions.size)

                for (k in i + 1 until max) {
                    val upcomingRevision = revisions[k]
                    val annotatedRevision = annotateRevision(revision, upcomingRevision)

                    if (annotatedRevision.isCandidate()) {
                        return annotatedRevision
                    }
                }
            }
        }

        return AnnotatedRevision(pageRevision.revisionId)
    }

    private fun getPage(file: File): MyPageType {
        val inputStream = SnappyInputStream(FileInputStream(file))
        return kryo.readObject(Input(inputStream), MyPageType::class.java)
    }

    private fun annotateRevision(revision: MyRevisionType, upcoming: MyRevisionType): AnnotatedRevision {
        val hasComment = upcoming.comment != null

        return if (hasComment) {
            val comment = upcoming.comment.value
            val isVandalism = containsVandalism(comment)
            val isRevert = containsRevert(comment)
            val isClueBot = containsClueBot(comment)
            AnnotatedRevision(revision.id, upcoming.id, hasComment, isVandalism, isRevert, isClueBot)
        } else {
            AnnotatedRevision(revision.id, upcoming.id, hasComment)
        }
    }

    private fun containsVandalism(comment: String): Boolean {
        return comment.contains("vandalism", true)
    }

    private fun containsRevert(comment: String): Boolean {
        return comment.contains("revert", true) || comment.contains("rv", true)
    }

    private fun containsClueBot(comment: String): Boolean {
        return comment.contains("cluebot", true)
    }
}
