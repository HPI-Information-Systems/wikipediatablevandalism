package commentfilter

import com.esotericsoftware.kryo.Kryo
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import model.PageRevision
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVPrinter
import parser.PageRevisionParser
import tracker.ProgressTracker
import utils.printAnnotatedRevision
import utils.readPageRevisions
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class VandalismRevisionFinder {
    fun parse(wikiFilesDir: File, csv: File, output: File) = runBlocking {
        val pageMapper = PageMapper(wikiFilesDir)
        val reader = CSVParser(FileReader(csv), CSVFormat.DEFAULT.withHeader("page_id", "revision_id"))
        val writer = CSVPrinter(PrintWriter(output), CSVFormat.DEFAULT.withHeader("revision_id", "upcoming_revision_id", "upcoming_has_comment", "upcoming_marks_vandalism", "upcoming_is_revert", "upcoming_is_cluebot"))
        val pageRevisions = reader.readPageRevisions()
        val progressTracker = ProgressTracker(pageRevisions.size.toLong())

        pageRevisions
                .map { parsePageRevision(pageMapper, it) }
                .forEach {
                    writer.printAnnotatedRevision(it.await())
                    progressTracker.trackPageParsed()
                }

        progressTracker.stop()
    }

    private fun parsePageRevision(pageMapper: PageMapper, pageRevision: PageRevision) = async {
        val file = pageMapper.getFile(pageRevision.pageId) ?: error("Cannot find page with id: ${pageRevision.pageId}")
        return@async PageRevisionParser(Kryo()).annotate(pageRevision, file)
    }
}
