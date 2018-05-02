package parser

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import model.Revision
import org.xerial.snappy.SnappyInputStream
import wikixmlsplit.datastructures.MyPageType
import java.io.File
import java.io.FileInputStream


class RevisionParser(private val kryo: Kryo) {
    fun parsePage(file: File): List<Revision> {
        val inputStream = SnappyInputStream(FileInputStream(file))
        val page = kryo.readObject(Input(inputStream), MyPageType::class.java)

        return page.revisions
                .map {
                    val savedAt = it.date.toInstant()
                    val hasTables = it.parsed != null
                    val tableHash = hash(it.parsed)
                    Revision(it.id, page.id, savedAt, hasTables, tableHash)
                }
                .toList()
    }
}
