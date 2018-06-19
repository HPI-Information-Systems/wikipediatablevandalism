package commentfilter

import utils.wikiPages
import java.io.File
import java.math.BigInteger

class PageMapper(private val baseDir: File) {
    private val mapping by lazy {
        baseDir.wikiPages()
                .map {
                    val pageId = it.name.substringBefore("-").toBigInteger()
                    pageId to it.path
                }.toMap()
    }

    fun getFile(pageid: BigInteger): File? {
        return File(mapping[pageid])
    }
}
