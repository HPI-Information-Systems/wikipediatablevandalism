package utils

import java.io.File

fun File.wikiPages(): List<File> = walkTopDown()
        .filter { it.isFile && it.name.endsWith(".parsed") }
        .toList()
