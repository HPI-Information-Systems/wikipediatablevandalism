package parser

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import model.Revision
import org.xerial.snappy.SnappyInputStream
import wikixmlsplit.datastructures.MyPageType
import java.io.File
import java.io.FileInputStream


class RevisionParser(private val kryo: Kryo) {
    fun parse(file: File): List<Revision> {
        val page = getPage(file)
        val revisions = mutableListOf<Revision>()
        var previousTables = emptyList<String>()

        for (pageRevision in page.revisions) {
            val tables = pageRevision.parsed ?: emptyList()
            val changedTables = changedTables(previousTables, tables)
            previousTables = tables

            val savedAt = pageRevision.date.toInstant()
            revisions.add(Revision(pageRevision.id, page.id, savedAt, tables.size, changedTables))
        }

        return revisions
    }

    private fun getPage(file: File): MyPageType {
        val inputStream = SnappyInputStream(FileInputStream(file))
        return kryo.readObject(Input(inputStream), MyPageType::class.java)
    }
}
