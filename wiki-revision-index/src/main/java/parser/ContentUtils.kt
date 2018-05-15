package parser

import org.apache.commons.codec.digest.DigestUtils

fun hash(content: List<String>?): String {
    if (content == null) return ""

    var size = 0
    for (json in content) {
        size += json.length
    }

    val buffer = StringBuffer(size)
    for (json in content) {
        buffer.append(json)
    }

    return DigestUtils.sha1Hex(buffer.toString())
}
