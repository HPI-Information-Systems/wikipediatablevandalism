package parser

import org.apache.commons.codec.digest.DigestUtils

fun hash(content: List<String>?): String {
    if (content == null) return ""

    val contentBytes = content
            .flatMap { it.toByteArray().asIterable() }
            .toByteArray()
    return DigestUtils.sha1Hex(contentBytes)
}
